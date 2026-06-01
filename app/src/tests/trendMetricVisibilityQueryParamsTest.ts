import { describe, expect, it } from "vitest";
import {
  buildSearchParamsWithUpdatedTrendMetricVisibility,
  hasAtLeastOneVisibleTrendMetric,
  readTrendMetricVisibilityFromSearchParams
} from "../pages/trends/model/trendMetricVisibility";

describe("trendMetricVisibility query params", () => {
  it("defaults all metrics to visible when query parameters are absent", () => {
    const currentSearchParams = new URLSearchParams();

    const trendMetricVisibility = readTrendMetricVisibilityFromSearchParams(currentSearchParams);

    expect(trendMetricVisibility).toEqual({
      battery: true,
      temperature: true,
      humidity: true
    });
    expect(hasAtLeastOneVisibleTrendMetric(trendMetricVisibility)).toBe(true);
  });

  it("reads hidden metrics from explicit zero query parameter values", () => {
    const currentSearchParams = new URLSearchParams("b=0&t=0");

    const trendMetricVisibility = readTrendMetricVisibilityFromSearchParams(currentSearchParams);

    expect(trendMetricVisibility).toEqual({
      battery: false,
      temperature: false,
      humidity: true
    });
  });

  it("writes hidden state with zero and removes key when shown again", () => {
    const currentSearchParams = new URLSearchParams("view=compact");

    const hiddenBatterySearchParams = buildSearchParamsWithUpdatedTrendMetricVisibility(
      currentSearchParams,
      "battery",
      false
    );

    expect(hiddenBatterySearchParams.toString()).toBe("view=compact&b=0");

    const visibleBatterySearchParams = buildSearchParamsWithUpdatedTrendMetricVisibility(
      hiddenBatterySearchParams,
      "battery",
      true
    );

    expect(visibleBatterySearchParams.toString()).toBe("view=compact");
  });

  it("reports no visible metric when all metrics are hidden", () => {
    const currentSearchParams = new URLSearchParams("b=0&t=0&h=0");

    const trendMetricVisibility = readTrendMetricVisibilityFromSearchParams(currentSearchParams);

    expect(hasAtLeastOneVisibleTrendMetric(trendMetricVisibility)).toBe(false);
  });
});
