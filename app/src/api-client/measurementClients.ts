import { createBattery, deleteBatteries, listBatteries } from "@/api-client/battery.api-client";
import { createHumidity, deleteHumidities, listHumidities } from "@/api-client/humidity.api-client";
import { createTemperature, deleteTemperatures, listTemperatures } from "@/api-client/temperature.api-client";
import type { MeasurementDtoMap, MeasurementQuery, ResourceKind } from "@/types/measurement.types";

export const measurementClients = {
  battery: { list: listBatteries, create: createBattery, remove: deleteBatteries },
  humidity: { list: listHumidities, create: createHumidity, remove: deleteHumidities },
  temperature: { list: listTemperatures, create: createTemperature, remove: deleteTemperatures }
} as const satisfies {
  [Resource in ResourceKind]: {
    list: (query: MeasurementQuery) => Promise<MeasurementDtoMap[Resource][]>;
    create: (newReading: MeasurementDtoMap[Resource]) => Promise<MeasurementDtoMap[Resource]>;
    remove: (query: MeasurementQuery) => Promise<void>;
  };
};
