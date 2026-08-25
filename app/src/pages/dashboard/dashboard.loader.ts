import type { LoaderFunctionArgs } from "react-router";

import {
  dateToRegisteredAt,
  getBatteriesByDateOrTimestamp,
  getHumiditiesByDateOrTimestamp,
  getTemperaturesByDateOrTimestamp,
  getToday,
  isValidDate
} from "../../lib";
import type { BatteryDto, HumidityDto, TemperatureDto } from "../../types";

interface DashboardLoaderData {
  date: string;
  registeredAt: string;
  battery: BatteryDto[];
  humidity: HumidityDto[];
  temperature: TemperatureDto[];
}

const dashboardLoader = async ({ request }: LoaderFunctionArgs): Promise<DashboardLoaderData> => {
  const url = new URL(request.url);
  const requestedDate = url.searchParams.get("date") ?? getToday();
  const date = isValidDate(requestedDate) ? requestedDate : getToday();
  const registeredAt = dateToRegisteredAt(date);

  const [battery, humidity, temperature] = await Promise.all([
    getBatteriesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    }),
    getHumiditiesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    }),
    getTemperaturesByDateOrTimestamp({
      dateOnly: true,
      registeredAt,
      signal: request.signal
    })
  ]);

  return {
    battery,
    date,
    humidity,
    registeredAt,
    temperature
  };
};

export { dashboardLoader };
