import type { Middleware, MiddlewareCallbackParams } from "openapi-fetch";

import type { ProblemDetailsDto } from "../../types";
import { apiClient } from "./client";
import { createApiRequestError, getNetworkErrorMessage } from "./error";

const errorMiddleware: Middleware = {
  onError: ({
    error
  }: MiddlewareCallbackParams & {
    error: unknown;
  }) => new Error(getNetworkErrorMessage(error), { cause: error })
};

const isProblemDetails = (value: unknown): value is ProblemDetailsDto => {
  if (!value || typeof value !== "object") {
    return false;
  }

  const problem: Record<string, unknown> = value as Record<string, unknown>;

  return (
    typeof problem["type"] === "string" &&
    typeof problem["title"] === "string" &&
    typeof problem["detail"] === "string" &&
    typeof problem["status"] === "number"
  );
};

const getProblemDetails = (value: unknown): ProblemDetailsDto | undefined =>
  isProblemDetails(value) ? value : undefined;

apiClient.use(errorMiddleware);

interface ResponseResult<T> {
  data?: T;
  error?: unknown;
  response: Response;
}

interface NoContentResult {
  error?: unknown;
  response: Response;
}

export const assertResponse = <T>(result: ResponseResult<T>): T => {
  if (result.data !== undefined) {
    return result.data;
  }
  throw createApiRequestError(result.response.status, getProblemDetails(result.error));
};

export const assertNoContent = (result: NoContentResult): void => {
  if (result.response.ok) {
    return;
  }
  throw createApiRequestError(result.response.status, getProblemDetails(result.error));
};
