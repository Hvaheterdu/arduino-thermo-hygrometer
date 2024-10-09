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

    // GET - Battery service.

    [LoggerMessage(EventId = 41, EventName = "RetrievingBatteryDtoById", Level = LogLevel.Information,
        Message = "Retrieving battery dto by id.")]
    internal static partial void LogRetrievingBatteryDtoById(this ILogger logger);

    [LoggerMessage(EventId = 42, EventName = "RetrievedBatteryDtoById", Level = LogLevel.Information,
        Message = "Retrieved battery dto by id.")]
    internal static partial void LogRetrievedBatteryDtoById(this ILogger logger);

    [LoggerMessage(EventId = 51, EventName = "RetrievingBatteryDtoByTimestamp", Level = LogLevel.Information,
        Message = "Retrieving battery dto by timestamp.")]
    internal static partial void LogRetrievingBatteryDtoByTimestamp(this ILogger logger);

    [LoggerMessage(EventId = 52, EventName = "RetrievedBatteryDtoByTimestamp", Level = LogLevel.Information,
        Message = "Retrieved battery dto by timestamp.")]
    internal static partial void LogRetrievedBatteryDtoByTimestamp(this ILogger logger);

    [LoggerMessage(EventId = 61, EventName = "RetrievingBatteryDtosByDate", Level = LogLevel.Information,
    Message = "Retrieving battery dto by date.")]
    internal static partial void LogRetrievingBatteryDtoByDate(this ILogger logger);

    [LoggerMessage(EventId = 62, EventName = "RetrievedBatteryDtosByDate", Level = LogLevel.Information,
        Message = "Retrieved battery dto by date.")]
    internal static partial void LogRetrievedBatteryDtoByDate(this ILogger logger);

    // POST - Battery service.

    [LoggerMessage(EventId = 71, EventName = "CreatingBattery", Level = LogLevel.Information,
        Message = "Creating battery dto for database.")]
    internal static partial void LogCreatingBattery(this ILogger logger);

    [LoggerMessage(EventId = 72, EventName = "CreatedBattery", Level = LogLevel.Information,
    Message = "Created battery dto for database.")]
    internal static partial void LogCreatedBattery(this ILogger logger);

    // DELETE - Battery service.

    [LoggerMessage(EventId = 81, EventName = "DeletingBattery", Level = LogLevel.Information,
    Message = "Deleting battery dto from database.")]
    internal static partial void LogDeletingBattery(this ILogger logger);

    [LoggerMessage(EventId = 82, EventName = "DeletedBattery", Level = LogLevel.Information,
    Message = "Deleted battery dto from database.")]
    internal static partial void LogDeletedBattery(this ILogger logger);
}
