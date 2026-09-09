import { Heading, Stack, Text } from "@chakra-ui/react";
import { faTriangleExclamation } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import type { ReactElement } from "react";
import { Link } from "react-router";

import { Button } from "@/components/primitives/Button.component";
import { ROUTES } from "@/utils/routes.util";

export const NotFoundPage = (): ReactElement => {
  return (
    <Stack align="center" data-testid="not-found-page" gap={4} py={16} textAlign="center">
      <FontAwesomeIcon
        aria-hidden="true"
        aria-label="hidden warning icon"
        icon={faTriangleExclamation}
        style={{
          color: "var(--chakra-colors-orange-600)",
          fontSize: "3rem"
        }}
      />
      <Heading as="h2" size="xl">
        Page not found
      </Heading>
      <Text color="fg.muted" maxW="prose">
        The page you're looking for doesn't exist, or the resource in the URL isn't one this dashboard understands (try
        "battery", "humidity", or "temperature").
      </Text>
      <Button asChild>
        <Link to={ROUTES.DASHBOARD}>Back to dashboard</Link>
      </Button>
    </Stack>
  );
};
