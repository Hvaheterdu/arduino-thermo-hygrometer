import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { useDeleteReading } from "@hooks/useDeleteReading";
import { HistoryPage } from "@pages/history/History.page";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { createMemoryRouter, RouterProvider } from "react-router";
import { beforeEach, describe, expect, it, vi } from "vitest";

vi.mock("@/hooks/useDeleteReading", () => ({
  useDeleteReading: vi.fn()
}));

const renderHistory = (loaderData: any) => {
  const router = createMemoryRouter(
    [
      {
        Component: HistoryPage,
        HydrateFallback: () => null,
        loader: () => loaderData,
        path: "/history"
      }
    ],
    {
      initialEntries: ["/history"]
    }
  );

  render(
    <ChakraProvider value={defaultSystem}>
      <RouterProvider router={router} />
    </ChakraProvider>
  );

  return router;
};

describe("HistoryPage", () => {
  beforeEach(() => {
    vi.mocked(useDeleteReading).mockReturnValue({
      data: undefined,
      error: undefined,
      isMutating: false,
      reset: vi.fn(),
      trigger: vi.fn()
    });
  });

  it.each([
    ["battery", { batteryStatus: 80 }, "80 %"],
    ["humidity", { airHumidity: 54.25 }, "54.3 % RH"],
    ["temperature", { temp: 21.75 }, "21.8 °C"]
  ])("renders %s readings", async (resource, reading, value) => {
    renderHistory({
      date: "2026-08-24",
      readings: [
        {
          registeredAt: "2026-08-24T12:00:00",
          ...reading
        }
      ],
      registeredAt: "2026-08-24T00:00:00",
      resource
    });

    expect(await screen.findByText(value)).toBeInTheDocument();
    expect(
      screen.getByRole("button", {
        name: "Delete all for this day"
      })
    ).toBeInTheDocument();
  });

  it("shows an empty state when there are no readings", async () => {
    renderHistory({
      date: "2026-08-24",
      readings: [],
      registeredAt: "2026-08-24T00:00:00",
      resource: "temperature"
    });

    expect(await screen.findByText("No readings found")).toBeInTheDocument();
    expect(
      screen.queryByRole("button", {
        name: "Delete all for this day"
      })
    ).not.toBeInTheDocument();
  });

  it("deletes all readings for the selected day", async () => {
    const trigger = vi.fn().mockResolvedValue(undefined);
    vi.mocked(useDeleteReading).mockReturnValue({
      data: undefined,
      error: undefined,
      isMutating: false,
      reset: vi.fn(),
      trigger
    });

    renderHistory({
      date: "2026-08-24",
      readings: [
        {
          registeredAt: "2026-08-24T12:00:00",
          temp: 21.5
        }
      ],
      registeredAt: "2026-08-24T00:00:00",
      resource: "temperature"
    });

    const user = userEvent.setup();
    await user.click(
      await screen.findByRole("button", {
        name: "Delete all for this day"
      })
    );

    await waitFor(() => {
      expect(trigger).toHaveBeenCalledWith({
        dateOnly: true,
        registeredAt: "2026-08-24T00:00:00",
        resource: "temperature"
      });
    });
    expect(await screen.findByText("Readings deleted.")).toBeInTheDocument();
  });

  it("renders date range inputs and deletes for the selection", async () => {
    const trigger = vi.fn().mockResolvedValue(undefined);
    vi.mocked(useDeleteReading).mockReturnValue({
      data: undefined,
      error: undefined,
      isMutating: false,
      reset: vi.fn(),
      trigger
    });

    renderHistory({
      date: "2026-08-22..2026-08-24",
      readings: [
        { registeredAt: "2026-08-22T10:00:00", temp: 20.1 },
        { registeredAt: "2026-08-23T11:00:00", temp: 20.5 },
        { registeredAt: "2026-08-24T12:00:00", temp: 21.0 }
      ],
      registeredAt: "2026-08-24T00:00:00",
      resource: "temperature"
    });

    await screen.findByText("Measurement history");
    const startInput = screen.getByLabelText("Start date") as HTMLInputElement;
    const endInput = screen.getByLabelText("End date") as HTMLInputElement;
    expect(startInput.value).toBe("2026-08-22");
    expect(endInput.value).toBe("2026-08-24");
    expect(screen.getByRole("button", { name: "Delete all for this selection" })).toBeInTheDocument();

    const user = userEvent.setup();
    window.history.replaceState({}, "", "/history?startDate=2026-08-22&endDate=2026-08-24");
    await user.click(screen.getByRole("button", { name: "Delete all for this selection" }));

    await waitFor(() => {
      expect(trigger).toHaveBeenCalledTimes(3);
    });
    expect(await screen.findByText("Readings deleted.")).toBeInTheDocument();
  });
});
