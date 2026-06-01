import type { JSX } from "react";
import { Button, Input, Label, Tab, TabList, TabPanel, Tabs, TextField, ToggleButton } from "react-aria-components";
import { type FilterDraft, type TimeMode } from "../model/historyFilters";

interface HistoryFiltersPanelProps {
  readonly draft: FilterDraft;
  readonly onDraftChange: (nextDraft: FilterDraft) => void;
  readonly onSubmit: () => void;
  readonly isLoading: boolean;
}

const toNumber = (value: string): number | undefined => {
  if (value.trim().length === 0) {
    return undefined;
  }

  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : undefined;
};

const numberValue = (value: number | undefined): string => {
  return value === undefined ? "" : String(value);
};

export const HistoryFiltersPanel = ({
  draft,
  onDraftChange,
  onSubmit,
  isLoading
}: HistoryFiltersPanelProps): JSX.Element => {
  const selectedMetricCount =
    Number(draft.selectedMetrics.battery) +
    Number(draft.selectedMetrics.temperature) +
    Number(draft.selectedMetrics.humidity);
  const hasMetricSelectionError = selectedMetricCount === 0;

  const setTimeMode = (mode: TimeMode): void => {
    onDraftChange({ ...draft, timeMode: mode });
  };

  const handleSubmit = (): void => {
    if (hasMetricSelectionError) {
      return;
    }

    onSubmit();
  };

  return (
    <section className="panel panel-filters" aria-label="History filters">
      <header className="panel-header">
        <h2>Filter historical readings</h2>
        <p>Query a day or exact timestamp and refine the results by text and ranges.</p>
      </header>

      <Tabs
        selectedKey={draft.timeMode}
        onSelectionChange={(key) => {
          setTimeMode(key === "timestamp" ? "timestamp" : "day");
        }}
      >
        <TabList aria-label="Time mode" className="mode-tabs">
          <Tab id="day">Day</Tab>
          <Tab id="timestamp">Timestamp</Tab>
        </TabList>

        <TabPanel id="day">
          <TextField
            className="field"
            value={draft.dayValue}
            onChange={(value) => {
              onDraftChange({ ...draft, dayValue: value });
            }}
          >
            <Label>Registered day</Label>
            <Input type="date" />
          </TextField>
        </TabPanel>

        <TabPanel id="timestamp">
          <TextField
            className="field"
            value={draft.timestampValue}
            onChange={(value) => {
              onDraftChange({ ...draft, timestampValue: value });
            }}
          >
            <Label>Exact registered timestamp</Label>
            <Input type="datetime-local" />
          </TextField>
        </TabPanel>
      </Tabs>

      <div className="filter-grid">
        <TextField
          className="field"
          value={draft.searchText}
          onChange={(value) => {
            onDraftChange({ ...draft, searchText: value });
          }}
        >
          <Label>Text search</Label>
          <Input placeholder="Search by id or timestamp" aria-describedby="search-help" />
        </TextField>

        <p id="search-help" className="field-help">
          Example: use part of an ID or timestamp like 2026-06-01.
        </p>

        <div className="field metric-toggle-group" role="group" aria-label="Metric toggles">
          <Label>Metrics</Label>
          <div className="metric-toggle-list">
            <ToggleButton
              isSelected={draft.selectedMetrics.battery}
              onChange={(isSelected) => {
                onDraftChange({
                  ...draft,
                  selectedMetrics: {
                    ...draft.selectedMetrics,
                    battery: isSelected
                  }
                });
              }}
            >
              Battery
            </ToggleButton>
            <ToggleButton
              isSelected={draft.selectedMetrics.temperature}
              onChange={(isSelected) => {
                onDraftChange({
                  ...draft,
                  selectedMetrics: {
                    ...draft.selectedMetrics,
                    temperature: isSelected
                  }
                });
              }}
            >
              Temperature
            </ToggleButton>
            <ToggleButton
              isSelected={draft.selectedMetrics.humidity}
              onChange={(isSelected) => {
                onDraftChange({
                  ...draft,
                  selectedMetrics: {
                    ...draft.selectedMetrics,
                    humidity: isSelected
                  }
                });
              }}
            >
              Humidity
            </ToggleButton>
          </div>
          <p className="field-help" aria-live="polite" role={hasMetricSelectionError ? "alert" : "status"}>
            {hasMetricSelectionError ? "Select at least one metric before searching." : "Choose one or more metrics."}
          </p>
        </div>
      </div>

      <div className="range-grid">
        <TextField
          className="field"
          value={numberValue(draft.batteryBounds.min)}
          onChange={(value) => {
            const num = toNumber(value);
            onDraftChange({
              ...draft,
              batteryBounds: {
                ...(num !== undefined ? { min: num } : {}),
                ...(draft.batteryBounds.max !== undefined ? { max: draft.batteryBounds.max } : {})
              }
            });
          }}
        >
          <Label>Battery min (%)</Label>
          <Input type="number" inputMode="numeric" min={0} max={100} />
        </TextField>

        <TextField
          className="field"
          value={numberValue(draft.batteryBounds.max)}
          onChange={(value) => {
            const num = toNumber(value);
            onDraftChange({
              ...draft,
              batteryBounds: {
                ...(draft.batteryBounds.min !== undefined ? { min: draft.batteryBounds.min } : {}),
                ...(num !== undefined ? { max: num } : {})
              }
            });
          }}
        >
          <Label>Battery max (%)</Label>
          <Input type="number" inputMode="numeric" min={0} max={100} />
        </TextField>

        <TextField
          className="field"
          value={numberValue(draft.temperatureBounds.min)}
          onChange={(value) => {
            const num = toNumber(value);
            onDraftChange({
              ...draft,
              temperatureBounds: {
                ...(num !== undefined ? { min: num } : {}),
                ...(draft.temperatureBounds.max !== undefined ? { max: draft.temperatureBounds.max } : {})
              }
            });
          }}
        >
          <Label>Temperature min (°C)</Label>
          <Input type="number" inputMode="decimal" step="0.1" min={-55} max={125} />
        </TextField>

        <TextField
          className="field"
          value={numberValue(draft.temperatureBounds.max)}
          onChange={(value) => {
            const num = toNumber(value);
            onDraftChange({
              ...draft,
              temperatureBounds: {
                ...(draft.temperatureBounds.min !== undefined ? { min: draft.temperatureBounds.min } : {}),
                ...(num !== undefined ? { max: num } : {})
              }
            });
          }}
        >
          <Label>Temperature max (°C)</Label>
          <Input type="number" inputMode="decimal" step="0.1" min={-55} max={125} />
        </TextField>

        <TextField
          className="field"
          value={numberValue(draft.humidityBounds.min)}
          onChange={(value) => {
            const num = toNumber(value);
            onDraftChange({
              ...draft,
              humidityBounds: {
                ...(num !== undefined ? { min: num } : {}),
                ...(draft.humidityBounds.max !== undefined ? { max: draft.humidityBounds.max } : {})
              }
            });
          }}
        >
          <Label>Humidity min (%)</Label>
          <Input type="number" inputMode="decimal" step="0.1" min={20} max={90} />
        </TextField>

        <TextField
          className="field"
          value={numberValue(draft.humidityBounds.max)}
          onChange={(value) => {
            const num = toNumber(value);
            onDraftChange({
              ...draft,
              humidityBounds: {
                ...(draft.humidityBounds.min !== undefined ? { min: draft.humidityBounds.min } : {}),
                ...(num !== undefined ? { max: num } : {})
              }
            });
          }}
        >
          <Label>Humidity max (%)</Label>
          <Input type="number" inputMode="decimal" step="0.1" min={20} max={90} />
        </TextField>
      </div>

      <Button className="cta-button" onPress={handleSubmit} isPending={isLoading} isDisabled={hasMetricSelectionError}>
        Search historical values
      </Button>
    </section>
  );
};
