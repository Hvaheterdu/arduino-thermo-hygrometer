import { Button, Grid, HStack, Heading, Input, Stack, Text, chakra } from "@chakra-ui/react";
import type { ReactElement } from "react";
import { Form, useLoaderData, useNavigation } from "react-router";

import { EmptyState, LoadingState, MetricCard } from "../../components";
import { formatRegisteredAt, latestByRegisteredAt } from "../../lib";
import type { dashboardLoader } from "./dashboard.loader";

const formatValue = (value: number | undefined, suffix: string, fractionDigits = 0): string =>
  value === undefined ? "—" : `${value.toFixed(fractionDigits)}${suffix}`;

export const DashboardPage = (): ReactElement => {
  const loaderData = useLoaderData<typeof dashboardLoader>(),
    navigation = useNavigation(),
    latestBattery = latestByRegisteredAt(loaderData.battery),
    latestHumidity = latestByRegisteredAt(loaderData.humidity),
    latestTemperature = latestByRegisteredAt(loaderData.temperature),
    latestTimestamp = [latestBattery?.registeredAt, latestHumidity?.registeredAt, latestTemperature?.registeredAt]
      .filter((value): value is string => Boolean(value))
      .sort()
      .at(-1),
    hasMeasurements =
      loaderData.battery.length > 0 || loaderData.humidity.length > 0 || loaderData.temperature.length > 0;

  if (navigation.state === "loading") {
    return <LoadingState label="Loading the selected day..." />;
  }

  return (
    <Stack gap="8">
      <Stack gap="2">
        <Heading size="2xl">Outside conditions</Heading>
        <Text color="fg.muted">Latest readings for {loaderData.date}.</Text>
      </Stack>

      <Form method="get">
        <HStack align="end" gap="3" wrap="wrap">
          <Stack gap="1">
            <chakra.label htmlFor="date" fontSize="sm" fontWeight="medium">
              Date
            </chakra.label>

            <Input id="date" name="date" type="date" defaultValue={loaderData.date} />
          </Stack>

          <Button type="submit">Load date</Button>
        </HStack>
      </Form>

      <Grid
        templateColumns={{
          base: "1fr",
          md: "repeat(3, 1fr)"
        }}
        gap="4"
      >
        <MetricCard
          label="Temperature"
          value={formatValue(latestTemperature?.temp, " °C", 1)}
          helper={latestTemperature ? formatRegisteredAt(latestTemperature.registeredAt) : "No reading for this date"}
        />

        <MetricCard
          label="Humidity"
          value={formatValue(latestHumidity?.airHumidity, " %", 1)}
          helper={latestHumidity ? formatRegisteredAt(latestHumidity.registeredAt) : "No reading for this date"}
        />

        <MetricCard
          label="Battery"
          value={formatValue(latestBattery?.batteryStatus, " %")}
          helper={latestBattery ? formatRegisteredAt(latestBattery.registeredAt) : "No reading for this date"}
        />
      </Grid>

      {latestTimestamp ? (
        <Text color="fg.muted">Most recent device reading: {formatRegisteredAt(latestTimestamp)}.</Text>
      ) : null}

      {hasMeasurements ? null : (
        <EmptyState title="No measurements yet" description="There are no measurements recorded for this date." />
      )}
    </Stack>
  );
};
