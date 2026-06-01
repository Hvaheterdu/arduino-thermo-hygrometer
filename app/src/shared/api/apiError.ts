import { frontendLogger } from "../observability/logger";
import type { ProblemDetailsDto } from "./apiSchema";

export class ApiError extends Error {
  readonly statusCode?: number;
  readonly traceId?: string;

  constructor(message: string, statusCode?: number, traceId?: string) {
    super(message);
    this.name = "ApiError";
    if (statusCode !== undefined) {
      this.statusCode = statusCode;
    }
    if (traceId !== undefined) {
      this.traceId = traceId;
    }
  }
}

const isProblemDetails = (value: unknown): value is ProblemDetailsDto => {
  if (typeof value !== "object" || value === null) {
    return false;
  }

  const candidate = value as Partial<ProblemDetailsDto>;
  return (
    typeof candidate.title === "string" &&
    typeof candidate.detail === "string" &&
    typeof candidate.status === "number" &&
    typeof candidate.traceId === "string"
  );
};

export const mapApiError = (error: unknown): ApiError => {
  if (isProblemDetails(error)) {
    const mappedProblemDetailsError =
      error.status === 401 || error.status === 403 ?
        new ApiError(
          "You are not authorized to access this data. Please verify your API key.",
          error.status,
          error.traceId
        )
      : error.status === 404 ? new ApiError("The requested API resource was not found.", error.status, error.traceId)
      : error.status === 429 ?
        new ApiError("Too many requests were sent. Please retry shortly.", error.status, error.traceId)
      : error.status >= 500 ?
        new ApiError("The API is currently unavailable. Please try again shortly.", error.status, error.traceId)
      : new ApiError(error.detail, error.status, error.traceId);

    frontendLogger.warn("Mapped API problem-details response to frontend ApiError.", {
      statusCode: mappedProblemDetailsError.statusCode,
      traceId: mappedProblemDetailsError.traceId
    });

    return mappedProblemDetailsError;
  }

  if (error instanceof Error) {
    if (error.name === "AbortError") {
      return new ApiError("The API request timed out. Please retry.");
    }

    if (error.name === "TypeError") {
      return new ApiError("Unable to reach the API. Please check your connection and base URL settings.");
    }

    return new ApiError(error.message);
  }

  return new ApiError("An unknown API error occurred.");
};
