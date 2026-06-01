/* eslint-disable @typescript-eslint/naming-convention */
export interface ProblemDetailsDto {
  readonly type: string;
  readonly title: string;
  readonly detail: string;
  readonly status: number;
  readonly instance: string;
  readonly traceId: string;
  readonly timestamp: string;
}

export interface BatteryDto {
  readonly id?: string;
  readonly registeredAt: string;
  readonly batteryStatus: number;
}

export interface TemperatureDto {
  readonly id?: string;
  readonly registeredAt: string;
  readonly temp: number;
}

export interface HumidityDto {
  readonly id?: string;
  readonly registeredAt: string;
  readonly airHumidity: number;
}

interface QueryDateOrTimestamp {
  readonly registeredAt: string;
  readonly dateOnly: boolean;
}

interface ReadingsProblemResponse {
  readonly content: {
    "application/problem+json": ProblemDetailsDto;
  };
}

interface ReadingsSuccessResponse<TReading> {
  readonly content: {
    "application/json": TReading[];
  };
}

interface ReadingsDateOrTimestampOperation<TReading> {
  readonly parameters: {
    readonly query: QueryDateOrTimestamp;
  };
  readonly responses: {
    readonly 200: ReadingsSuccessResponse<TReading>;
    readonly 400: ReadingsProblemResponse;
    readonly 401: ReadingsProblemResponse;
    readonly 403: ReadingsProblemResponse;
    readonly 404: ReadingsProblemResponse;
    readonly 429: ReadingsProblemResponse;
    readonly 500: ReadingsProblemResponse;
  };
}

export interface ApiPaths {
  readonly "/api/v1/batteries": {
    readonly get: ReadingsDateOrTimestampOperation<BatteryDto>;
  };
  readonly "/api/v1/temperatures": {
    readonly get: ReadingsDateOrTimestampOperation<TemperatureDto>;
  };
  readonly "/api/v1/humidities": {
    readonly get: ReadingsDateOrTimestampOperation<HumidityDto>;
  };
}
