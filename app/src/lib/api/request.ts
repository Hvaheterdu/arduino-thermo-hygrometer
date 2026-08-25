import type { Middleware, MiddlewareCallbackParams } from "openapi-fetch";

import { apiClient } from "@/lib/api/client";
import type { ProblemDetailsDto } from "@/types/domain";

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

  const problem = value as Record<string, unknown>;

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

type ResponseResult<T> = {
  data?: T;
  error?: unknown;
  response: Response;
};

type NoContentResult = {
  error?: unknown;
  response: Response;
};

export const assertResponse = <T>(result: ResponseResult<T>): T => {
  if (result.data !== undefined) {
    return result.data;
  }

  throw createApiRequestError(result.response.status, getProblemDetails(result.error));
};

export const assertResponseOrEmpty = <T>(result: ResponseResult<T[]>): T[] => {
  if (result.data !== undefined) {
    return result.data;
  }

  if (result.response.status === 404) {
    return [];
  }

  throw createApiRequestError(result.response.status, getProblemDetails(result.error));
};

export const assertNoContent = (result: NoContentResult): void => {
  if (result.response.ok) {
    return;
  }

  throw createApiRequestError(result.response.status, getProblemDetails(result.error));
};
