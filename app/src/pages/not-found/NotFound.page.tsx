import type { ReactElement } from "react";
import { Button, Center, Heading, Stack, Text } from "@chakra-ui/react";
import { Link } from "react-router";

export const NotFoundPage = (): ReactElement => (
  <Center minH="60dvh">
    <Stack align="center" gap="4" textAlign="center">
      <Heading size="2xl">Page not found</Heading>
      <Text color="fg.muted">The page you requested does not exist.</Text>
      <Button asChild>
        <Link to="/">Back to dashboard</Link>
      </Button>
    </Stack>
  </Center>
);
