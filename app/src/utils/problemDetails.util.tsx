import type { ApiError, ProblemDetailsDto } from "@/types/problemDetails.types";

const UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred. Please try again.";

const isApiError = (error: unknown): error is ApiError => {
  if (!error || typeof error !== "object") {
    return false;
  }

  return (
    "status" in error &&
    typeof error.status === "number" &&
    "title" in error &&
    typeof error.title === "string" &&
    "detail" in error &&
    typeof error.detail === "string" &&
    "errors" in error &&
    Array.isArray(error.errors)
  );
};

const isProblemDetails = (error: unknown): error is ProblemDetailsDto => {
  if (!error || typeof error !== "object") {
    return false;
  }

  return (
    "status" in error &&
    typeof error.status === "number" &&
    "title" in error &&
    typeof error.title === "string" &&
    "detail" in error &&
    typeof error.detail === "string"
  );
};

export const toApiError = (error: unknown): ApiError => {
  if (isApiError(error)) {
    return error;
  }

  if (!isProblemDetails(error)) {
    return { status: 0, title: "Something went wrong", detail: UNEXPECTED_ERROR_MESSAGE, errors: [] };
  }

  return {
    status: error.status,
    title: error.title,
    detail: error.detail,
    errors: error.errors ?? []
  };
};

export const toErrorMessage = (error: ApiError): string => {
  if (error.errors.length === 0) {
    return error.detail;
  }

  const fieldMessages = error.errors
    .map((validationError) => {
      const field = validationError.parameter ?? validationError.header ?? validationError.pointer;
      return field ? `${field}: ${validationError.description}` : validationError.description;
    })
    .join(", ");

  return `${error.detail} (${fieldMessages})`;
};
