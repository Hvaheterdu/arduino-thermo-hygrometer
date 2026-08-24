export const getBatteryKey = (registeredAt: string, dateOnly: boolean): readonly ["batteries", string, boolean] => [
  "batteries",
  registeredAt,
  dateOnly
];

export const getHumidityKey = (registeredAt: string, dateOnly: boolean): readonly ["humidities", string, boolean] => [
  "humidities",
  registeredAt,
  dateOnly
];

export const getTemperatureKey = (registeredAt: string, dateOnly: boolean): readonly ["temperatures", string, boolean] => [
  "temperatures",
  registeredAt,
  dateOnly
];
