import type { ReadingsApi } from "../../../shared/api/readingsApi";
import type {
  BatteryReading,
  HistoricalSearchRequest,
  HumidityReading,
  TemperatureReading
} from "../model/readingTypes";
import { OpenApiReadingsApi } from "./openApiReadingsApi";

export interface ReadingsRepository {
  getBatteryHistory(request: HistoricalSearchRequest): Promise<BatteryReading[]>;
  getTemperatureHistory(request: HistoricalSearchRequest): Promise<TemperatureReading[]>;
  getHumidityHistory(request: HistoricalSearchRequest): Promise<HumidityReading[]>;
}

export class ApiReadingsRepository implements ReadingsRepository {
  constructor(private readonly readingsApi: ReadingsApi) {}

  async getBatteryHistory(request: HistoricalSearchRequest): Promise<BatteryReading[]> {
    const readings = await this.readingsApi.getBatteries({
      registeredAt: request.registeredAt,
      dateOnly: request.dateOnly
    });

    return readings.map((reading) => ({
      ...(reading.id !== undefined ? { id: reading.id } : {}),
      registeredAt: reading.registeredAt,
      batteryStatus: reading.batteryStatus
    }));
  }

  async getTemperatureHistory(request: HistoricalSearchRequest): Promise<TemperatureReading[]> {
    const readings = await this.readingsApi.getTemperatures({
      registeredAt: request.registeredAt,
      dateOnly: request.dateOnly
    });

    return readings.map((reading) => ({
      ...(reading.id !== undefined ? { id: reading.id } : {}),
      registeredAt: reading.registeredAt,
      temp: reading.temp
    }));
  }

  async getHumidityHistory(request: HistoricalSearchRequest): Promise<HumidityReading[]> {
    const readings = await this.readingsApi.getHumidities({
      registeredAt: request.registeredAt,
      dateOnly: request.dateOnly
    });

    return readings.map((reading) => ({
      ...(reading.id !== undefined ? { id: reading.id } : {}),
      registeredAt: reading.registeredAt,
      airHumidity: reading.airHumidity
    }));
  }
}

export const defaultReadingsRepository: ReadingsRepository = new ApiReadingsRepository(new OpenApiReadingsApi());
