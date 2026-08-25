import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { MeasurementTable } from "../../components";

describe("MeasurementTable", () => {
  it("renders the correct sensor label and unit", () => {
    render(
      <ChakraProvider value={defaultSystem}>
        <MeasurementTable
          resource="humidity"
          rows={[
            {
              registeredAt: "2026-08-24T12:00:00",
              value: "54.2"
            }
          ]}
        />
      </ChakraProvider>
    );

    expect(screen.getByRole("columnheader", { name: "Humidity" })).toBeInTheDocument();
    expect(screen.getByText("54.2 % RH")).toBeInTheDocument();
  });
});
