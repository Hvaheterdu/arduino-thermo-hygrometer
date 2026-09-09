import { Alert, Button, Checkbox, Heading, HStack, Stack, Text, useDisclosure } from "@chakra-ui/react";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { type ChangeEvent, type ReactElement, useState } from "react";
import { useParams } from "react-router";

import { MeasurementForm } from "@/components/measurementForm/MeasurementForm.component";
import { MeasurementTable } from "@/components/measurementTable/MeasurementTable.component";
import { Input } from "@/components/primitives/Input.component";
import { Modal } from "@/components/primitives/Modal.component";
import { useDeleteMeasurement } from "@/hooks/useDeleteMeasurement";
import { useMeasurements } from "@/hooks/useMeasurements";
import { NotFoundPage } from "@/pages/notFound/NotFound.page";
import type { ResourceKind } from "@/types/measurement.types";
import { formatRegisteredAt, toDateTimeLocalInputValue, toRegisteredAtQueryValue } from "@/utils/date.util";
import { toErrorMessage } from "@/utils/problemDetails.util";
import { RESOURCE_CONFIG } from "@/utils/resourceConfig.util";
import { isResourceKind } from "@/utils/routes.util";

export const HistoryPage = (): ReactElement => {
  const { resource } = useParams<{ resource: string }>();

  if (!isResourceKind(resource)) {
    return <NotFoundPage />;
  }

  return <ResourceHistory resource={resource} />;
};

type ResourceHistoryProps = {
  resource: ResourceKind;
};

const ResourceHistory = ({ resource }: ResourceHistoryProps): ReactElement => {
  const config = RESOURCE_CONFIG[resource];
  const [selectedRegisteredAt, setSelectedRegisteredAt] = useState(() => toDateTimeLocalInputValue(new Date()));
  const [dateOnly, setDateOnly] = useState(true);
  const query = { registeredAt: toRegisteredAtQueryValue(selectedRegisteredAt), dateOnly };

  const { data, error, isLoading } = useMeasurements(resource, query);
  const addDisclosure = useDisclosure();
  const deleteDisclosure = useDisclosure();
  const { trigger: deleteReadings, isMutating: isDeleting, error: deleteError } = useDeleteMeasurement(resource);

  const handleConfirmDelete = async (): Promise<void> => {
    await deleteReadings(query);
    deleteDisclosure.onClose();
  };

  return (
    <Stack gap={6}>
      <HStack gap={4} justify="space-between" wrap="wrap">
        <HStack gap={3}>
          <FontAwesomeIcon
            aria-hidden="true"
            aria-label={`hidden ${config.title} icon`}
            icon={config.icon}
            style={{
              color: "var(--chakra-colors-brand-solid)",
              fontSize: "2rem"
            }}
          />
          <Heading as="h2" size="xl">
            {config.title} history
          </Heading>
        </HStack>
        <HStack gap={3}>
          <Button data-testid="history-add-button" onClick={addDisclosure.onOpen}>
            Add reading
          </Button>
          <Button
            colorPalette="red"
            data-testid="history-delete-button"
            disabled={!data || data.length === 0}
            variant="outline"
            onClick={deleteDisclosure.onOpen}
          >
            <FontAwesomeIcon aria-hidden="true" aria-label="hidden trash can icon" icon={faTrash} /> Delete matching
            readings
          </Button>
        </HStack>
      </HStack>

      <HStack align="flex-end" gap={4} wrap="wrap">
        <Input
          data-testid="history-date-input"
          label={dateOnly ? "Date" : "Date and time"}
          type={dateOnly ? "date" : "datetime-local"}
          value={dateOnly ? selectedRegisteredAt.slice(0, 10) : selectedRegisteredAt}
          onChange={(event: ChangeEvent<HTMLInputElement>) => {
            const value = event.target.value;
            const newDateTime = dateOnly ? `${value}T${selectedRegisteredAt.slice(11) || "00:00"}` : value;
            setSelectedRegisteredAt(newDateTime);
          }}
        />
        <Checkbox.Root
          checked={dateOnly}
          data-testid="history-date-only-checkbox"
          onCheckedChange={(details) => setDateOnly(Boolean(details.checked))}
        >
          <Checkbox.HiddenInput />
          <Checkbox.Control />
          <Checkbox.Label>Match by date only</Checkbox.Label>
        </Checkbox.Root>
      </HStack>

      <MeasurementTable
        error={error}
        formatValue={config.formatValue}
        isLoading={isLoading}
        readings={data}
        valueHeader={`${config.fieldLabel} (${config.unit})`}
      />

      <Modal
        open={addDisclosure.open}
        title={`Add ${config.title.toLowerCase()} reading`}
        onClose={addDisclosure.onClose}
      >
        <MeasurementForm
          fieldLabel={config.fieldLabel}
          fieldName={config.fieldName}
          max={config.max}
          min={config.min}
          resource={resource}
          step={config.step}
          unit={config.unit}
          onCreated={addDisclosure.onClose}
        />
      </Modal>

      <Modal
        footer={
          <HStack gap={3}>
            <Button data-testid="history-delete-cancel" onClick={deleteDisclosure.onClose} variant="ghost">
              Cancel
            </Button>
            <Button
              colorPalette="red"
              data-testid="history-delete-confirm"
              loading={isDeleting}
              onClick={handleConfirmDelete}
            >
              Delete
            </Button>
          </HStack>
        }
        open={deleteDisclosure.open}
        title="Delete matching readings"
        onClose={deleteDisclosure.onClose}
      >
        <Stack gap={3}>
          {deleteError && (
            <Alert.Root data-testid="history-delete-error" role="alert" status="error">
              <Alert.Indicator />
              <Alert.Title>{toErrorMessage(deleteError)}</Alert.Title>
            </Alert.Root>
          )}
          <Text>
            This deletes every {config.title.toLowerCase()} reading matching{" "}
            {dateOnly ? formatRegisteredAt(query.registeredAt, true) : formatRegisteredAt(query.registeredAt)}
            {dateOnly ? " (any time)" : " exactly"}. This cannot be undone.
          </Text>
        </Stack>
      </Modal>
    </Stack>
  );
};
