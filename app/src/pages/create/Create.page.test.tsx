import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { useCreateBattery } from "@hooks/useCreateBattery";
import { useCreateHumidity } from "@hooks/useCreateHumidity";
import { useCreateTemperature } from "@hooks/useCreateTemperature";
import { CreatePage } from "@pages/create/Create.page";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { beforeEach, describe, expect, it, vi } from "vitest";

vi.mock("@/hooks/useCreateBattery", () => ({
  useCreateBattery: vi.fn()
}));

vi.mock("@/hooks/useCreateHumidity", () => ({
  useCreateHumidity: vi.fn()
}));

vi.mock("@/hooks/useCreateTemperature", () => ({
  useCreateTemperature: vi.fn()
}));

const renderCreate = () =>
  render(
    <ChakraProvider value={defaultSystem}>
      <CreatePage />
    </ChakraProvider>
  );

const fill = async (fields: { date?: string; value?: string }) => {
  const user = userEvent.setup();

  if (fields.date !== undefined) {
    const input = screen.getByLabelText("Registered at");

    await user.clear(input);

    if (fields.date) {
      await user.type(input, fields.date);
    }
  }

  if (fields.value !== undefined) {
    const input = screen.getByLabelText("Value");

    await user.clear(input);

    if (fields.value) {
      await user.type(input, fields.value);
    }
  }

  await user.click(screen.getByRole("button", { name: "Save reading" }));
};

describe("CreatePage", () => {
  beforeEach(() => {
    vi.mocked(useCreateBattery).mockReturnValue({
      data: undefined,
      error: undefined,
      isMutating: false,
      reset: vi.fn(),
      trigger: vi.fn()
    });

    vi.mocked(useCreateHumidity).mockReturnValue({
      data: undefined,
      error: undefined,
      isMutating: false,
      reset: vi.fn(),
      trigger: vi.fn()
    });

    vi.mocked(useCreateTemperature).mockReturnValue({
      data: undefined,
      error: undefined,
      isMutating: false,
      reset: vi.fn(),
      trigger: vi.fn()
    });
  });

  it("validates required fields before submitting", async () => {
    renderCreate();

    await fill({
      date: "",
      value: ""
    });

    expect(await screen.findByText("Enter the date and time.")).toBeInTheDocument();
    expect(screen.getByText("Enter a value.")).toBeInTheDocument();
  });

  it("validates sensor-specific value ranges", async () => {
    renderCreate();

    await fill({
      value: "999"
    });

    expect(await screen.findByText("Value must be between -55 and 125 °C.")).toBeInTheDocument();
  });

  it("updates validation when the sensor changes", async () => {
    renderCreate();

    const user = userEvent.setup();
    await user.selectOptions(screen.getByLabelText("Sensor"), "humidity");
    await fill({
      value: "10"
    });

    expect(await screen.findByText("Value must be between 20 and 90 % RH.")).toBeInTheDocument();
  });

  it("creates a temperature reading", async () => {
    const trigger = vi.fn().mockResolvedValue({
      registeredAt: "2026-08-24T12:00:00",
      temp: 21.5
    });

    vi.mocked(useCreateTemperature).mockReturnValue({
      data: undefined,
      error: undefined,
      isMutating: false,
      reset: vi.fn(),
      trigger
    });

    renderCreate();

    await fill({
      date: "2026-08-24T12:00",
      value: "21.5"
    });

    expect(trigger).toHaveBeenCalledWith({
      registeredAt: "2026-08-24T12:00:00",
      temp: 21.5
    });
    expect(await screen.findByText("Reading created")).toBeInTheDocument();
  });
});
