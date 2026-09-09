import { Heading, SimpleGrid, Stack, useDisclosure } from "@chakra-ui/react";
import type { ReactElement } from "react";

import { MeasurementCard } from "@/components/measurementCard/MeasurementCard.component";
import { MeasurementForm } from "@/components/measurementForm/MeasurementForm.component";
import { Button } from "@/components/primitives/Button.component";
import { Modal } from "@/components/primitives/Modal.component";
import type { MeasurementQuery, ResourceKind } from "@/types/measurement.types";
import { toRegisteredAtQueryValue } from "@/utils/date.util";
import { RESOURCE_CONFIG } from "@/utils/resourceConfig.util";
import { RESOURCE_KINDS } from "@/utils/routes.util";

const todayQuery: MeasurementQuery = {
  registeredAt: toRegisteredAtQueryValue(new Date().toISOString()),
  dateOnly: true
};

export const DashboardPage = (): ReactElement => {
  return (
    <Stack gap={6}>
      <Heading as="h2" size="xl">
        Today's readings
      </Heading>
      <SimpleGrid columns={{ base: 1, md: 3 }} gap={6}>
        {RESOURCE_KINDS.map((resource) => (
          <ResourceSummary key={resource} resource={resource} />
        ))}
      </SimpleGrid>
    </Stack>
  );
};

type ResourceSummaryProps = {
  resource: ResourceKind;
};

const ResourceSummary = ({ resource }: ResourceSummaryProps): ReactElement => {
  const config = RESOURCE_CONFIG[resource];
  const disclosure = useDisclosure();

  return (
    <Stack gap={3}>
      <MeasurementCard
        icon={config.icon}
        query={todayQuery}
        resource={resource}
        title={config.title}
        formatValue={config.formatValue}
      />
      <Button data-testid={`dashboard-add-${resource}`} variant="outline" onClick={disclosure.onOpen}>
        Add {config.title.toLowerCase()} reading
      </Button>
      <Modal open={disclosure.open} title={`Add ${config.title.toLowerCase()} reading`} onClose={disclosure.onClose}>
        <MeasurementForm
          fieldLabel={config.fieldLabel}
          fieldName={config.fieldName}
          max={config.max}
          min={config.min}
          resource={resource}
          step={config.step}
          unit={config.unit}
          onCreated={disclosure.onClose}
        />
      </Modal>
    </Stack>
  );
};
