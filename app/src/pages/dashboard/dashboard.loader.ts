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
import type { BatteryDto, HumidityDto, TemperatureDto } from "@/types";

type DashboardLoaderData = {
  date: string;
  registeredAt: string;
  battery: BatteryDto[];
  humidity: HumidityDto[];
  temperature: TemperatureDto[];
};

const dashboardLoader = async ({
                                 request
                               }: LoaderFunctionArgs): Promise<DashboardLoaderData> => {
  const url = new URL(request.url);
  const requestedDate = url.searchParams.get("date") ?? getToday();
  const date = isValidDate(requestedDate) ? requestedDate : getToday();
  const registeredAt = dateToRegisteredAt(date);

  const [battery, humidity, temperature] = await Promise.all([
    getBatteriesByDateOrTimestamp({
      registeredAt,
      dateOnly: true,
      signal: request.signal
    }),
    getHumiditiesByDateOrTimestamp({
      registeredAt,
      dateOnly: true,
      signal: request.signal
    }),
    getTemperaturesByDateOrTimestamp({
      registeredAt,
      dateOnly: true,
      signal: request.signal
    })
  ]);

  await Promise.all([
    mutate(getBatteryKey(registeredAt, true), battery, {
      revalidate: false
    }),
    mutate(getHumidityKey(registeredAt, true), humidity, {
      revalidate: false
    }),
    mutate(getTemperatureKey(registeredAt, true), temperature, {
      revalidate: false
    })
  ]);

  return {
    date,
    registeredAt,
    battery,
    humidity,
    temperature
  };
};

export { dashboardLoader };