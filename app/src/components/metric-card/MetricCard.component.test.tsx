import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { MetricCard } from "@/components/metric-card/MetricCard.component";

describe("MetricCard", () => {
  it("renders the metric and supporting information", () => {
    render(
      <ChakraProvider value={defaultSystem}>
        <MetricCard label="Temperature" value="21.5 °C" helper="Today at 12:00" />
      </ChakraProvider>
    );

    expect(screen.getByText("Temperature")).toBeDefined();
    expect(screen.getByText("21.5 °C")).toBeDefined();
    expect(screen.getByText("Today at 12:00")).toBeDefined();
  });
});
