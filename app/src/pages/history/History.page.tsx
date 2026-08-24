import type { ReactElement } from "react";
import { useState } from "react";
import { Button, chakra, Heading, HStack, Input, NativeSelect, Stack, Text } from "@chakra-ui/react";
import { Form, useLoaderData, useNavigation, useRevalidator } from "react-router";
import { useSWRConfig } from "swr";

import { ApiErrorMessage, EmptyState, LoadingState, MeasurementTable } from "@/components";
import { useBatteryReadings, useDeleteReading, useHumidityReadings, useTemperatureReadings } from "@/hooks";
import { formatRegisteredAt, getBatteryKey, getHumidityKey, getTemperatureKey } from "@/lib";
import { historyLoader } from "@/pages/history/history.loader";

type HistoryRow = {
  registeredAt: string;
  value: string;
};

export const HistoryPage = (): ReactElement => {
  const loaderData = useLoaderData<typeof historyLoader>();
  const navigation = useNavigation();
  const revalidator = useRevalidator();
  const [deleted, setDeleted] = useState(false);
  const { mutate } = useSWRConfig();
  const deleteReading = useDeleteReading();

  const battery = useBatteryReadings(
    loaderData.registeredAt,
    true,
    loaderData.resource === "battery"
  );

  const humidity = useHumidityReadings(
    loaderData.registeredAt,
    true,
    loaderData.resource === "humidity"
  );

  const temperature = useTemperatureReadings(
    loaderData.registeredAt,
    true,
    loaderData.resource === "temperature"
  );

  const error =
    loaderData.resource === "battery"
      ? battery.error
      : loaderData.resource === "humidity"
        ? humidity.error
        : temperature.error;

  const rows: HistoryRow[] =
    loaderData.resource === "battery"
      ? (battery.data ?? []).map((item) => ({
        registeredAt: item.registeredAt,
        value: String(item.batteryStatus)
      }))
      : loaderData.resource === "humidity"
        ? (humidity.data ?? []).map((item) => ({
          registeredAt: item.registeredAt,
          value: item.airHumidity.toFixed(1)
        }))
        : (temperature.data ?? []).map((item) => ({
          registeredAt: item.registeredAt,
          value: item.temp.toFixed(1)
        }));

  const reversedRows: HistoryRow[] = [...rows].reverse();

  const latestRegisteredAt: string =
    loaderData.resource === "battery"
      ? battery.data?.at(-1)?.registeredAt ??
      loaderData.registeredAt
      : loaderData.resource === "humidity"
        ? humidity.data?.at(-1)?.registeredAt ??
        loaderData.registeredAt
        : temperature.data?.at(-1)?.registeredAt ??
        loaderData.registeredAt;

  const handleDelete = async (): Promise<void> => {
    setDeleted(false);

    try {
      await deleteReading.trigger({
        resource: loaderData.resource,
        registeredAt: loaderData.registeredAt,
        dateOnly: true
      });

      setDeleted(true);

      const key =
        loaderData.resource === "battery"
          ? getBatteryKey(loaderData.registeredAt, true)
          : loaderData.resource === "humidity"
            ? getHumidityKey(loaderData.registeredAt, true)
            : getTemperatureKey(loaderData.registeredAt, true);

      await mutate(key, [], { revalidate: false });
      await revalidator.revalidate();
    } catch {
      // Mutation errors are exposed through the mutation hook.
    }
  };

  if (navigation.state === "loading") {
    return <LoadingState label="Loading history..." />;
  }

  return (
    <Stack gap="7">
      <Stack gap="2">
        <Heading size="2xl">Measurement history</Heading>

        <Text color="fg.muted">
          Review and remove readings for a selected day.
        </Text>
      </Stack>

      <Form method="get">
        <HStack align="end" gap="3" wrap="wrap">
          <Stack gap="1">
            <chakra.label
              htmlFor="resource"
              fontSize="sm"
              fontWeight="medium"
            >
              Sensor
            </chakra.label>

            <NativeSelect.Root>
              <NativeSelect.Field
                id="resource"
                name="resource"
                defaultValue={loaderData.resource}
              >
                <option value="temperature">Temperature</option>
                <option value="humidity">Humidity</option>
                <option value="battery">Battery</option>
              </NativeSelect.Field>

              <NativeSelect.Indicator />
            </NativeSelect.Root>
          </Stack>

          <Stack gap="1">
            <chakra.label
              htmlFor="date"
              fontSize="sm"
              fontWeight="medium"
            >
              Date
            </chakra.label>

            <Input
              id="date"
              name="date"
              type="date"
              defaultValue={loaderData.date}
            />
          </Stack>

          <Button type="submit">Load history</Button>
        </HStack>
      </Form>

      {error ? <ApiErrorMessage error={error} /> : null}

      {rows.length === 0 && !error ? (
        <EmptyState
          title="No readings found"
          description={`There are no ${loaderData.resource} readings recorded for ${loaderData.date}.`}
        />
      ) : null}

      {rows.length > 0 ? (
        <Stack gap="4">
          <Text color="fg.muted">
            {rows.length} reading(s), newest first.
          </Text>

          <MeasurementTable
            resource={loaderData.resource}
            rows={reversedRows}
          />

          <Button
            colorPalette="red"
            variant="outline"
            alignSelf="flex-start"
            loading={deleteReading.isMutating}
            onClick={handleDelete}
          >
            Delete all for this day
          </Button>

          {deleteReading.error ? (
            <ApiErrorMessage error={deleteReading.error} />
          ) : null}

          {deleted ? (
            <Text color="fg.success">Readings deleted.</Text>
          ) : null}

          <Text color="fg.muted" fontSize="sm">
            Latest data shown from{" "}
            {formatRegisteredAt(latestRegisteredAt)}.
          </Text>
        </Stack>
      ) : null}
    </Stack>
  );
};