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

    // Invalid id.

    [LoggerMessage(EventId = 10, EventName = "InvalidId", Level = LogLevel.Error,
        Message = "{Id} is invalid id.")]
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

    // Object is null or empty.

    [LoggerMessage(EventId = 30, EventName = "ObjectIsNull", Level = LogLevel.Error,
        Message = "{ObjectIsNull} not found.")]
    internal static partial void LogIsNull(this ILogger logger, string objectIsNull);

    [LoggerMessage(EventId = 31, EventName = "ObjectIsNullOrEmpty", Level = LogLevel.Error,
        Message = "{ObjectIsNullOrEmpty} for {DateTimeOffset} not found.")]
    internal static partial void LogIsNullOrEmpty(this ILogger logger, string? objectIsNullOrEmpty, string dateTimeOffset);

    // GET

    [LoggerMessage(EventId = 41, EventName = "RetrievingDtoById", Level = LogLevel.Information,
        Message = "Retrieving {EntityToGetById} by id.")]
    internal static partial void LogRetrievingDtoById(this ILogger logger, string entityToGetById);

    [LoggerMessage(EventId = 42, EventName = "RetrievedDtoById", Level = LogLevel.Information,
        Message = "Retrieved {EntityToGetById} by id.")]
    internal static partial void LogRetrievedDtoById(this ILogger logger, string entityToGetById);

    [LoggerMessage(EventId = 43, EventName = "RetrievingDtoByTimestamp", Level = LogLevel.Information,
        Message = "Retrieving {EntityToGetByTimestamp} by timestamp.")]
    internal static partial void LogRetrievingDtoByTimestamp(this ILogger logger, string entityToGetByTimestamp);

    [LoggerMessage(EventId = 44, EventName = "RetrievedDtoByTimestamp", Level = LogLevel.Information,
        Message = "Retrieved {EntityToGetByTimestamp} by timestamp.")]
    internal static partial void LogRetrievedDtoByTimestamp(this ILogger logger, string entityToGetByTimestamp);

    [LoggerMessage(EventId = 45, EventName = "RetrievingDtosByDate", Level = LogLevel.Information,
        Message = "Retrieving {EntitiesToGetByDate} by date.")]
    internal static partial void LogRetrievingDtoByDate(this ILogger logger, string entitiesToGetByDate);

    [LoggerMessage(EventId = 46, EventName = "RetrievedDtosByDate", Level = LogLevel.Information,
        Message = "Retrieved {EntitiesToGetByDate} by date.")]
    internal static partial void LogRetrievedDtoByDate(this ILogger logger, string entitiesToGetByDate);

    // POST

    [LoggerMessage(EventId = 51, EventName = "CreatingDto", Level = LogLevel.Information,
        Message = "Creating {EntityToCreate} for database.")]
    internal static partial void LogCreating(this ILogger logger, string entityToCreate);

    [LoggerMessage(EventId = 52, EventName = "CreatedDto", Level = LogLevel.Information,
        Message = "Created {EntityToCreate} for database.")]
    internal static partial void LogCreated(this ILogger logger, string entityToCreate);

    // DELETE

    [LoggerMessage(EventId = 61, EventName = "DeletingDtoById", Level = LogLevel.Information,
        Message = "Deleting {EntityToDeleteById} from database by id.")]
    internal static partial void LogDeletingById(this ILogger logger, string entityToDeleteById);

    [LoggerMessage(EventId = 62, EventName = "DeletedDto", Level = LogLevel.Information,
        Message = "Deleted {EntityToDeleteById} from database by id.")]
    internal static partial void LogDeletedById(this ILogger logger, string entityToDeleteById);

    [LoggerMessage(EventId = 63, EventName = "DeletingDtoByTimestamp", Level = LogLevel.Information,
        Message = "Deleting {EntityToDeleteByTimestamp} from database by timestamp.")]
    internal static partial void LogDeletingByTimestamp(this ILogger logger, string entityToDeleteByTimestamp);

    [LoggerMessage(EventId = 64, EventName = "DeletedDtoByTimestamp", Level = LogLevel.Information,
        Message = "Deleted {EntityToDeleteByTimestamp} from database by timestamp.")]
    internal static partial void LogDeletedByTimestamp(this ILogger logger, string entityToDeleteByTimestamp);
}
