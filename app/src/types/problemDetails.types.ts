import type { components } from "@/arduino-thermo-hygrometer-api";

export type ProblemDetailsDto = components["schemas"]["ProblemDetailsDto"];

export type ProblemDetailsValidationErrorDto = components["schemas"]["ProblemDetailsValidationErrorDto"];

export type ApiError = {
  status: number;
  title: string;
  detail: string;
  errors: ProblemDetailsValidationErrorDto[];
};
