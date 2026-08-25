import type { LoaderFunctionArgs } from "react-router";

import {
  dateToRegisteredAt,
  getBatteriesByDateOrTimestamp,
  getHumiditiesByDateOrTimestamp,
  getTemperaturesByDateOrTimestamp,
  getToday,
  isSensorResource,
  isValidDate
} from "../../lib";
import type { BatteryDto, HumidityDto, SensorResource, TemperatureDto } from "../../types";

type HistoryLoaderData =
  | {
      resource: "battery";
      date: string;
      registeredAt: string;
      readings: BatteryDto[];
    }
  | {
      resource: "humidity";
      date: string;
      registeredAt: string;
      readings: HumidityDto[];
    }
  | {
      resource: "temperature";
      date: string;
      registeredAt: string;
      readings: TemperatureDto[];
    };

const historyLoader = async ({ request }: LoaderFunctionArgs): Promise<HistoryLoaderData> => {
  const url = new URL(request.url),
    resourceParam = url.searchParams.get("resource"),
    resource: SensorResource = isSensorResource(resourceParam) ? resourceParam : "temperature",
    requestedDate = url.searchParams.get("date") ?? getToday(),
    date = isValidDate(requestedDate) ? requestedDate : getToday(),
    registeredAt = dateToRegisteredAt(date);

  if (resource === "battery") {
    const readings = await getBatteriesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    });

    return { date, readings, registeredAt, resource };
  }

  if (resource === "humidity") {
    const readings = await getHumiditiesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    });

    return { date, readings, registeredAt, resource };
  }

  const readings = await getTemperaturesByDateOrTimestamp({
    dateOnly: true,
    registeredAt,
    signal: request.signal
  });

  return { date, readings, registeredAt, resource };
};

export { historyLoader };
