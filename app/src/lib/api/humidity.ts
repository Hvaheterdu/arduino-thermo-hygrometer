import type { HumidityDto } from "@/types";
import { apiClient } from "@/lib/api/client";
import { assertNoContent, assertResponse } from "@/lib/api/request";

type HumidityQuery = {
  registeredAt: string;
  dateOnly: boolean;
  signal?: AbortSignal;
};

export const getHumiditiesByDateOrTimestamp = async ({
                                                       registeredAt,
                                                       dateOnly,
                                                       signal
                                                     }: HumidityQuery): Promise<HumidityDto[]> => {
  const result = await apiClient.GET("/api/v1/humidities", {
    params: { query: { registeredAt, dateOnly } },
    ...(signal !== undefined ? { signal } : {})
  });

  return assertResponse(result);
};

export const createHumidity = async (
  body: HumidityDto,
  signal?: AbortSignal
): Promise<HumidityDto> => {
  const result = await apiClient.POST("/api/v1/humidities", {
    body,
    ...(signal !== undefined ? { signal } : {})
  });

  return assertResponse(result);
};

export const deleteHumiditiesByDateOrTimestamp = async ({
                                                          registeredAt,
                                                          dateOnly,
                                                          signal
                                                        }: HumidityQuery): Promise<void> => {
  const result = await apiClient.DELETE("/api/v1/humidities", {
    params: { query: { registeredAt, dateOnly } },
    ...(signal !== undefined ? { signal } : {})
  });

  assertNoContent(result);
};