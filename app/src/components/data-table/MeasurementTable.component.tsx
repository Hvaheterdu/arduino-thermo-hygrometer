import { Table, Text } from "@chakra-ui/react";
import type { ReactElement } from "react";

import { formatRegisteredAt, SENSOR_CONFIG } from "../../lib";
import type { SensorResource } from "../../types";

interface MeasurementTableRow {
  registeredAt: string;
  value: string;
}

interface MeasurementTableProps {
  resource: SensorResource;
  rows: MeasurementTableRow[];
}

export const MeasurementTable = ({ resource, rows }: MeasurementTableProps): ReactElement => {
  const { label, unit } = SENSOR_CONFIG[resource];

  return (
    <Table.Root size="sm" variant="outline">
      <Table.Header>
        <Table.Row>
          <Table.ColumnHeader>Registered</Table.ColumnHeader>
          <Table.ColumnHeader>{label}</Table.ColumnHeader>
        </Table.Row>
      </Table.Header>

      <Table.Body>
        {rows.map((row) => (
          <Table.Row key={`${row.registeredAt}-${row.value}`}>
            <Table.Cell>{formatRegisteredAt(row.registeredAt)}</Table.Cell>

            <Table.Cell>
              <Text fontWeight="semibold">
                {row.value} {unit}
              </Text>
            </Table.Cell>
          </Table.Row>
        ))}
      </Table.Body>
    </Table.Root>
  );
};
