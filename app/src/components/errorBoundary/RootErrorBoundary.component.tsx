import { Box, Button, Code, Heading, Stack, Text } from "@chakra-ui/react";
import { faTriangleExclamation } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import type { ReactElement } from "react";
import { isRouteErrorResponse, useNavigate, useRouteError } from "react-router";

import { ROUTES } from "@/utils/routes.util";

export const RootErrorBoundary = (): ReactElement => {
  const error = useRouteError();
  const navigate = useNavigate();

  const title = isRouteErrorResponse(error) ? `${error.status} ${error.statusText}` : "Unexpected error";
  let detail: string;
  if (isRouteErrorResponse(error)) {
    detail = error.data?.message ?? error.statusText;
  } else if (error instanceof Error) {
    detail = error.message;
  } else {
    detail = "Something went wrong while loading this page.";
  }

  return (
    <Box data-testid="root-error-boundary" px={4} py={16} role="alert" textAlign="center">
      <Stack align="center" gap={4}>
        <FontAwesomeIcon
          aria-hidden="true"
          icon={faTriangleExclamation}
          style={{ color: "var(--chakra-colors-orange-600)", fontSize: "3rem" }}
        />
        <Heading as="h2" size="lg">
          {title}
        </Heading>
        <Text color="fg.muted" maxW="prose">
          {detail}
        </Text>
        {import.meta.env.DEV && error instanceof Error && error.stack && (
          <Code fontSize="xs" maxW="full" p={4} textAlign="left" whiteSpace="pre-wrap">
            {error.stack}
          </Code>
        )}
        <Button colorPalette="brand" data-testid="root-error-boundary-back" onClick={() => navigate(ROUTES.DASHBOARD)}>
          Back to dashboard
        </Button>
      </Stack>
    </Box>
  );
};
