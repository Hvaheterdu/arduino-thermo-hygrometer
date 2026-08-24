import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { CreatePage } from "@/pages";

describe("CreatePage", () => {
  it("validates required fields through React Hook Form before submitting", () => {
    render(
      <ChakraProvider value={defaultSystem}>
        <CreatePage />
      </ChakraProvider>
    );

    fireEvent.click(screen.getByRole("button", { name: "Save reading" }));

    expect(screen.getByText("Enter the date and time.")).toBeDefined();
    expect(screen.getByText("Enter a value.")).toBeDefined();
  });

  it("validates sensor-specific value ranges", () => {
    render(
      <ChakraProvider value={defaultSystem}>
        <CreatePage />
      </ChakraProvider>
    );

    const dateTime = screen.getByLabelText("Registered at");
    const value = screen.getByLabelText("Value");

    fireEvent.change(dateTime, { target: { value: "2026-08-24T12:00" } });
    fireEvent.change(value, { target: { value: "999" } });
    fireEvent.click(screen.getByRole("button", { name: "Save reading" }));

    expect(screen.getByText("Value must be between -55 and 125 °C.")).toBeDefined();
  });
});
