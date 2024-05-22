import { useContext } from "react";
import { ApiClientContext } from "./apiClientContext";

export const useApiClient = () => {
  const context = useContext(ApiClientContext);

  if (!context) {
    throw new Error("ApiClientContext must be provided before use");
  }

  return context;
};
