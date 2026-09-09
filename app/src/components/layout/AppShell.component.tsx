import { Box, Container, Flex, Heading, HStack, Link } from "@chakra-ui/react";
import { faBatteryFull, faDroplet, faTemperatureHalf } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import type { CSSProperties, ReactElement } from "react";
import { NavLink, Outlet } from "react-router";

import { buildHistoryPath, RESOURCE_KINDS, ROUTES } from "@/utils/routes.util";

const navIconByResource = {
  battery: faBatteryFull,
  humidity: faDroplet,
  temperature: faTemperatureHalf
} as const;

export const AppShell = (): ReactElement => {
  return (
    <Flex direction="column" minH="100vh">
      <Link
        _focus={{ left: "1rem" }}
        bg="brand.solid"
        borderRadius="md"
        color="brand.contrast"
        fontWeight="semibold"
        href="#main-content"
        left="-9999px"
        position="absolute"
        px={4}
        py={2}
        top="1rem"
        zIndex="popover"
      >
        Skip to main content
      </Link>
      <Box as="header" bg="bg.panel" borderBottomWidth="1px">
        <Container maxW="6xl" py={4}>
          <Flex align="center" gap={4} justify="space-between" wrap="wrap">
            <Heading as="h1" color="brand.fg" size="lg">
              Arduino Thermo Hygrometer
            </Heading>
            <HStack aria-label="Primary" as="nav" gap={1}>
              <NavLink end to={ROUTES.DASHBOARD} style={navLinkStyle}>
                Dashboard
              </NavLink>
              {RESOURCE_KINDS.map((resource) => (
                <NavLink key={resource} to={buildHistoryPath(resource)} style={navLinkStyle}>
                  <HStack gap={1}>
                    <FontAwesomeIcon
                      aria-hidden="true"
                      aria-label={`${resource} history icon`}
                      icon={navIconByResource[resource]}
                    />
                    <span style={{ textTransform: "capitalize" }}>{resource} history</span>
                  </HStack>
                </NavLink>
              ))}
            </HStack>
          </Flex>
        </Container>
      </Box>
      <Box as="main" flex="1" id="main-content" tabIndex={-1}>
        <Container maxW="6xl" py={8}>
          <Outlet />
        </Container>
      </Box>
    </Flex>
  );
};

const navLinkStyle = ({ isActive }: { isActive: boolean }): CSSProperties => ({
  backgroundColor: isActive ? "var(--chakra-colors-brand-subtle)" : "transparent",
  borderRadius: "0.375rem",
  color: isActive ? "var(--chakra-colors-brand-fg)" : "inherit",
  fontWeight: isActive ? 700 : 500,
  padding: "0.5rem 0.75rem",
  textDecoration: "none"
});
