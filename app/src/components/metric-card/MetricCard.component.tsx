import type { ReactElement } from "react";
import { Card, Flex, Heading, Stack, Text } from "@chakra-ui/react";

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
        <Flex align="baseline" gap="2">
          <Heading size="2xl">{value}</Heading>
        </Flex>
        <Text color="fg.muted" fontSize="sm">
          {helper}
        </Text>
      </Stack>
    </Card.Body>
  </Card.Root>
);
