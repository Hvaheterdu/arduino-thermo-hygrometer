import type { ReactElement } from "react";
import { Table, Text } from "@chakra-ui/react";

import { formatRegisteredAt } from "@/lib";
import type { SensorResource } from "@/types";

type MeasurementTableRow = {
  registeredAt: string;
  value: string;
};

type MeasurementTableProps = {
  resource: SensorResource;
  rows: MeasurementTableRow[];
};

const getSensorLabel = (resource: SensorResource): string => {
  if (resource === "battery") {
    return "Battery";
  }
  if (resource === "humidity") {
    return "Humidity";
  }
  return "Temperature";
};

const getSensorUnit = (resource: SensorResource): string => {
  if (resource === "battery") {
    return "%";
  }
  if (resource === "humidity") {
    return "% RH";
  }
  return "°C";
};

export const MeasurementTable = ({ resource, rows }: MeasurementTableProps): ReactElement => {
  const label: string = getSensorLabel(resource);
  const unit: string = getSensorUnit(resource);

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
