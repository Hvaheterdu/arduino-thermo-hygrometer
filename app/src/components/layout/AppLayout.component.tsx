import type { ReactElement } from "react";
import { Container, Flex, Heading, HStack, Link as ChakraLink, Stack } from "@chakra-ui/react";
import { NavLink, Outlet, ScrollRestoration } from "react-router";

type NavigationItem = {
  to: string;
  label: string;
  end: boolean;
};

const navigation: readonly NavigationItem[] = [
  { to: "/", label: "Dashboard", end: true },
  { to: "/history", label: "History", end: false },
  { to: "/create", label: "Add reading", end: false }
];

export const AppLayout = (): ReactElement => (
  <Stack minH="100dvh" bg="bg.subtle">
    <Flex as="header" borderBottomWidth="1px" bg="bg" justify="center">
      <Container maxW="7xl" py="4">
        <Flex align={{ base: "flex-start", md: "center" }} direction={{ base: "column", md: "row" }} gap="4">
          <Heading size="lg" flex="1">
            Arduino Thermo Hygrometer
          </Heading>
          <HStack gap="4" wrap="wrap">
            {navigation.map((item: NavigationItem) => (
              <ChakraLink key={item.to} asChild _currentPage={{ fontWeight: "bold" }}>
                <NavLink to={item.to} end={item.end}>
                  {item.label}
                </NavLink>
              </ChakraLink>
            ))}
          </HStack>
        </Flex>
      </Container>
    </Flex>
    <Container as="main" maxW="7xl" flex="1" py={{ base: "6", md: "10" }}>
      <Outlet />
      <ScrollRestoration />
    </Container>
  </Stack>
);
