import type { ResourceKind } from "@/types/measurement.types";

export const ROUTES = {
  DASHBOARD: "/",
  HISTORY: "/history/:resource"
} as const;

export const buildHistoryPath = (resource: ResourceKind): string => `/history/${resource}`;

export const RESOURCE_KINDS: ResourceKind[] = ["battery", "humidity", "temperature"];

export const isResourceKind = (value: string | undefined): value is ResourceKind =>
  RESOURCE_KINDS.includes(value as ResourceKind);
