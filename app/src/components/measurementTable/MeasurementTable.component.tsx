import { Alert, Skeleton, Table, Text } from "@chakra-ui/react";
import type { ReactElement } from "react";

import type { MeasurementDto } from "@/types/measurement.types";
import type { ApiError } from "@/types/problemDetails.types";
import { formatRegisteredAt } from "@/utils/date.util";
import { toErrorMessage } from "@/utils/problemDetails.util";

type MeasurementTableProps = {
  readings: MeasurementDto[] | undefined;
  isLoading: boolean;
  error: ApiError | undefined;
  valueHeader: string;
  formatValue: (measurement: MeasurementDto) => string;
};

export const MeasurementTable = ({
  readings,
  isLoading,
  error,
  valueHeader,
  formatValue
}: MeasurementTableProps): ReactElement => {
  if (isLoading) {
    return (
      <Skeleton aria-label="Loading readings" data-testid="measurement-table-skeleton" height="12rem" role="status" />
    );
  }

  if (error) {
    return (
      <Alert.Root data-testid="measurement-table-error" role="alert" status="error">
        <Alert.Indicator />
        <Alert.Title>{toErrorMessage(error)}</Alert.Title>
      </Alert.Root>
    );
  }

  if (!readings || readings.length === 0) {
    return (
      <Text aria-live="polite" color="fg.muted" data-testid="measurement-table-empty" role="status">
        No readings found for this date.
      </Text>
    );
  }

  return (
    <Table.Root aria-live="polite" data-testid="measurement-table" striped variant="outline">
      <Table.Header>
        <Table.Row>
          <Table.ColumnHeader>Registered at</Table.ColumnHeader>
          <Table.ColumnHeader>{valueHeader}</Table.ColumnHeader>
        </Table.Row>
      </Table.Header>
      <Table.Body>
        {readings.map((reading) => (
          <Table.Row key={reading.registeredAt}>
            <Table.Cell>{formatRegisteredAt(reading.registeredAt)}</Table.Cell>
            <Table.Cell>{formatValue(reading)}</Table.Cell>
          </Table.Row>
        ))}
      </Table.Body>
    </Table.Root>
  );
};
