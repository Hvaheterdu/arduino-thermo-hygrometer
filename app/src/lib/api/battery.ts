import type { BatteryDto } from "@/types";
import { apiClient } from "@/lib/api/client";
import { assertNoContent, assertResponse } from "@/lib/api/request";

type BatteryQuery = {
  registeredAt: string;
  dateOnly: boolean;
  signal?: AbortSignal;
};

export const getBatteriesByDateOrTimestamp = async ({
                                                      registeredAt,
                                                      dateOnly,
                                                      signal
                                                    }: BatteryQuery): Promise<BatteryDto[]> => {
  const result = await apiClient.GET("/api/v1/batteries", {
    params: { query: { registeredAt, dateOnly } },
    ...(signal !== undefined ? { signal } : {})
  });

  return assertResponse(result);
};

export const createBattery = async (
  body: BatteryDto,
  signal?: AbortSignal
): Promise<BatteryDto> => {
  const result = await apiClient.POST("/api/v1/batteries", {
    body,
    ...(signal !== undefined ? { signal } : {})
  });

  return assertResponse(result);
};

export const deleteBatteriesByDateOrTimestamp = async ({
                                                         registeredAt,
                                                         dateOnly,
                                                         signal
                                                       }: BatteryQuery): Promise<void> => {
  const result = await apiClient.DELETE("/api/v1/batteries", {
    params: { query: { registeredAt, dateOnly } },
    ...(signal !== undefined ? { signal } : {})
  });

  assertNoContent(result);
};
