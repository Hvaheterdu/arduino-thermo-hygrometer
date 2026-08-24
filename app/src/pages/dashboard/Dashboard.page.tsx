import type { ReactElement } from "react";
import { Button, chakra, Grid, Heading, HStack, Input, Stack, Text } from "@chakra-ui/react";
import { Form, useLoaderData, useNavigation } from "react-router";

import { ApiErrorMessage, EmptyState, LoadingState, MetricCard } from "@/components";
import { useBatteryReadings, useHumidityReadings, useTemperatureReadings } from "@/hooks";
import { formatRegisteredAt, latestByRegisteredAt } from "@/lib";
import type { BatteryDto, HumidityDto, TemperatureDto } from "@/types";
import { dashboardLoader } from "@/pages/dashboard/dashboard.loader";

const formatValue = (
  value: number | undefined,
  suffix: string,
  fractionDigits: number = 0
): string =>
  value === undefined
    ? "—"
    : `${value.toFixed(fractionDigits)}${suffix}`;

export const DashboardPage = (): ReactElement => {
  const loaderData = useLoaderData<typeof dashboardLoader>();
  const navigation = useNavigation();

  const battery = useBatteryReadings(
    loaderData.registeredAt,
    true
  );
  const humidity = useHumidityReadings(
    loaderData.registeredAt,
    true
  );
  const temperature = useTemperatureReadings(
    loaderData.registeredAt,
    true
  );

  const latestBattery: BatteryDto | undefined =
    latestByRegisteredAt<BatteryDto>(battery.data ?? []);

  const latestHumidity: HumidityDto | undefined =
    latestByRegisteredAt<HumidityDto>(humidity.data ?? []);

  const latestTemperature: TemperatureDto | undefined =
    latestByRegisteredAt<TemperatureDto>(temperature.data ?? []);

  const latestTimestamp: string | undefined = [
    latestBattery?.registeredAt,
    latestHumidity?.registeredAt,
    latestTemperature?.registeredAt
  ]
    .filter(
      (value: string | undefined): value is string =>
        Boolean(value)
    )
    .sort()
    .at(-1);

  const hasMeasurements: boolean = Boolean(
    battery.data?.length ||
    humidity.data?.length ||
    temperature.data?.length
  );

  const isLoading: boolean =
    navigation.state === "loading";

  if (isLoading) {
    return (
      <LoadingState label="Loading the selected day..." />
    );
  }

  return (
    <Stack gap="8">
      <Stack gap="2">
        <Heading size="2xl">Garden conditions</Heading>

        <Text color="fg.muted">
          Latest readings for {loaderData.date}.
        </Text>
      </Stack>

      <Form method="get">
        <HStack align="end" gap="3" wrap="wrap">
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
          value={formatValue(
            latestTemperature?.temp,
            " °C",
            1
          )}
          helper={
            latestTemperature
              ? formatRegisteredAt(
                latestTemperature.registeredAt
              )
              : "No reading for this date"
          }
        />

        <MetricCard
          label="Humidity"
          value={formatValue(
            latestHumidity?.airHumidity,
            " %",
            1
          )}
          helper={
            latestHumidity
              ? formatRegisteredAt(
                latestHumidity.registeredAt
              )
              : "No reading for this date"
          }
        />

        <MetricCard
          label="Battery"
          value={formatValue(
            latestBattery?.batteryStatus,
            " %"
          )}
          helper={
            latestBattery
              ? formatRegisteredAt(
                latestBattery.registeredAt
              )
              : "No reading for this date"
          }
        />
      </Grid>

      {latestTimestamp ? (
        <Text color="fg.muted">
          Most recent device reading:{" "}
          {formatRegisteredAt(latestTimestamp)}.
        </Text>
      ) : null}

      {!hasMeasurements ? (
        <EmptyState
          title="No measurements yet"
          description="There are no measurements recorded for this date."
        />
      ) : null}

      {battery.error ||
      humidity.error ||
      temperature.error ? (
        <Stack gap="3">
          <Heading size="sm">
            Some measurements could not be refreshed
          </Heading>

          {battery.error ? (
            <ApiErrorMessage
              error={battery.error}
              title="Battery"
            />
          ) : null}

          {humidity.error ? (
            <ApiErrorMessage
              error={humidity.error}
              title="Humidity"
            />
          ) : null}

          {temperature.error ? (
            <ApiErrorMessage
              error={temperature.error}
              title="Temperature"
            />
          ) : null}
        </Stack>
      ) : null}
    </Stack>
  );
};