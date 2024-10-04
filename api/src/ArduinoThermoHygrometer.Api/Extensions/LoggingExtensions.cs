using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Extensions;

internal static partial class LoggingExtensions
{
    // GLOBAL EXCEPTION HANDLER

    [LoggerMessage(EventId = 5, EventName = "UnexpectedServerError", Level = LogLevel.Error,
    Message = "An unexpected server error has occurred. Exception: {ExceptionMessage}\n InnerException: {InnerExceptionMessage}")]
    internal static partial void LogUnexpectedException(this ILogger logger, string exceptionMessage, string? innerExceptionMessage);

    // RATE LIMIT SERVICE

    [LoggerMessage(EventId = 1003, EventName = "RateLimit", Level = LogLevel.Warning,
    Message = "Rate limit reached for {RequestMethod} request to {RequestPath}. Please try again after {RetryRequestAfter} minute.")]
    internal static partial void LogRateLimitExceeded(this ILogger logger, string requestMethod, string requestPath, string retryRequestAfter);

    // HEALTHCHECK SERVICE

    [LoggerMessage(EventId = 2002, EventName = "RetrievingHealthCheckReport", Level = LogLevel.Information,
    Message = "Retrieving health check report.")]
    internal static partial void LogRetrievingHealthCheckReport(this ILogger logger);

    [LoggerMessage(EventId = 2002, EventName = "RetrievedHealthCheckReport", Level = LogLevel.Information,
    Message = "Retrived health check report.")]
    internal static partial void LogRetrievedHealthCheckReport(this ILogger logger);

    [LoggerMessage(EventId = 2004, EventName = "HealthCheckStatusUnhealthyOrDegraded", Level = LogLevel.Error,
    Message = "Health check report status is {Status}.")]
    internal static partial void LogUnhealthyOrDegradedHealthCheckReportStatus(this ILogger logger, HealthStatus status);

    // GUID ID

    [LoggerMessage(EventId = 3004, EventName = "EmptyId", Level = LogLevel.Error,
    Message = "The {Id} is empty.")]
    internal static partial void LogEmptyId(this ILogger logger, Guid id);

    [LoggerMessage(EventId = 3004, EventName = "InvalidId", Level = LogLevel.Error,
    Message = "The {Id} is not valid.")]
    internal static partial void LogInvalidId(this ILogger logger, Guid id);

    // NULL OBJECT

    [LoggerMessage(EventId = 4004, EventName = "BatteryIsNull", Level = LogLevel.Error,
    Message = "Battery not found.")]
    internal static partial void LogBatteryIsNull(this ILogger logger);

    // BATTERY SERVICE

    [LoggerMessage(EventId = 5002, EventName = "RetrievingBatteryServiceById", Level = LogLevel.Information,
    Message = "Retrieving battery dto by id.")]
    internal static partial void LogRetrievingBatteryDtoById(this ILogger logger);

    [LoggerMessage(EventId = 5002, EventName = "RetrievedBatteryServiceById", Level = LogLevel.Information,
    Message = "Retrieved battery dto by id.")]
    internal static partial void LogRetrievedBatteryDtoById(this ILogger logger);

    [LoggerMessage(EventId = 5002, EventName = "RetrievingBatteryServiceByTimestamp", Level = LogLevel.Information,
    Message = "Retrieving battery dto by timestamp.")]
    internal static partial void LogRetrievingBatteryDtoByTimestamp(this ILogger logger);

    [LoggerMessage(EventId = 5002, EventName = "RetrievedBatteryServiceByTimestamp", Level = LogLevel.Information,
    Message = "Retrieved battery dto by timestamp.")]
    internal static partial void LogRetrievedBatteryDtoByTimestamp(this ILogger logger);
}
