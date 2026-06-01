export type LogLevel = "debug" | "info" | "warn" | "error";

export interface FrontendLogEntry {
  readonly timestampIso: string;
  readonly level: LogLevel;
  readonly message: string;
  readonly context?: Record<string, unknown>;
}

const maxStoredLogEntries = 200;

const inMemoryLogEntries: FrontendLogEntry[] = [];

const appendLogEntry = (logEntry: FrontendLogEntry): void => {
  inMemoryLogEntries.push(logEntry);

  if (inMemoryLogEntries.length > maxStoredLogEntries) {
    inMemoryLogEntries.shift();
  }
};

const dispatchLogEvent = (logEntry: FrontendLogEntry): void => {
  if (typeof window === "undefined") {
    return;
  }

  window.dispatchEvent(new CustomEvent<FrontendLogEntry>("frontend-log-entry", { detail: logEntry }));
};

const createLogEntry = (
  level: LogLevel,
  message: string,
  context: Record<string, unknown> | undefined
): FrontendLogEntry => {
  return {
    timestampIso: new Date().toISOString(),
    level,
    message,
    ...(context !== undefined ? { context } : {})
  };
};

export const frontendLogger = {
  debug(message: string, context?: Record<string, unknown>): void {
    const logEntry = createLogEntry("debug", message, context);
    appendLogEntry(logEntry);
    dispatchLogEvent(logEntry);
  },
  info(message: string, context?: Record<string, unknown>): void {
    const logEntry = createLogEntry("info", message, context);
    appendLogEntry(logEntry);
    dispatchLogEvent(logEntry);
  },
  warn(message: string, context?: Record<string, unknown>): void {
    const logEntry = createLogEntry("warn", message, context);
    appendLogEntry(logEntry);
    dispatchLogEvent(logEntry);
  },
  error(message: string, context?: Record<string, unknown>): void {
    const logEntry = createLogEntry("error", message, context);
    appendLogEntry(logEntry);
    dispatchLogEvent(logEntry);
  },
  getEntries(): readonly FrontendLogEntry[] {
    return inMemoryLogEntries;
  }
};
