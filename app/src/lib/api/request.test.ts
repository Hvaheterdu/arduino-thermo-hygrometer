import { describe, expect, it } from "vitest";

import { assertNoContent, assertResponse } from "./request";

const response = (status: number): Response => new Response(null, { status });

describe("API response helpers", () => {
  it("returns response data", () => {
    expect(
      assertResponse({
        data: { value: 42 },
        response: response(200)
      })
    ).toEqual({ value: 42 });
  });

  it("throws when a response has no data", () => {
    expect(() =>
      assertResponse({
        error: { detail: "Not found" },
        response: response(404)
      })
    ).toThrow("No matching measurements were found.");
  });

  it("accepts successful no-content responses", () => {
    expect(() => assertNoContent({ response: response(204) })).not.toThrow();
  });

  it("throws for failed no-content responses", () => {
    expect(() => assertNoContent({ response: response(403) })).toThrow(
      "You do not have permission to access this data."
    );
  });
});
