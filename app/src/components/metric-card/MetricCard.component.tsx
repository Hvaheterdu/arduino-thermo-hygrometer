import { Card, Heading, Stack, Text } from "@chakra-ui/react";
import type { ReactElement } from "react";

type MetricCardProps = {
  label: string;
  value: string;
  helper: string;
};

export const MetricCard = ({ label, value, helper }: MetricCardProps): ReactElement => (
  <Card.Root variant="outline" height="100%">
    <Card.Body>
      <Stack gap="2">
        <Text color="fg.muted" fontSize="sm" fontWeight="medium">
          {label}
        </Text>

        <Heading size="2xl">{value}</Heading>

        <Text color="fg.muted" fontSize="sm">
          {helper}
        </Text>
      </Stack>
    </Card.Body>
  </Card.Root>
);
