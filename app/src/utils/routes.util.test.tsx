import { describe, expect, it } from "vitest";

import { buildHistoryPath, isResourceKind } from "@/utils/routes.util";

describe("routes.util", () => {
  describe("isResourceKind", () => {
    it.each(["battery", "humidity", "temperature"])("accepts %s as a valid resource kind", (value) => {
      const result = isResourceKind(value);
      expect(result).toBe(true);
    });

    it("rejects an unknown string", () => {
      const value = "unknown";

      const result = isResourceKind(value);

      expect(result).toBe(false);
    });

    it("rejects undefined", () => {
      const result = isResourceKind(undefined);
      expect(result).toBe(false);
    });
  });

  describe("buildHistoryPath", () => {
    it("builds a /history/:resource path", () => {
      const path = buildHistoryPath("temperature");
      expect(path).toBe("/history/temperature");
    });
  });
});
