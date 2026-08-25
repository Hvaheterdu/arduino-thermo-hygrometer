import type { BatteryDto } from "../../types";
import { apiClient } from "./client";
import { assertNoContent, assertResponse } from "./request";

interface BatteryQuery {
  registeredAt: string;
  dateOnly: boolean;
  signal?: AbortSignal;
}

export const getBatteriesByDateOrTimestamp = async ({
  registeredAt,
  dateOnly,
  signal
}: BatteryQuery): Promise<BatteryDto[]> => {
  const result = await apiClient.GET("/api/v1/batteries", {
    params: { query: { dateOnly, registeredAt } },
    ...(signal === undefined ? {} : { signal })
  });

  return assertResponse(result);
};

export const createBattery = async (body: BatteryDto, signal?: AbortSignal): Promise<BatteryDto> => {
  const result = await apiClient.POST("/api/v1/batteries", {
    body,
    ...(signal === undefined ? {} : { signal })
  });

  return assertResponse(result);
};

export const deleteBatteriesByDateOrTimestamp = async ({
  registeredAt,
  dateOnly,
  signal
}: BatteryQuery): Promise<void> => {
  const result = await apiClient.DELETE("/api/v1/batteries", {
    params: { query: { dateOnly, registeredAt } },
    ...(signal === undefined ? {} : { signal })
  });

  assertNoContent(result);
};
