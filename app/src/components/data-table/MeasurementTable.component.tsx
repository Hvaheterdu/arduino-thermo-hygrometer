import { Table, Text } from "@chakra-ui/react";
import type { ReactElement } from "react";

import { SENSOR_CONFIG, formatRegisteredAt } from "../../lib";
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
        {rows.map((row, index) => (
          <Table.Row key={`${row.registeredAt}-${row.value}-${index}`}>
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
