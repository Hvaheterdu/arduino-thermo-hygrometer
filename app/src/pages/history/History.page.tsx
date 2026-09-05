import { Button, chakra, Heading, HStack, Input, NativeSelect, Stack, Text } from "@chakra-ui/react";
import { MeasurementTable } from "@components/data-table/MeasurementTable.component";
import { ApiErrorMessage } from "@components/feedback/ApiErrorMessage.component";
import { EmptyState } from "@components/feedback/EmptyState.component";
import { LoadingState } from "@components/feedback/LoadingState.component";
import { useDeleteReading } from "@hooks/useDeleteReading";
import { formatRegisteredAt } from "@lib/date/date";
import type { historyLoader } from "@pages/history/history.loader";
import type { ReactElement } from "react";
import { useState } from "react";
import { Form, useLoaderData, useNavigation, useRevalidator } from "react-router";

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

  const [defaultStart, defaultEnd] =
    loaderData.date && loaderData.date.includes("..")
      ? loaderData.date.split("..")
      : [loaderData.date, loaderData.date];

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

    const url = new URL(window.location.href);
    const start = url.searchParams.get("startDate");
    const end = url.searchParams.get("endDate");

    if (start && end && start !== end) {
      const startDate = new Date(start);
      const endDate = new Date(end);
      const deletions: Promise<unknown>[] = [];

      for (let date = new Date(startDate); date <= endDate; date.setDate(date.getDate() + 1)) {
        const dateStr = [
          date.getFullYear(),
          String(date.getMonth() + 1).padStart(2, "0"),
          String(date.getDate()).padStart(2, "0")
        ].join("-");

        deletions.push(trigger({ dateOnly: true, registeredAt: `${dateStr}T00:00:00`, resource: loaderData.resource }));
      }

      await Promise.all(deletions);
    } else {
      await trigger({ dateOnly: true, registeredAt: loaderData.registeredAt, resource: loaderData.resource });
    }

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
        <Text color="fg.muted">Review and remove readings for a selected day or date range.</Text>
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
            <chakra.label htmlFor="startDate" fontSize="sm" fontWeight="medium">
              Start date
            </chakra.label>

            <Input id="startDate" name="startDate" type="date" defaultValue={defaultStart} />
          </Stack>

          <Stack gap="1">
            <chakra.label htmlFor="endDate" fontSize="sm" fontWeight="medium">
              End date
            </chakra.label>

            <Input id="endDate" name="endDate" type="date" defaultValue={defaultEnd} />
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
            {defaultStart === defaultEnd ? "Delete all for this day" : "Delete all for this selection"}
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
