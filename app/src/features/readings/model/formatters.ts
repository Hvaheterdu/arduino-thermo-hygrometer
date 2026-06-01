export const formatRegisteredAt = (value: string): string => {
  return new Intl.DateTimeFormat("en-GB", {
    dateStyle: "medium",
    timeStyle: "short"
  }).format(new Date(value));
};

export const formatDecimal = (value: number, fractionDigits = 2): string => {
  return new Intl.NumberFormat("en-GB", {
    minimumFractionDigits: fractionDigits,
    maximumFractionDigits: fractionDigits
  }).format(value);
};
