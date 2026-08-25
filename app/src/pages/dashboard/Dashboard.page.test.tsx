import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { render, screen } from "@testing-library/react";
import { RouterProvider, createMemoryRouter } from "react-router";
import { describe, expect, it } from "vitest";

import { DashboardPage } from "./Dashboard.page";

const data = {
    battery: [
      {
        batteryStatus: 75,
        registeredAt: "2026-08-24T11:00:00"
      }
    ],
    date: "2026-08-24",
    humidity: [
      {
        airHumidity: 54.2,
        registeredAt: "2026-08-24T11:00:00"
      }
    ],
    registeredAt: "2026-08-24T00:00:00",
    temperature: [
      {
        registeredAt: "2026-08-24T12:00:00",
        temp: 21.7
      }
    ]
  },
  renderDashboard = (loaderData = data) => {
    const router = createMemoryRouter(
      [
        {
          Component: DashboardPage,
          HydrateFallback: () => null,
          loader: () => loaderData,
          path: "/"
        }
      ],
      {
        initialEntries: ["/"]
      }
    );

    render(
      <ChakraProvider value={defaultSystem}>
        <RouterProvider router={router} />
      </ChakraProvider>
    );

    return router;
  };

describe("DashboardPage", () => {
  it("shows the latest reading for each sensor", async () => {
    renderDashboard();

    expect(
      await screen.findByRole("heading", {
        name: "Outside conditions"
      })
    ).toBeInTheDocument();
    expect(await screen.findByText("21.7 °C")).toBeInTheDocument();
    expect(await screen.findByText("54.2 %")).toBeInTheDocument();
    expect(await screen.findByText("75 %")).toBeInTheDocument();
    expect(await screen.findByText(/Most recent device reading/)).toBeInTheDocument();
  });

  it("shows an empty state when there are no readings", async () => {
    renderDashboard({
      ...data,
      battery: [],
      humidity: [],
      temperature: []
    });

    expect(await screen.findByText("No measurements yet")).toBeInTheDocument();
    expect(screen.getByText("There are no measurements recorded for this date.")).toBeInTheDocument();
  });
});
