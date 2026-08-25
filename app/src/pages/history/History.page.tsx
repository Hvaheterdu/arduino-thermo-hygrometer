import { Button, chakra, Heading, HStack, Input, NativeSelect, Stack, Text } from "@chakra-ui/react";
import type { ReactElement } from "react";
import { useState } from "react";
import { Form, useLoaderData, useNavigation, useRevalidator } from "react-router";

import { MeasurementTable } from "@/components/data-table/MeasurementTable.component";
import { ApiErrorMessage } from "@/components/feedback/ApiErrorMessage.component";
import { EmptyState } from "@/components/feedback/EmptyState.component";
import { LoadingState } from "@/components/feedback/LoadingState.component";
import { useDeleteReading } from "@/hooks/useDeleteReading";
import { formatRegisteredAt } from "@/lib/date/date";

import type { historyLoader } from "./history.loader";

type HistoryRow = {
  registeredAt: string;
  value: string;
};

export const HistoryPage = (): ReactElement => {
  const loaderData = useLoaderData<typeof historyLoader>();
  const navigation = useNavigation();
  const revalidator = useRevalidator();
  const { trigger, error, isMutating } = useDeleteReading();
  const [deleted, setDeleted] = useState(false);

  const rows: HistoryRow[] =
    loaderData.resource === "battery"
      ? loaderData.readings.map((item) => ({
          registeredAt: item.registeredAt,
          value: String(item.batteryStatus)
        }))
      : loaderData.resource === "humidity"
        ? loaderData.readings.map((item) => ({
            registeredAt: item.registeredAt,
            value: item.airHumidity.toFixed(1)
          }))
        : loaderData.readings.map((item) => ({
            registeredAt: item.registeredAt,
            value: item.temp.toFixed(1)
          }));

  const handleDelete = async (): Promise<void> => {
    setDeleted(false);

    await trigger({
      dateOnly: true,
      registeredAt: loaderData.registeredAt,
      resource: loaderData.resource
    });

    setDeleted(true);
    await revalidator.revalidate();
  };

  if (navigation.state === "loading") {
    return <LoadingState label="Loading history..." />;
  }

  return (
    <Stack gap="7">
      <Stack gap="2">
        <Heading size="2xl">Measurement history</Heading>
        <Text color="fg.muted">Review and remove readings for a selected day.</Text>
      </Stack>

      <Form method="get">
        <HStack align="end" gap="3" wrap="wrap">
          <Stack gap="1">
            <chakra.label htmlFor="resource" fontSize="sm" fontWeight="medium">
              Sensor
            </chakra.label>

            <NativeSelect.Root>
              <NativeSelect.Field id="resource" name="resource" defaultValue={loaderData.resource}>
                <option value="temperature">Temperature</option>
                <option value="humidity">Humidity</option>
                <option value="battery">Battery</option>
              </NativeSelect.Field>
              <NativeSelect.Indicator />
            </NativeSelect.Root>
          </Stack>

          <Stack gap="1">
            <chakra.label htmlFor="date" fontSize="sm" fontWeight="medium">
              Date
            </chakra.label>

            <Input id="date" name="date" type="date" defaultValue={loaderData.date} />
          </Stack>

          <Button type="submit">Load history</Button>
        </HStack>
      </Form>

      {rows.length === 0 ? (
        <EmptyState
          title="No readings found"
          description={`There are no ${loaderData.resource} readings recorded for ${loaderData.date}.`}
        />
      ) : (
        <Stack gap="4">
          <Text color="fg.muted">{rows.length} reading(s), newest first.</Text>

          <MeasurementTable resource={loaderData.resource} rows={[...rows].reverse()} />

          <Button
            colorPalette="red"
            variant="outline"
            alignSelf="flex-start"
            loading={isMutating}
            disabled={isMutating}
            onClick={handleDelete}
          >
            Delete all for this day
          </Button>

          {error ? <ApiErrorMessage error={error} /> : null}

          {deleted ? <Text color="fg.success">Readings deleted.</Text> : null}

          <Text color="fg.muted" fontSize="sm">
            Latest data shown from{" "}
            {formatRegisteredAt(loaderData.readings.at(-1)?.registeredAt ?? loaderData.registeredAt)}
          </Text>
        </Stack>
      )}
    </Stack>
  );
};
