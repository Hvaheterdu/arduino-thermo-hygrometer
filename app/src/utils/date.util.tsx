import dayjs from "dayjs";
import timezone from "dayjs/plugin/timezone";
import utc from "dayjs/plugin/utc";

dayjs.extend(utc);
dayjs.extend(timezone);

const DATE_TIME_LOCAL_FORMAT = "YYYY-MM-DDTHH:mm";
const DISPLAY_DATE_FORMAT = "DD MMM YYYY";
const DISPLAY_TIMESTAMP_FORMAT = "DD MMM YYYY, HH:mm";

export const formatRegisteredAt = (isoTimestamp: string, dateOnly: boolean = false): string => {
    if (!dayjs(isoTimestamp).isValid()) {
        return isoTimestamp;
    }

    if (dateOnly) {
        return dayjs.utc(isoTimestamp).local().format(DISPLAY_DATE_FORMAT);
    }

    return dayjs.utc(isoTimestamp).local().format(DISPLAY_TIMESTAMP_FORMAT);
};

export const toDateTimeLocalInputValue = (date: Date): string => dayjs(date).format(DATE_TIME_LOCAL_FORMAT);

export const toRegisteredAtQueryValue = (selectedRegisteredAt: string): string => dayjs(selectedRegisteredAt).toISOString();
