import { getBatteriesByDateOrTimestamp } from "@lib/api/battery";
import { getHumiditiesByDateOrTimestamp } from "@lib/api/humidity";
import { getTemperaturesByDateOrTimestamp } from "@lib/api/temperature";
import { dateToRegisteredAt, getToday, isValidDate } from "@lib/date/date";
import { isSensorResource } from "@lib/sensor/sensorConfig";
import type { LoaderFunctionArgs } from "react-router";

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

export const historyLoader = async ({ request }: LoaderFunctionArgs): Promise<HistoryLoaderData> => {
  const url = new URL(request.url);

  const resourceParam = url.searchParams.get("resource");
  const resource: SensorResource = isSensorResource(resourceParam) ? resourceParam : "temperature";

  const requestedDate = url.searchParams.get("date");
  const startDateParam = url.searchParams.get("startDate");
  const endDateParam = url.searchParams.get("endDate");

  const isRangeSearch = Boolean(
    startDateParam && endDateParam && isValidDate(startDateParam) && isValidDate(endDateParam)
  );

  if (isRangeSearch) {
    const start = startDateParam as string;
    const end = endDateParam as string;

    const dates: string[] = [];
    const startDate = new Date(start);
    const endDate = new Date(end);
    for (let date = new Date(startDate); date <= endDate; date.setDate(date.getDate() + 1)) {
      dates.push(
        [
          date.getFullYear(),
          String(date.getMonth() + 1).padStart(2, "0"),
          String(date.getDate()).padStart(2, "0")
        ].join("-")
      );
    }

    const fetchForDate = async (dateStr: string) => {
      const registeredAt = dateToRegisteredAt(dateStr);

      if (resource === "battery") {
        return getBatteriesByDateOrTimestamp({ dateOnly: true, registeredAt, signal: request.signal });
      }

      if (resource === "humidity") {
        return getHumiditiesByDateOrTimestamp({ dateOnly: true, registeredAt, signal: request.signal });
      }

      return getTemperaturesByDateOrTimestamp({ dateOnly: true, registeredAt, signal: request.signal });
    };

    const allResults = await Promise.all(dates.map((d) => fetchForDate(d)));
    const flat = allResults.flat();
    let readings: BatteryDto[] | HumidityDto[] | TemperatureDto[];
    if (resource === "battery") {
      readings = flat as BatteryDto[];
    } else if (resource === "humidity") {
      readings = flat as HumidityDto[];
    } else {
      readings = flat as TemperatureDto[];
    }
    readings.sort((a, b) => Date.parse(a.registeredAt) - Date.parse(b.registeredAt));

    const date = `${start}..${end}`;
    const registeredAt = dateToRegisteredAt(end);
    if (resource === "battery") {
      return { resource: "battery", date, registeredAt, readings: readings as BatteryDto[] } as HistoryLoaderData;
    }

    if (resource === "humidity") {
      return { resource: "humidity", date, registeredAt, readings: readings as HumidityDto[] } as HistoryLoaderData;
    }

    return { resource: "temperature", date, registeredAt, readings: readings as TemperatureDto[] } as HistoryLoaderData;
  }

  const date = requestedDate ?? getToday();
  const validDate = isValidDate(date) ? date : getToday();
  const registeredAt = dateToRegisteredAt(validDate);

  if (resource === "battery") {
    const readings = await getBatteriesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    });

    return { resource, date: validDate, registeredAt, readings };
  }

  if (resource === "humidity") {
    const readings = await getHumiditiesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    });

    return { resource, date: validDate, registeredAt, readings };
  }

  const readings = await getTemperaturesByDateOrTimestamp({
    dateOnly: true,
    registeredAt,
    signal: request.signal
  });

  return { resource, date: validDate, registeredAt, readings };
};
