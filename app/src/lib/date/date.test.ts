import { describe, expect, it } from "vitest";

import { dateToRegisteredAt, getToday, isValidDate, latestByRegisteredAt } from "@/lib";

describe("date helpers", () => {
  it("converts a calendar date to the backend local timestamp format", () => {
    expect(dateToRegisteredAt("2026-08-24")).toBe("2026-08-24T00:00:00");
  });

  it("validates calendar dates", () => {
    expect(isValidDate("2026-08-24")).toBe(true);
    expect(isValidDate("2026-02-30")).toBe(false);
  });

  it("returns the latest reading", () => {
    const result = latestByRegisteredAt([
      { registeredAt: "2026-08-24T10:00:00", value: 1 },
      { registeredAt: "2026-08-24T11:00:00", value: 2 }
    ]);

    expect(result?.value).toBe(2);
  });

  it("returns a local calendar date", () => {
    expect(getToday()).toMatch(/^\d{4}-\d{2}-\d{2}$/);
  });
});
