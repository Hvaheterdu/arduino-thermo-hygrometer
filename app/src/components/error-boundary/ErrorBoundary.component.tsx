import { Alert, Button, Center, Container, Heading, Stack, Text } from "@chakra-ui/react";
import { getUserFacingErrorMessage, isApiRequestError } from "@lib/api/error";
import type { ReactElement } from "react";
import { isRouteErrorResponse, Link, useRouteError } from "react-router";

const getRouteErrorMessage = (error: unknown): string => {
  if (isApiRequestError(error)) {
    return getUserFacingErrorMessage(error.status, error.problem);
  }
  if (isRouteErrorResponse(error)) {
    if (error.status === 404) {
      return "The requested page could not be found.";
    }
    if (error.status === 429) {
      return "Too many requests. Please try again shortly.";
    }
    if (error.status >= 500) {
      return "The server could not complete the request.";
    }
    return error.statusText || "The page could not be loaded.";
  }
  return "The page could not be loaded. Please try again.";
};

const getRouteErrorStatus = (error: unknown): number | undefined => {
  if (isApiRequestError(error)) {
    return error.status;
  }
  return isRouteErrorResponse(error) ? error.status : undefined;
};

export const ErrorBoundary = (): ReactElement => {
  const error: unknown = useRouteError();
  const status: number | undefined = getRouteErrorStatus(error);

  return (
    <Center minH="100dvh" px="4">
      <Container maxW="xl">
        <Stack gap="5">
          <Alert.Root status="error">
            <Alert.Indicator />
            <Alert.Content>
              <Alert.Title>{status ? `Request error (${status})` : "Something went wrong"}</Alert.Title>
              <Alert.Description>{getRouteErrorMessage(error)}</Alert.Description>
            </Alert.Content>
          </Alert.Root>
          <Stack gap="2">
            <Heading size="lg">The application could not continue</Heading>
            <Text color="fg.muted">Try again or return to the dashboard.</Text>
            <Button asChild alignSelf="flex-start">
              <Link to="/">Back to dashboard</Link>
            </Button>
          </Stack>
        </Stack>
      </Container>
    </Center>
  );
};
