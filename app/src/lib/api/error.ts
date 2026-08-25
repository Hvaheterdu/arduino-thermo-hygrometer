import type { ProblemDetailsDto } from "../../types";

type ApiRequestError = Error & {
  status: number;
  problem?: ProblemDetailsDto | undefined;
};

export const isApiRequestError = (error: unknown): error is ApiRequestError =>
  error instanceof Error && "status" in error && typeof error.status === "number";

const createApiRequestError = (status: number, problem?: ProblemDetailsDto): ApiRequestError => {
  const message: string = getUserFacingErrorMessage(status, problem);
  const error: ApiRequestError = new Error(message) as ApiRequestError;
  error.name = "ApiRequestError";
  error.status = status;
  error.problem = problem;
  return error;
};

export const getUserFacingErrorMessage = (status: number, problem?: ProblemDetailsDto): string => {
  switch (status) {
    case 400:
      return problem?.errors?.length ? "Some submitted values are invalid." : "The request could not be processed.";
    case 401:
      return "The API key is missing or invalid.";
    case 403:
      return "You do not have permission to access this data.";
    case 404:
      return "No matching measurements were found.";
    case 429:
      return "Too many requests. Please try again shortly.";
    case 500:
      return "The server could not complete the request.";
    default:
      return problem?.detail ?? "Something went wrong while contacting the API.";
  }
};

export const getNetworkErrorMessage = (error: unknown): string => {
  if (error instanceof DOMException && error.name === "AbortError") {
    return "The request was cancelled.";
  }
  if (error instanceof TypeError) {
    return "The API could not be reached. Check that the backend is running.";
  }
  return "Something went wrong while contacting the API.";
};

export { createApiRequestError };
