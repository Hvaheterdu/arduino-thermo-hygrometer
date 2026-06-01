import type { JSX } from "react";
import { formatDecimal, formatRegisteredAt } from "../model/formatters";
import type { BatteryReading, HumidityReading, TemperatureReading } from "../model/readingTypes";

interface ReadingsTableProps {
  readonly batteryReadings: readonly BatteryReading[];
  readonly temperatureReadings: readonly TemperatureReading[];
  readonly humidityReadings: readonly HumidityReading[];
}

const asCell = (value: number | undefined, unit: string, precision = 2): string => {
  if (value === undefined) {
    return "-";
  }

  return `${formatDecimal(value, precision)} ${unit}`;
};

export const ReadingsTable = ({
  batteryReadings,
  temperatureReadings,
  humidityReadings
}: ReadingsTableProps): JSX.Element => {
  const rowCount = Math.max(batteryReadings.length, temperatureReadings.length, humidityReadings.length);

  if (rowCount === 0) {
    return (
      <section id="historical-results" className="panel panel-table" aria-live="polite" tabIndex={-1}>
        <h2>Results</h2>
        <p className="empty-state">No readings matched your filters.</p>
      </section>
    );
  }

  return (
    <section id="historical-results" className="panel panel-table" aria-live="polite" tabIndex={-1}>
      <h2>Results</h2>
      <div className="table-scroll">
        <table>
          <caption className="screen-reader-only">Historical battery, temperature, and humidity readings.</caption>
          <thead>
            <tr>
              <th scope="col">Timestamp</th>
              <th scope="col">Battery</th>
              <th scope="col">Temperature</th>
              <th scope="col">Humidity</th>
            </tr>
          </thead>
          <tbody>
            {Array.from({ length: rowCount }, (_, index) => {
              const battery = batteryReadings[index];
              const temperature = temperatureReadings[index];
              const humidity = humidityReadings[index];

              return (
                <tr
                  key={`${battery?.id ?? "battery"}-${temperature?.id ?? "temperature"}-${humidity?.id ?? "humidity"}-${String(index)}`}
                >
                  <td>
                    {formatRegisteredAt(
                      battery?.registeredAt ??
                        temperature?.registeredAt ??
                        humidity?.registeredAt ??
                        new Date().toISOString()
                    )}
                  </td>
                  <td>{asCell(battery?.batteryStatus, "%", 0)}</td>
                  <td>{asCell(temperature?.temp, "°C")}</td>
                  <td>{asCell(humidity?.airHumidity, "%")}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </section>
  );
};
