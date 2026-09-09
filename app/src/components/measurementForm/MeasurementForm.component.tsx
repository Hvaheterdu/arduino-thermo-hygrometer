import { Alert, Stack } from "@chakra-ui/react";
import type { ReactElement } from "react";
import { useForm } from "react-hook-form";

import { Button } from "@/components/primitives/Button.component";
import { Input } from "@/components/primitives/Input.component";
import { useCreateMeasurement } from "@/hooks/useCreateMeasurement";
import type { MeasurementDto, ResourceKind } from "@/types/measurement.types";
import { toDateTimeLocalInputValue, toRegisteredAtQueryValue } from "@/utils/date.util";
import { toErrorMessage } from "@/utils/problemDetails.util";

type MeasurementFormValues = {
  registeredAt: string;
  value: string;
};

type MeasurementFormProps = {
  resource: ResourceKind;
  fieldName: string;
  fieldLabel: string;
  unit: string;
  min: number;
  max: number;
  step?: number | undefined;
  onCreated?: () => void;
};

export const MeasurementForm = ({
  resource,
  fieldName,
  fieldLabel,
  unit,
  min,
  max,
  step = 0.1,
  onCreated
}: MeasurementFormProps): ReactElement => {
  const { trigger, isMutating, error } = useCreateMeasurement(resource);
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<MeasurementFormValues>({
    defaultValues: { registeredAt: toDateTimeLocalInputValue(new Date()), value: "" }
  });

  const onSubmit = handleSubmit(async (formValues) => {
    const newReading = {
      registeredAt: toRegisteredAtQueryValue(formValues.registeredAt),
      [fieldName]: Number(formValues.value)
    } as MeasurementDto;
    await trigger(newReading);
    onCreated?.();
  });

  return (
    <form data-testid={`measurement-form-${resource}`} noValidate onSubmit={onSubmit}>
      <Stack gap={4}>
        {error && (
          <Alert.Root data-testid="measurement-form-error" role="alert" status="error">
            <Alert.Indicator />
            <Alert.Title>{toErrorMessage(error)}</Alert.Title>
          </Alert.Root>
        )}
        <Input
          data-testid="measurement-form-registered-at"
          errorText={errors.registeredAt?.message}
          label="Registered at"
          required
          type="datetime-local"
          {...register("registeredAt", { required: "Registration date and time are required." })}
        />
        <Input
          data-testid="measurement-form-value"
          errorText={errors.value?.message}
          label={`${fieldLabel} (${unit})`}
          max={max}
          min={min}
          required
          step={step}
          type="number"
          {...register("value", {
            required: `${fieldLabel} is required.`,
            min: { value: min, message: `${fieldLabel} must be at least ${min}${unit}.` },
            max: { value: max, message: `${fieldLabel} must be at most ${max}${unit}.` }
          })}
        />
        <Button alignSelf="flex-start" data-testid="measurement-form-submit" loading={isMutating} type="submit">
          Save reading
        </Button>
      </Stack>
    </form>
  );
};
