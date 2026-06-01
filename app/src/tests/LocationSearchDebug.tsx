import type { JSX } from "react";
import { useLocation } from "react-router";

export const LocationSearchDebug = (): JSX.Element => {
  const location = useLocation();
  return <output data-testid="location-search">{location.search}</output>;
};
