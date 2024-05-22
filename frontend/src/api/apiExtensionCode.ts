import { formatISO } from "date-fns";

export class ApiExtension {
  protected transformOptions = async (options: RequestInit): Promise<RequestInit> => {
    options.credentials = "include";
    options.headers = {
      ...options.headers,
    };
    return Promise.resolve(options);
  };
}

export const overrideStandardDateISOString = () => {
  Date.prototype.toISOString = function () {
    return formatISO(this);
  };
};
