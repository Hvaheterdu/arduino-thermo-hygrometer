import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { createMemoryRouter, RouterProvider } from "react-router";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { useDeleteReading } from "../../hooks";
import { HistoryPage } from "./History.page";

vi.mock("../../hooks", () => ({
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
    } as any);
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
    } as any);

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
});
