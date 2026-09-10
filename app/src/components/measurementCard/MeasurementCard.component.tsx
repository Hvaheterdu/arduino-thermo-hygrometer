import { Alert, Skeleton, Stack, Text } from "@chakra-ui/react";
import type { IconDefinition } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import type { ReactElement } from "react";
import { Link as RouterLink } from "react-router";

import { Card } from "@/components/primitives/Card.component";
import { useMeasurements } from "@/hooks/useMeasurements";
import type { MeasurementDto, MeasurementQuery, ResourceKind } from "@/types/measurement.types";
import { formatRegisteredAt } from "@/utils/date.util";
import { toErrorMessage } from "@/utils/problemDetails.util";
import { buildHistoryPath } from "@/utils/routes.util";

type MeasurementCardProps = {
  resource: ResourceKind;
  query: MeasurementQuery;
  title: string;
  icon: IconDefinition;
  formatValue: (measurement: MeasurementDto) => string;
};

export const MeasurementCard = ({ resource, query, title, icon, formatValue }: MeasurementCardProps): ReactElement => {
  const { data: measurements, error, isLoading } = useMeasurements(resource, query);
  const latestReading = measurements?.reduce<MeasurementDto | undefined>((latest, measurement): MeasurementDto => {
    if (!latest) {
      return measurement;
    }

    return Date.parse(measurement.registeredAt) > Date.parse(latest.registeredAt) ? measurement : latest;
  }, undefined);

  return (
    <Card heading={title} data-testid={`measurement-card-${resource}`}>
      <Stack gap={3}>
        <FontAwesomeIcon
          icon={icon}
          aria-label={`${title} icon`}
          aria-hidden="true"
          style={{
            fontSize: "1rem",
            color: "var(--chakra-colors-brand-solid)"
          }}
        />
        {isLoading && <Skeleton height="2.5rem" role="status" aria-label={`Loading ${title.toLowerCase()} reading`} />}
        {error && (
          <Alert.Root status="error" role="alert" data-testid={`measurement-card-error-${resource}`}>
            <Alert.Indicator />
            <Alert.Title>{toErrorMessage(error)}</Alert.Title>
          </Alert.Root>
        )}
        <Stack gap={1} aria-live="polite" aria-atomic="true">
          {!isLoading && !error && !latestReading && (
            <Text color="fg.muted" data-testid={`measurement-card-empty-${resource}`}>
              No readings yet for today.
            </Text>
          )}
          {latestReading && (
            <>
              <Text fontSize="3xl" fontWeight="bold" data-testid={`measurement-card-value-${resource}`}>
                {formatValue(latestReading)}
              </Text>
              <Text fontSize="sm" color="fg.muted">
                Registered {formatRegisteredAt(latestReading.registeredAt)}
              </Text>
            </>
          )}
        </Stack>
        <RouterLink to={buildHistoryPath(resource)} style={{ color: "var(--chakra-colors-brand-fg)" }}>
          View full history →
        </RouterLink>
      </Stack>
    </Card>
  );
};
