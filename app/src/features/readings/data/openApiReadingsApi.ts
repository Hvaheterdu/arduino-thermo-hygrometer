import { apiClient } from "../../../shared/api/apiClient";
import { ApiError, mapApiError } from "../../../shared/api/apiError";
import type { BatteryDto, HumidityDto, TemperatureDto } from "../../../shared/api/apiSchema";
import type { HistoryRequest, ReadingsApi } from "../../../shared/api/readingsApi";
import { frontendLogger } from "../../../shared/observability/logger";

interface ReadingsClientResponse<TData> {
  readonly data?: TData;
  readonly error?: unknown;
}

export interface OpenApiClientLike {
  GET: (
    path: "/api/v1/batteries" | "/api/v1/temperatures" | "/api/v1/humidities",
    options: {
      readonly params: {
        readonly query: {
          readonly registeredAt: string;
          readonly dateOnly: boolean;
        };
      };
    }
  ) => Promise<ReadingsClientResponse<unknown>>;
}

const ensureData = <TReading>(data: TReading[] | undefined): TReading[] => {
  return Array.isArray(data) ? data : [];
};

const normalizeProblemPayload = (error: unknown): unknown => {
  if (typeof error === "object" && error !== null && "error" in error) {
    return (error as { error?: unknown }).error;
  }

  return error;
};

const defaultRetryAttempts = 2;
const maximumRetryAttempts = 3;
const baseRetryDelayMilliseconds = 200;

const resolveRetryAttempts = (): number => {
  const parsedRetryAttempts = Number(import.meta.env.VITE_API_RETRY_ATTEMPTS);

  if (!Number.isFinite(parsedRetryAttempts)) {
    return defaultRetryAttempts;
  }

  return Math.min(maximumRetryAttempts, Math.max(0, Math.floor(parsedRetryAttempts)));
};

const shouldRetryApiError = (apiError: ApiError): boolean => {
  if (apiError.statusCode === undefined) {
    return true;
  }

  return apiError.statusCode === 429 || apiError.statusCode >= 500;
};

const waitForRetryDelay = async (attemptNumber: number): Promise<void> => {
  const retryDelayMilliseconds = baseRetryDelayMilliseconds * 2 ** (attemptNumber - 1);
  await new Promise((resolve) => {
    setTimeout(resolve, retryDelayMilliseconds);
  });
};

const getReadings = async <TDto>(
  client: OpenApiClientLike,
  path: "/api/v1/batteries" | "/api/v1/temperatures" | "/api/v1/humidities",
  request: HistoryRequest
): Promise<TDto[]> => {
  const retryAttempts = resolveRetryAttempts();

  for (let attemptNumber = 0; attemptNumber <= retryAttempts; attemptNumber += 1) {
    try {
      const response = await client.GET(path, {
        params: {
          query: {
            registeredAt: request.registeredAt,
            dateOnly: request.dateOnly
          }
        }
      });

      if (response.error !== undefined) {
        throw mapApiError(normalizeProblemPayload(response.error));
      }

      return ensureData(response.data as TDto[] | undefined);
    } catch (error) {
      const mappedApiError = error instanceof ApiError ? error : mapApiError(error);
      const canRetry = attemptNumber < retryAttempts && shouldRetryApiError(mappedApiError);

      frontendLogger.warn("Readings API call failed.", {
        path,
        attemptNumber,
        retryAttempts,
        canRetry,
        statusCode: mappedApiError.statusCode,
        traceId: mappedApiError.traceId,
        message: mappedApiError.message
      });

      if (!canRetry) {
        throw mappedApiError;
      }

      await waitForRetryDelay(attemptNumber + 1);
    }
  }

  throw new ApiError("The API request could not be completed.");
};

export class OpenApiReadingsApi implements ReadingsApi {
  constructor(private readonly client: OpenApiClientLike = apiClient) {}

  getBatteries(request: HistoryRequest): Promise<BatteryDto[]> {
    return getReadings<BatteryDto>(this.client, "/api/v1/batteries", request);
  }

  getTemperatures(request: HistoryRequest): Promise<TemperatureDto[]> {
    return getReadings<TemperatureDto>(this.client, "/api/v1/temperatures", request);
  }

  getHumidities(request: HistoryRequest): Promise<HumidityDto[]> {
    return getReadings<HumidityDto>(this.client, "/api/v1/humidities", request);
  }
}
