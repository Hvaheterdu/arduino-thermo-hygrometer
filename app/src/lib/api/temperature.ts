import type { TemperatureDto } from "@/types";
import { apiClient } from "@/lib/api/client";
import { assertNoContent, assertResponse } from "@/lib/api/request";

type TemperatureQuery = {
  registeredAt: string;
  dateOnly: boolean;
  signal?: AbortSignal;
};

export const getTemperaturesByDateOrTimestamp = async ({
                                                         registeredAt,
                                                         dateOnly,
                                                         signal
                                                       }: TemperatureQuery): Promise<TemperatureDto[]> => {
  const result = await apiClient.GET("/api/v1/temperatures", {
    params: { query: { registeredAt, dateOnly } },
    ...(signal !== undefined ? { signal } : {})
  });

  return assertResponse(result);
};

export const createTemperature = async (
  body: TemperatureDto,
  signal?: AbortSignal
): Promise<TemperatureDto> => {
  const result = await apiClient.POST("/api/v1/temperatures", {
    body,
    ...(signal !== undefined ? { signal } : {})
  });

  return assertResponse(result);
};

export const deleteTemperaturesByDateOrTimestamp = async ({
                                                            registeredAt,
                                                            dateOnly,
                                                            signal
                                                          }: TemperatureQuery): Promise<void> => {
  const result = await apiClient.DELETE("/api/v1/temperatures", {
    params: { query: { registeredAt, dateOnly } },
    ...(signal !== undefined ? { signal } : {})
  });

  assertNoContent(result);
};