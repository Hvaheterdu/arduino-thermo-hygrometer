import AxeBuilder from "@axe-core/playwright";
import { expect, test } from "@playwright/test";

test.describe("frontend smoke tests", () => {
  test("dashboard shell and navigation are rendered", async ({ page }) => {
    await page.goto("");

    await expect(page.getByRole("heading", { name: "Historical Climate Telemetry" })).toBeVisible();
    await expect(page.getByRole("link", { name: "Trends" })).toBeVisible();
    await expect(page.getByRole("link", { name: "Sensor Health" })).toBeVisible();
  });

  test("trends metric visibility toggles are reflected in URL", async ({ page }) => {
    await page.goto("trends");
    await expect(page.getByRole("heading", { name: "Telemetry Trends" })).toBeVisible();

    await page.getByRole("button", { name: "Battery" }).click();
    await expect(page).toHaveURL(/\/arduinothermohygrometer\/trends\?b=0$/);
    await expect(page.getByRole("heading", { name: "Battery Direction" })).toHaveCount(0);

    await page.reload();
    await expect(page).toHaveURL(/\/arduinothermohygrometer\/trends\?b=0$/);
    await expect(page.getByRole("heading", { name: "Battery Direction" })).toHaveCount(0);
  });
});

test.describe("frontend accessibility smoke checks", () => {
  test("dashboard has no critical accessibility violations", async ({ page }) => {
    await page.goto("");
    await expect(page.getByRole("heading", { name: "Historical Climate Telemetry" })).toBeVisible();

    const dashboardA11yResults = await new AxeBuilder({ page }).analyze();

    const criticalViolations = dashboardA11yResults.violations.filter((violation) => violation.impact === "critical");
    expect(criticalViolations).toEqual([]);
  });

  test("trends has no critical accessibility violations", async ({ page }) => {
    await page.goto("trends");
    await expect(page.getByRole("heading", { name: "Telemetry Trends" })).toBeVisible();

    const trendsA11yResults = await new AxeBuilder({ page }).analyze();

    const criticalViolations = trendsA11yResults.violations.filter((violation) => violation.impact === "critical");
    expect(criticalViolations).toEqual([]);
  });
});
