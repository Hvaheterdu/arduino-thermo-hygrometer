import { describe, expect, it } from "vitest";

import { formatRegisteredAt, toDateTimeLocalInputValue, toRegisteredAtQueryValue } from "@/utils/date.util";

describe("date.util", () => {
  describe("toRegisteredAtQueryValue", () => {
    it("returns a full ISO timestamp from ISO input", () => {
      const date = new Date("2017-07-21T17:00:00Z");

      const queryValue = toRegisteredAtQueryValue(date.toISOString());

      expect(queryValue).toBe("2017-07-21T17:00:00.000Z");
    });

    it("handles datetime-local format string input", () => {
      const dateTimeLocalString = "2017-07-21T17:00";

      const queryValue = toRegisteredAtQueryValue(dateTimeLocalString);

      expect(queryValue).toMatch(/2017-07-21T\d{2}:\d{2}:\d{2}\.\d{3}Z/);
    });
  });

  describe("toDateTimeLocalInputValue", () => {
    it("preserves the local date and time for a datetime-local input", () => {
      const localDate = new Date(2017, 6, 21, 17, 35);

      const inputValue = toDateTimeLocalInputValue(localDate);

      expect(inputValue).toBe("2017-07-21T17:35");
    });
  });

  describe("formatRegisteredAt", () => {
    it("formats a valid ISO timestamp into a human-readable string", () => {
      const isoTimestamp = "2017-07-21T17:00:00Z";

      const formatted = formatRegisteredAt(isoTimestamp);

      expect(formatted).toMatch(/21 Jul 2017, \d{2}:\d{2}/);
    });

    it("returns the original string when it cannot be parsed", () => {
      const unparsableValue = "not-a-date";

      const formatted = formatRegisteredAt(unparsableValue);

      expect(formatted).toBe(unparsableValue);
    });

    it("returns only date when dateOnly is set to true", () => {
      const isoTimestamp = "2017-07-21T17:00:00Z";

      const formatted = formatRegisteredAt(isoTimestamp, true);

      expect(formatted).toBe("21 Jul 2017");
    });
  });
});
