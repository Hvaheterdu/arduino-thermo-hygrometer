import { httpClient } from "@/api-client/httpClient";
import type { BatteryDto, MeasurementQuery } from "@/types/measurement.types";
import { toApiError } from "@/utils/problemDetails.util";

const PATH = "/api/v1/batteries";

export const listBatteries = async (query: MeasurementQuery): Promise<BatteryDto[]> => {
  try {
    const { data, error } = await httpClient.GET(PATH, { params: { query } });
    if (error) {
      throw toApiError(error);
    }
    return data;
  } catch (error) {
    throw toApiError(error);
  }
};

export const createBattery = async (newReading: BatteryDto): Promise<BatteryDto> => {
  try {
    const { data, error } = await httpClient.POST(PATH, { body: newReading });
    if (error) {
      throw toApiError(error);
    }
    return data;
  } catch (error) {
    throw toApiError(error);
  }
};

export const deleteBatteries = async (query: MeasurementQuery): Promise<void> => {
  try {
    const { error } = await httpClient.DELETE(PATH, { params: { query } });
    if (error) {
      throw toApiError(error);
    }
  } catch (error) {
    throw toApiError(error);
  }
};
