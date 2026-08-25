import type { LoaderFunctionArgs } from "react-router";

import { getBatteriesByDateOrTimestamp } from "@/lib/api/battery";
import { getHumiditiesByDateOrTimestamp } from "@/lib/api/humidity";
import { getTemperaturesByDateOrTimestamp } from "@/lib/api/temperature";
import { dateToRegisteredAt, getToday, isValidDate } from "@/lib/date/date";
import { isSensorResource } from "@/lib/sensor/sensorConfig";
import type { BatteryDto, HumidityDto, TemperatureDto } from "@/types/domain";
import type { SensorResource } from "@/types/sensor";

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
  const url = new URL(request.url);

  const resourceParam = url.searchParams.get("resource");
  const resource: SensorResource = isSensorResource(resourceParam) ? resourceParam : "temperature";

  const requestedDate = url.searchParams.get("date") ?? getToday();
  const date = isValidDate(requestedDate) ? requestedDate : getToday();
  const registeredAt = dateToRegisteredAt(date);

  if (resource === "battery") {
    const readings = await getBatteriesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    });

    return { resource, date, registeredAt, readings };
  }

  if (resource === "humidity") {
    const readings = await getHumiditiesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    });

    return { resource, date, registeredAt, readings };
  }

  const readings = await getTemperaturesByDateOrTimestamp({
    dateOnly: true,
    registeredAt,
    signal: request.signal
  });

  return { resource, date, registeredAt, readings };
};

export { historyLoader };
