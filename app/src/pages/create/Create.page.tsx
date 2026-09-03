import { Alert, Button, Field, Heading, Input, NativeSelect, Stack, Text } from "@chakra-ui/react";
import { ApiErrorMessage } from "@components/feedback/ApiErrorMessage.component";
import { useCreateBattery } from "@hooks/useCreateBattery";
import { useCreateHumidity } from "@hooks/useCreateHumidity";
import { useCreateTemperature } from "@hooks/useCreateTemperature";
import { getToday, isValidDateTime } from "@lib/date/date";
import { SENSOR_CONFIG, SENSOR_OPTIONS } from "@lib/sensor/sensorConfig";
import type { ReactElement } from "react";
import { useState } from "react";
import { type SubmitHandler, useForm, useWatch } from "react-hook-form";

import type { BatteryDto, HumidityDto, TemperatureDto } from "@/types/domain";
import type { SensorConfig, SensorResource } from "@/types/sensor";

type CreateReadingFormValues = {
  resource: SensorResource;
  registeredAt: string;
  value: string;
};

const getDefaultDateTime = (): string => `${getToday()}T12:00`;

export const CreatePage = (): ReactElement => {
  const createBattery = useCreateBattery();
  const createHumidity = useCreateHumidity();
  const createTemperature = useCreateTemperature();
  const [success, setSuccess] = useState(false);

  const {
    control,
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm<CreateReadingFormValues>({
    defaultValues: {
      registeredAt: getDefaultDateTime(),
      resource: "temperature",
      value: ""
    }
  });

  const resource: SensorResource = useWatch({
    control,
    name: "resource"
  });
  const config: SensorConfig = SENSOR_CONFIG[resource];

  const isMutating = createBattery.isMutating || createHumidity.isMutating || createTemperature.isMutating;

  const error: Error | undefined =
    resource === "battery"
      ? createBattery.error
      : resource === "humidity"
        ? createHumidity.error
        : createTemperature.error;

  const onSubmit: SubmitHandler<CreateReadingFormValues> = async ({
    resource,
    registeredAt,
    value
  }: CreateReadingFormValues): Promise<void> => {
    setSuccess(false);

    const dateTime = registeredAt.length === 16 ? `${registeredAt}:00` : registeredAt;
    const numericValue = Number(value);

    try {
      if (resource === "battery") {
        await createBattery.trigger({
          batteryStatus: numericValue,
          registeredAt: dateTime
        } satisfies BatteryDto);
      } else if (resource === "humidity") {
        await createHumidity.trigger({
          airHumidity: numericValue,
          registeredAt: dateTime
        } satisfies HumidityDto);
      } else {
        await createTemperature.trigger({
          registeredAt: dateTime,
          temp: numericValue
        } satisfies TemperatureDto);
      }

      reset({
        registeredAt,
        resource,
        value: ""
      });

      setSuccess(true);
    } catch {
      // Mutation errors are exposed through the mutation hook.
    }
  };

  return (
    <Stack gap="7" maxW="2xl">
      <Stack gap="2">
        <Heading size="2xl">Add measurement</Heading>
        <Text color="fg.muted">Register a reading directly with the Arduino API.</Text>
      </Stack>

      <form onSubmit={handleSubmit(onSubmit)} noValidate>
        <Stack gap="5">
          <Field.Root required invalid={Boolean(errors.resource)}>
            <Field.Label>Sensor</Field.Label>

            <NativeSelect.Root>
              <NativeSelect.Field
                {...register("resource", {
                  required: "Select a sensor.",
                  onChange: () => setSuccess(false)
                })}
              >
                {SENSOR_OPTIONS.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </NativeSelect.Field>

              <NativeSelect.Indicator />
            </NativeSelect.Root>

            {errors.resource ? <Field.ErrorText>{errors.resource.message}</Field.ErrorText> : null}
          </Field.Root>

          <Field.Root required invalid={Boolean(errors.registeredAt)}>
            <Field.Label>Registered at</Field.Label>

            <Input
              type="datetime-local"
              {...register("registeredAt", {
                required: "Enter the date and time.",
                validate: (value) => isValidDateTime(value) || "Enter a valid date and time."
              })}
            />

            <Field.HelperText>Use the backend&apos;s local date and time.</Field.HelperText>

            {errors.registeredAt ? <Field.ErrorText>{errors.registeredAt.message}</Field.ErrorText> : null}
          </Field.Root>

          <Field.Root required invalid={Boolean(errors.value)}>
            <Field.Label>Value</Field.Label>

            <Input
              type="number"
              min={config.min}
              max={config.max}
              step={config.step}
              placeholder={`${config.min}–${config.max}`}
              {...register("value", {
                required: "Enter a value.",
                validate: (value) => {
                  const numericValue = Number(value);

                  if (!Number.isFinite(numericValue)) {
                    return "Enter a valid number.";
                  }

                  if (numericValue < config.min || numericValue > config.max) {
                    return `Value must be between ${config.min} and ${config.max} ${config.unit}.`;
                  }

                  return true;
                }
              })}
            />

            <Field.HelperText>
              Allowed range: {config.min}–{config.max} {config.unit}.
            </Field.HelperText>

            {errors.value ? <Field.ErrorText>{errors.value.message}</Field.ErrorText> : null}
          </Field.Root>

          <Button type="submit" loading={isMutating} alignSelf="flex-start">
            Save reading
          </Button>

          {error ? <ApiErrorMessage error={error} title="Could not save reading" /> : null}

          {success ? (
            <Alert.Root status="success">
              <Alert.Indicator />
              <Alert.Content>
                <Alert.Title>Reading created</Alert.Title>
                <Alert.Description>The measurement was saved successfully.</Alert.Description>
              </Alert.Content>
            </Alert.Root>
          ) : null}
        </Stack>
      </form>
    </Stack>
  );
};
