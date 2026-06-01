import type { JSX } from "react";
import type { DailyTrendPoint } from "../model/insightTypes";

interface MetricTrendChartProps {
  readonly title: string;
  readonly unit: string;
  readonly points: readonly DailyTrendPoint[];
  readonly getValue: (point: DailyTrendPoint) => number | undefined;
  readonly lineClassName: string;
}

interface ChartPoint {
  readonly x: number;
  readonly y: number;
  readonly label: string;
  readonly value: number;
}

const viewBoxWidth = 640;
const viewBoxHeight = 220;
const margin = {
  top: 18,
  right: 16,
  bottom: 36,
  left: 44
};

const formatValue = (value: number, unit: string): string => {
  return `${value.toFixed(2)} ${unit}`;
};

const createChartPoints = (
  points: readonly DailyTrendPoint[],
  getValue: (point: DailyTrendPoint) => number | undefined
): ChartPoint[] => {
  const values = points.map(getValue).filter((value): value is number => value !== undefined);

  if (values.length === 0) {
    return [];
  }

  const min = Math.min(...values);
  const max = Math.max(...values);
  const hasRange = max - min > 0;
  const paddedMin = hasRange ? min : min - 1;
  const paddedMax = hasRange ? max : max + 1;

  const plotWidth = viewBoxWidth - margin.left - margin.right;
  const plotHeight = viewBoxHeight - margin.top - margin.bottom;
  const denominator = Math.max(points.length - 1, 1);

  return points.flatMap((point, index) => {
    const value = getValue(point);
    if (value === undefined) {
      return [];
    }

    const x = margin.left + (plotWidth * index) / denominator;
    const normalized = (value - paddedMin) / (paddedMax - paddedMin);
    const y = margin.top + (1 - normalized) * plotHeight;

    return [
      {
        x,
        y,
        label: point.dayLabel,
        value
      }
    ];
  });
};

const createPath = (points: readonly ChartPoint[]): string => {
  if (points.length === 0) {
    return "";
  }

  return points
    .map((point, index) => {
      const command = index === 0 ? "M" : "L";
      return `${command} ${point.x.toFixed(2)} ${point.y.toFixed(2)}`;
    })
    .join(" ");
};

export const MetricTrendChart = ({
  title,
  unit,
  points,
  getValue,
  lineClassName
}: MetricTrendChartProps): JSX.Element => {
  const chartPoints = createChartPoints(points, getValue);

  if (chartPoints.length === 0) {
    return (
      <article className="metric-chart-card" aria-label={`${title} chart`}>
        <h3>{title}</h3>
        <p className="field-help">No trend data in the current window.</p>
      </article>
    );
  }

  const path = createPath(chartPoints);

  return (
    <article className="metric-chart-card" aria-label={`${title} chart`}>
      <h3>{title}</h3>
      <svg className="metric-chart-svg" viewBox={`0 0 ${String(viewBoxWidth)} ${String(viewBoxHeight)}`} role="img">
        <title>{`${title} daily trend chart`}</title>
        <desc>{`Line chart of ${title.toLowerCase()} daily averages in ${unit}.`}</desc>

        <line
          className="metric-chart-axis"
          x1={margin.left}
          y1={margin.top}
          x2={margin.left}
          y2={viewBoxHeight - margin.bottom}
        />
        <line
          className="metric-chart-axis"
          x1={margin.left}
          y1={viewBoxHeight - margin.bottom}
          x2={viewBoxWidth - margin.right}
          y2={viewBoxHeight - margin.bottom}
        />

        <path className={`metric-chart-path ${lineClassName}`} d={path} />

        {chartPoints.map((point) => {
          return (
            <g key={`${point.label}-${String(point.x)}`}>
              <circle className={`metric-chart-dot ${lineClassName}`} cx={point.x} cy={point.y} r="4" />
              <title>{`${point.label}: ${formatValue(point.value, unit)}`}</title>
            </g>
          );
        })}

        {points.map((point, index) => {
          const denominator = Math.max(points.length - 1, 1);
          const plotWidth = viewBoxWidth - margin.left - margin.right;
          const x = margin.left + (plotWidth * index) / denominator;

          return (
            <text key={point.dayLabel} className="metric-chart-label" x={x} y={viewBoxHeight - 14} textAnchor="middle">
              {point.dayLabel}
            </text>
          );
        })}
      </svg>
    </article>
  );
};
