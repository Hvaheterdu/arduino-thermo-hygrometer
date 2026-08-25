import { describe, expect, it } from "vitest";

import { dateToRegisteredAt, getToday, isValidDate, isValidDateTime, latestByRegisteredAt } from "@/lib/date/date";

describe("date helpers", () => {
  it("converts a date to the backend timestamp format", () => {
    expect(dateToRegisteredAt("2026-08-24")).toBe("2026-08-24T00:00:00");
  });

  it("validates calendar dates", () => {
    expect(isValidDate("2026-08-24")).toBe(true);
    expect(isValidDate("2026-02-30")).toBe(false);
    expect(isValidDate("2026-13-01")).toBe(false);
    expect(isValidDate("not-a-date")).toBe(false);
  });

  it("validates local date and time values", () => {
    expect(isValidDateTime("2026-08-24T12:30")).toBe(true);
    expect(isValidDateTime("2026-02-30T12:30")).toBe(false);
    expect(isValidDateTime("2026-08-24T25:00")).toBe(false);
    expect(isValidDateTime("2026-08-24T12:30:00")).toBe(false);
  });

  it("returns the latest reading", () => {
    expect(
      latestByRegisteredAt([
        { registeredAt: "2026-08-24T10:00:00", value: 1 },
        { registeredAt: "2026-08-24T11:00:00", value: 2 }
      ])
    ).toEqual({
      registeredAt: "2026-08-24T11:00:00",
      value: 2
    });
  });

  it("returns undefined for an empty list", () => {
    expect(latestByRegisteredAt([])).toBeUndefined();
  });

  it("returns a local calendar date", () => {
    expect(getToday()).toMatch(/^\d{4}-\d{2}-\d{2}$/u);
  });
});
