import { mutate } from "swr";
import type { LoaderFunctionArgs } from "react-router";

import {
  dateToRegisteredAt,
  getBatteriesByDateOrTimestamp,
  getBatteryKey,
  getHumiditiesByDateOrTimestamp,
  getHumidityKey,
  getTemperatureKey,
  getTemperaturesByDateOrTimestamp,
  getToday,
  isValidDate
} from "@/lib";
import type { BatteryDto, HumidityDto, SensorResource, TemperatureDto } from "@/types";
import { isSensorResource } from "@/types/sensors";

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

const historyLoader = async ({
                               request
                             }: LoaderFunctionArgs): Promise<HistoryLoaderData> => {
  const url = new URL(request.url);
  const resourceParam = url.searchParams.get("resource");
  const resource: SensorResource = isSensorResource(resourceParam)
    ? resourceParam
    : "temperature";
  const requestedDate = url.searchParams.get("date") ?? getToday();
  const date = isValidDate(requestedDate) ? requestedDate : getToday();
  const registeredAt = dateToRegisteredAt(date);

  if (resource === "battery") {
    const readings = await getBatteriesByDateOrTimestamp({
      registeredAt,
      dateOnly: true,
      signal: request.signal
    });

    await mutate(getBatteryKey(registeredAt, true), readings, {
      revalidate: false
    });

    return {
      resource,
      date,
      registeredAt,
      readings
    };
  }

  if (resource === "humidity") {
    const readings = await getHumiditiesByDateOrTimestamp({
      registeredAt,
      dateOnly: true,
      signal: request.signal
    });

    await mutate(getHumidityKey(registeredAt, true), readings, {
      revalidate: false
    });

    return {
      resource,
      date,
      registeredAt,
      readings
    };
  }

  const readings = await getTemperaturesByDateOrTimestamp({
    registeredAt,
    dateOnly: true,
    signal: request.signal
  });

  await mutate(getTemperatureKey(registeredAt, true), readings, {
    revalidate: false
  });

  return {
    resource,
    date,
    registeredAt,
    readings
  };
};

export { historyLoader };