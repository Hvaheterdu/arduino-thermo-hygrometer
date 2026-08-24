const DATE_PATTERN = /^\d{4}-\d{2}-\d{2}$/;
const LOCAL_DATE_TIME_PATTERN = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/;

type RegisteredAtItem = {
  registeredAt: string;
};

export const getToday = (): string => {
  const now: Date = new Date();
  const year: number = now.getFullYear();
  const month: string = String(now.getMonth() + 1).padStart(2, "0");
  const day: string = String(now.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

export const isValidDate = (value: string): boolean => {
  if (!DATE_PATTERN.test(value)) return false;
  const date: Date = new Date(`${value}T00:00:00`);
  return !Number.isNaN(date.getTime()) && date.toISOString().slice(0, 10) === value;
};

export const isValidDateTime = (value: string): boolean => {
  if (!LOCAL_DATE_TIME_PATTERN.test(value)) return false;
  return !Number.isNaN(new Date(value).getTime());
};

export const dateToRegisteredAt = (date: string): string => `${date}T00:00:00`;

export const formatRegisteredAt = (value: string): string => {
  const date: Date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat(undefined, {
    dateStyle: "medium",
    timeStyle: "short"
  }).format(date);
};

export const latestByRegisteredAt = <T extends RegisteredAtItem>(items: T[]): T | undefined =>
  items.reduce<T | undefined>((latest: T | undefined, current: T): T => {
    if (!latest) return current;
    return new Date(current.registeredAt).getTime() > new Date(latest.registeredAt).getTime() ? current : latest;
  }, undefined);
