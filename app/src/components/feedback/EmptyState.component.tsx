import { Alert } from "@chakra-ui/react";
import type { ReactElement } from "react";

type EmptyStateProps = {
  title: string;
  description: string;
};

export const EmptyState = ({ title, description }: EmptyStateProps): ReactElement => (
  <Alert.Root status="info" variant="subtle">
    <Alert.Indicator />
    <Alert.Content>
      <Alert.Title>{title}</Alert.Title>
      <Alert.Description>{description}</Alert.Description>
    </Alert.Content>
  </Alert.Root>
);
