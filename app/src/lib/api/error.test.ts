import { describe, expect, it } from "vitest";

import { createApiRequestError, getNetworkErrorMessage, getUserFacingErrorMessage, isApiRequestError } from "./error";

describe("API errors", () => {
  it("identifies API request errors", () => {
    const error = createApiRequestError(404);

    expect(isApiRequestError(error)).toBe(true);
    expect(isApiRequestError(new Error("failed"))).toBe(false);
  });

  it("returns useful messages for known statuses", () => {
    expect(getUserFacingErrorMessage(401)).toBe("The API key is missing or invalid.");
    expect(getUserFacingErrorMessage(404)).toBe("No matching measurements were found.");
    expect(getUserFacingErrorMessage(429)).toBe("Too many requests. Please try again shortly.");
  });

  it("uses validation details for bad requests", () => {
    expect(
      getUserFacingErrorMessage(400, {
        detail: "Invalid value",
        errors: [
          {
            description: "Must be within range."
          }
        ],
        instance: "/api/v1/temperatures",
        status: 400,
        timestamp: "2026-08-24T12:00:00Z",
        title: "Bad request",
        traceId: "trace",
        type: "validation"
      })
    ).toBe("Some submitted values are invalid.");
  });

  it("handles network errors", () => {
    expect(getNetworkErrorMessage(new TypeError("Failed to fetch"))).toBe(
      "The API could not be reached. Check that the backend is running."
    );

    expect(getNetworkErrorMessage(new DOMException("cancelled", "AbortError"))).toBe("The request was cancelled.");
  });
});
