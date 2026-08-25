const DATE_PATTERN = /^(\d{4})-(\d{2})-(\d{2})$/,
  LOCAL_DATE_TIME_PATTERN = /^(\d{4}-\d{2}-\d{2})T(\d{2}):(\d{2})$/;

interface RegisteredAtItem {
  registeredAt: string;
}

export const getToday = (): string => {
  const date = new Date();

  return [
    date.getFullYear(),
    String(date.getMonth() + 1).padStart(2, "0"),
    String(date.getDate()).padStart(2, "0")
  ].join("-");
};

export const isValidDate = (value: string): boolean => {
  const match = DATE_PATTERN.exec(value);

  if (!match) {
    return false;
  }

  const [, yearString, monthString, dayString] = match;

  if (!yearString || !monthString || !dayString) {
    return false;
  }

  const year = Number(yearString),
    month = Number(monthString),
    day = Number(dayString),
    date = new Date(year, month - 1, day);

  return date.getFullYear() === year && date.getMonth() === month - 1 && date.getDate() === day;
};

export const isValidDateTime = (value: string): boolean => {
  const match = LOCAL_DATE_TIME_PATTERN.exec(value);

  if (!match) {
    return false;
  }

  const [, datePart, hoursString, minutesString] = match;

  if (!datePart || !hoursString || !minutesString) {
    return false;
  }

  if (!isValidDate(datePart)) {
    return false;
  }

  const hours = Number(hoursString),
    minutes = Number(minutesString),
    date = new Date(`${datePart}T${hoursString}:${minutesString}`);

  return !Number.isNaN(date.getTime()) && date.getHours() === hours && date.getMinutes() === minutes;
};

export const dateToRegisteredAt = (date: string): string => `${date}T00:00:00`;

export const formatRegisteredAt = (value: string): string => {
  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return new Intl.DateTimeFormat(undefined, {
    dateStyle: "medium",
    timeStyle: "short"
  }).format(date);
};

export const latestByRegisteredAt = <T extends RegisteredAtItem>(items: T[]): T | undefined =>
  items.reduce<T | undefined>((latest, current) => {
    if (!latest) {
      return current;
    }

    return Date.parse(current.registeredAt) > Date.parse(latest.registeredAt) ? current : latest;
  }, undefined);
