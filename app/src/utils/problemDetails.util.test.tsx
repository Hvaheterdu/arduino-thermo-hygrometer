import { describe, expect, it } from "vitest";

import type { ProblemDetailsDto } from "@/types/problemDetails.types";
import { toApiError, toErrorMessage } from "@/utils/problemDetails.util";

describe("problemDetails.util", () => {
  describe("toApiError", () => {
    it("falls back to a generic message when no problem-details payload is present", () => {
      const problemDetails = undefined;

      const error = toApiError(problemDetails);

      expect(error).toEqual({
        status: 0,
        title: "Something went wrong",
        detail: "An unexpected error occurred. Please try again.",
        errors: []
      });
    });

    it("maps a problem-details payload to an ApiError", () => {
      const problemDetails: ProblemDetailsDto = {
        type: "https://api.arduinothermohygrometer/errors/resource-not-found",
        title: "Resource not found.",
        detail: "Battery with id 00000000-0000-0000-0000-000000000000 not found.",
        status: 404,
        instance: "/api/v1/batteries",
        traceId: "trace-id",
        timestamp: "2017-07-21T17:00:00Z"
      };

      const error = toApiError(problemDetails);

      expect(error).toEqual({
        status: 404,
        title: "Resource not found.",
        detail: "Battery with id 00000000-0000-0000-0000-000000000000 not found.",
        errors: []
      });
    });

    it("normalizes a transport failure without exposing implementation details", () => {
      const transportError = new TypeError("fetch failed");

      const error = toApiError(transportError);

      expect(error).toEqual({
        status: 0,
        title: "Something went wrong",
        detail: "An unexpected error occurred. Please try again.",
        errors: []
      });
    });
  });

  describe("toErrorMessage", () => {
    it("returns the detail message when there are no validation errors", () => {
      const error = { status: 404, title: "Not found", detail: "Not found.", errors: [] };

      const message = toErrorMessage(error);

      expect(message).toBe("Not found.");
    });

    it("appends field-level validation messages", () => {
      const error = {
        status: 400,
        title: "Bad request",
        detail: "One or more fields are invalid.",
        errors: [{ description: "must not be null", parameter: "batteryStatus", header: null, pointer: null }]
      };

      const message = toErrorMessage(error);

      expect(message).toBe("One or more fields are invalid. (batteryStatus: must not be null)");
    });
  });
});
