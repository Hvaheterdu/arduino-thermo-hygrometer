import type { ReactElement } from "react";
import { useEffect, useMemo, useState } from "react";
import { Alert, Button, Field, Heading, Input, NativeSelect, Stack, Text } from "@chakra-ui/react";
import { type SubmitHandler, useForm, useWatch } from "react-hook-form";
import { useSWRConfig } from "swr";

import { ApiErrorMessage } from "@/components";
import { useCreateBattery, useCreateHumidity, useCreateTemperature } from "@/hooks";
import { dateToRegisteredAt, getBatteryKey, getHumidityKey, getTemperatureKey, getToday, isValidDateTime } from "@/lib";
import type { BatteryDto, HumidityDto, SensorConfig, SensorResource, TemperatureDto } from "@/types";
import { SENSOR_CONFIG, SENSOR_OPTIONS } from "@/types/sensors";

type CreateReadingFormValues = {
  resource: SensorResource;
  registeredAt: string;
  value: string;
};

const getDefaultDateTime = (): string => `${getToday()}T12:00`;

export const CreatePage = (): ReactElement => {
  const { mutate } = useSWRConfig();

  const createBattery = useCreateBattery();
  const createHumidity = useCreateHumidity();
  const createTemperature = useCreateTemperature();

  const [success, setSuccess] = useState<boolean>(false);

  const {
    control,
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm<CreateReadingFormValues>({
    defaultValues: {
      resource: "temperature",
      registeredAt: getDefaultDateTime(),
      value: ""
    },
    mode: "onSubmit",
    shouldFocusError: true
  });

  const resource: SensorResource = useWatch({
    control,
    name: "resource"
  });

  const config: SensorConfig = useMemo(
    () => SENSOR_CONFIG[resource],
    [resource]
  );

  const isMutating: boolean =
    createBattery.isMutating ||
    createHumidity.isMutating ||
    createTemperature.isMutating;

  const error =
    resource === "battery"
      ? createBattery.error
      : resource === "humidity"
        ? createHumidity.error
        : createTemperature.error;

  useEffect(() => {
    setSuccess(false);
  }, [resource]);

  const onSubmit: SubmitHandler<CreateReadingFormValues> = async (
    formValues
  ): Promise<void> => {
    setSuccess(false);

    const dateTime: string =
      formValues.registeredAt.length === 16
        ? `${formValues.registeredAt}:00`
        : formValues.registeredAt;

    const numericValue: number = Number(formValues.value);
    const registeredAt: string = dateToRegisteredAt(
      dateTime.slice(0, 10)
    );

    try {
      if (formValues.resource === "battery") {
        await createBattery.trigger({
          registeredAt: dateTime,
          batteryStatus: numericValue
        } satisfies BatteryDto);

        await mutate(getBatteryKey(registeredAt, true));
      } else if (formValues.resource === "humidity") {
        await createHumidity.trigger({
          registeredAt: dateTime,
          airHumidity: numericValue
        } satisfies HumidityDto);

        await mutate(getHumidityKey(registeredAt, true));
      } else {
        await createTemperature.trigger({
          registeredAt: dateTime,
          temp: numericValue
        } satisfies TemperatureDto);

        await mutate(getTemperatureKey(registeredAt, true));
      }

      reset({
        resource: formValues.resource,
        registeredAt: formValues.registeredAt,
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

        <Text color="fg.muted">
          Register a reading directly with the Arduino API.
        </Text>
      </Stack>

      <form onSubmit={handleSubmit(onSubmit)} noValidate>
        <Stack gap="5">
          <Field.Root required invalid={!!errors.resource}>
            <Field.Label>Sensor</Field.Label>

            <NativeSelect.Root>
              <NativeSelect.Field
                {...register("resource", {
                  required: "Select a sensor."
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

            {errors.resource ? (
              <Field.ErrorText>
                {errors.resource.message}
              </Field.ErrorText>
            ) : null}
          </Field.Root>

          <Field.Root required invalid={!!errors.registeredAt}>
            <Field.Label>Registered at</Field.Label>

            <Input
              type="datetime-local"
              {...register("registeredAt", {
                required: "Enter the date and time.",
                validate: (value: string) =>
                  isValidDateTime(value) ||
                  "Enter a valid date and time."
              })}
            />

            <Field.HelperText>
              Use the backend&apos;s local date and time.
            </Field.HelperText>

            {errors.registeredAt ? (
              <Field.ErrorText>
                {errors.registeredAt.message}
              </Field.ErrorText>
            ) : null}
          </Field.Root>

          <Field.Root required invalid={!!errors.value}>
            <Field.Label>Value</Field.Label>

            <Input
              type="number"
              min={config.min}
              max={config.max}
              step={config.step}
              placeholder={`${config.min}–${config.max}`}
              {...register("value", {
                required: "Enter a value.",
                validate: (value: string) => {
                  const numericValue: number = Number(value);
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

            {errors.value ? (
              <Field.ErrorText>
                {errors.value.message}
              </Field.ErrorText>
            ) : null}
          </Field.Root>

          <Button
            type="submit"
            loading={isMutating}
            alignSelf="flex-start"
          >
            Save reading
          </Button>

          {error ? (
            <ApiErrorMessage
              error={error}
              title="Could not save reading"
            />
          ) : null}

          {success ? (
            <Alert.Root status="success">
              <Alert.Indicator />

              <Alert.Content>
                <Alert.Title>Reading created</Alert.Title>

                <Alert.Description>
                  The measurement was saved successfully.
                </Alert.Description>
              </Alert.Content>
            </Alert.Root>
          ) : null}
        </Stack>
      </form>
    </Stack>
  );
};