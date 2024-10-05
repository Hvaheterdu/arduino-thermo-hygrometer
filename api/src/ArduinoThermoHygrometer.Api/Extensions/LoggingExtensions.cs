using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Extensions;

internal static partial class LoggingExtensions
{
    // Global exception handler.

    [LoggerMessage(EventId = 1, EventName = "UnexpectedServerError", Level = LogLevel.Error,
        Message = "An unexpected server error has occurred. Exception: {ExceptionMessage}\n InnerException: {InnerExceptionMessage}")]
    internal static partial void LogUnexpectedException(this ILogger logger, string exceptionMessage, string? innerExceptionMessage);

    // Rate limit service.

    [LoggerMessage(EventId = 2, EventName = "RateLimit", Level = LogLevel.Warning,
        Message = "Rate limit reached for {RequestMethod} request to {RequestPath}. Please try again after {RetryRequestAfter} minute(s).")]
    internal static partial void LogRateLimitExceeded(this ILogger logger, string requestMethod, string requestPath, string retryRequestAfter);

    // Guid Id.

    [LoggerMessage(EventId = 10, EventName = "InvalidId", Level = LogLevel.Error,
        Message = "{Id} is invalid.")]
    internal static partial void LogInvalidId(this ILogger logger, Guid id);

    // Healthcheck service.

    [LoggerMessage(EventId = 21, EventName = "RetrievingHealthCheckReport", Level = LogLevel.Information,
        Message = "Retrieving healthcheck report.")]
    internal static partial void LogRetrievingHealthCheckReport(this ILogger logger);

    [LoggerMessage(EventId = 22, EventName = "RetrievedHealthCheckReport", Level = LogLevel.Information,
        Message = "Retrived healthcheck report.")]
    internal static partial void LogRetrievedHealthCheckReport(this ILogger logger);

    [LoggerMessage(EventId = 23, EventName = "HealthCheckStatusUnhealthyOrDegraded", Level = LogLevel.Error,
        Message = "Healthcheck report status is {Status}.")]
    internal static partial void LogHealthCheckReportStatus(this ILogger logger, HealthStatus status);

    // Battery null.

    [LoggerMessage(EventId = 30, EventName = "BatteryIsNull", Level = LogLevel.Error,
        Message = "Battery not found.")]
    internal static partial void LogBatteryIsNull(this ILogger logger);

    // Battery service.

    [LoggerMessage(EventId = 31, EventName = "RetrievingBatteryDtoById", Level = LogLevel.Information,
        Message = "Retrieving battery dto by id.")]
    internal static partial void LogRetrievingBatteryDtoById(this ILogger logger);

    [LoggerMessage(EventId = 32, EventName = "RetrievedBatteryDtoById", Level = LogLevel.Information,
        Message = "Retrieved battery dto by id.")]
    internal static partial void LogRetrievedBatteryDtoById(this ILogger logger);

    [LoggerMessage(EventId = 41, EventName = "RetrievingBatteryDtoByTimestamp", Level = LogLevel.Information,
        Message = "Retrieving battery dto by timestamp")]
    internal static partial void LogRetrievingBatteryDtoByTimestamp(this ILogger logger);

    [LoggerMessage(EventId = 42, EventName = "RetrievedBatteryDtoByTimestamp", Level = LogLevel.Information,
        Message = "Retrieved battery dto by timestamp")]
    internal static partial void LogRetrievedBatteryDtoByTimestamp(this ILogger logger);

    [LoggerMessage(EventId = 51, EventName = "RetrievingBatteryDtosByDate", Level = LogLevel.Information,
    Message = "Retrieving battery dto by date")]
    internal static partial void LogRetrievingBatteryDtoByDate(this ILogger logger);

    [LoggerMessage(EventId = 52, EventName = "RetrievedBatteryDtosByDate", Level = LogLevel.Information,
        Message = "Retrieved battery dto by date")]
    internal static partial void LogRetrievedBatteryDtoByDate(this ILogger logger);
}
