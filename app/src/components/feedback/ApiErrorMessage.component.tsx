import { Alert, List, Text } from "@chakra-ui/react";
import type { ReactElement } from "react";

import { getNetworkErrorMessage, getUserFacingErrorMessage, isApiRequestError } from "../../lib";

interface ApiErrorMessageProps {
  error: unknown;
  title?: string;
}

export const ApiErrorMessage = ({ error, title = "Request failed" }: ApiErrorMessageProps): ReactElement => {
  const message = isApiRequestError(error)
      ? getUserFacingErrorMessage(error.status, error.problem)
      : error instanceof Error
        ? getNetworkErrorMessage(error)
        : "Something went wrong while contacting the API.",
    details = isApiRequestError(error) ? (error.problem?.errors ?? []) : [];

  return (
    <Alert.Root status="error" variant="subtle" role="alert">
      <Alert.Indicator />
      <Alert.Content>
        <Alert.Title>{title}</Alert.Title>
        <Alert.Description>{message}</Alert.Description>

        {details.length > 0 ? (
          <List.Root ps="5" mt="2">
            {details.map((detail, index) => (
              <List.Item key={`${detail.parameter ?? detail.pointer ?? "error"}-${index}`}>
                <Text>{detail.description}</Text>
              </List.Item>
            ))}
          </List.Root>
        ) : null}
      </Alert.Content>
    </Alert.Root>
  );
};
