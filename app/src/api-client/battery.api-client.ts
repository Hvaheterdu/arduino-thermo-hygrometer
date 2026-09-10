import { httpClient } from "@/api-client/httpClient";
import type { BatteryDto, MeasurementQuery } from "@/types/measurement.types";
import { toApiError } from "@/utils/problemDetails.util";

const PATH = "/api/v1/batteries";

export const listBatteries = async (query: MeasurementQuery): Promise<BatteryDto[]> => {
  const { data, error } = await httpClient.GET(PATH, { params: { query } });
  if (error) {
    throw toApiError(error);
  }

  return data;
};

export const createBattery = async (newReading: BatteryDto): Promise<BatteryDto> => {
  const { data, error } = await httpClient.POST(PATH, { body: newReading });
  if (error) {
    throw toApiError(error);
  }

  return data;
};

export const deleteBatteries = async (query: MeasurementQuery): Promise<void> => {
  const { error } = await httpClient.DELETE(PATH, { params: { query } });
  if (error) {
    throw toApiError(error);
  }
};
