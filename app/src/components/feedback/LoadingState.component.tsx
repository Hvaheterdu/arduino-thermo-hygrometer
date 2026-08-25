import { Center, Spinner, Stack, Text } from "@chakra-ui/react";
import type { ReactElement } from "react";

interface LoadingStateProps {
  label?: string;
}

export const LoadingState = ({ label = "Loading measurements..." }: LoadingStateProps): ReactElement => (
  <Center py="12">
    <Stack align="center" gap="3">
      <Spinner size="lg" />
      <Text color="fg.muted">{label}</Text>
    </Stack>
  </Center>
);
