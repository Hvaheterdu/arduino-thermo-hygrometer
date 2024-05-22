import React, { createContext, useState } from "react";
import { appConfiguration } from "../config/appConfig";
import {
  AdminClient,
  AttendanceClient,
  AttendanceReportClient,
  EventClient,
  EventOverviewClient,
  SendEmailClient,
  UserClient,
} from "./api.generated";

export interface ApiClientContextProps {
  emailClient: SendEmailClient;
  eventOverviewClient: EventOverviewClient;
  eventClient: EventClient;
  attendanceClient: AttendanceClient;
  userClient: UserClient;
  adminClient: AdminClient;
  attendanceReportClient: AttendanceReportClient;
}

export const ApiClientContext = createContext<ApiClientContextProps>(undefined as never);
ApiClientContext.displayName = "ApiClientContext";

interface ApiClientProviderProps {
  children: React.ReactNode;
}

const ApiClientProviderComponent = ({ children }: ApiClientProviderProps) => {
  const apiBaseURL = appConfiguration.APIBaseURL;

  // TODO: Replace with own client classes when backend is ready. Same with imports.
  const [emailClient] = useState(() => new SendEmailClient(apiBaseURL));
  const [attendanceClient] = useState(() => new AttendanceClient(apiBaseURL));
  const [userClient] = useState(() => new UserClient(apiBaseURL));
  const [eventOverviewClient] = useState(() => new EventOverviewClient(apiBaseURL));
  const [eventClient] = useState(() => new EventClient(apiBaseURL));
  const [adminClient] = useState(() => new AdminClient(apiBaseURL));
  const [attendanceReportClient] = useState(() => new AttendanceReportClient(apiBaseURL));

  return (
    <ApiClientContext.Provider
      value={{
        emailClient,
        attendanceClient,
        eventOverviewClient,
        eventClient,
        userClient,
        adminClient,
        attendanceReportClient,
      }}
    >
      {children}
    </ApiClientContext.Provider>
  );
};

export const ApiClientProvider = React.memo(ApiClientProviderComponent);
