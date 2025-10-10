using Microsoft.Extensions.Diagnostics.HealthChecks;
using Microsoft.Extensions.Logging;

namespace ArduinoThermoHygrometer.Core.Logging;

public static partial class LoggingExtensions
{
    // Global exception handler.

    [LoggerMessage(EventId = 1, EventName = "UnexpectedServerError", Level = LogLevel.Error,
        Message = "An unexpected server error has occurred. Exception: {ExceptionMessage}\n InnerException: {InnerExceptionMessage}")]
    public static partial void LogUnexpectedException(this ILogger logger, string exceptionMessage, string? innerExceptionMessage);

    // Rate limit service.

    [LoggerMessage(EventId = 2, EventName = "RateLimit", Level = LogLevel.Warning,
        Message = "Rate limit reached for {RequestMethod} request to {RequestPath}. Please try again after {RetryRequestAfter} minute(s).")]
    public static partial void LogRateLimitExceeded(this ILogger logger, string requestMethod, string requestPath, string retryRequestAfter);

    // Invalid id.

    [LoggerMessage(EventId = 10, EventName = "InvalidId", Level = LogLevel.Error,
        Message = "{Id} is invalid id.")]
    public static partial void LogInvalidId(this ILogger logger, Guid id);

    // Object is null or empty.

    [LoggerMessage(EventId = 11, EventName = "ObjectIsNull", Level = LogLevel.Error,
        Message = "{ObjectIsNull} not found.")]
    public static partial void LogIsNull(this ILogger logger, string objectIsNull);

    [LoggerMessage(EventId = 12, EventName = "ObjectIsNullOrEmpty", Level = LogLevel.Error,
        Message = "{ObjectIsNullOrEmpty} for {DateTimeOffset} not found.")]
    public static partial void LogIsNullOrEmpty(this ILogger logger, string? objectIsNullOrEmpty, string dateTimeOffset);

    // Returned object

    [LoggerMessage(EventId = 20, EventName = "LogDtoObjectToReturn", Level = LogLevel.Information,
        Message = "{LogDtoObjectToReturn} with Id={Id} and RegisteredAt={DateTimeOffset} is found.")]
    public static partial void LogDtoObjectToReturn(this ILogger logger, string? LogDtoObjectToReturn, Guid? id, string dateTimeOffset);

    [LoggerMessage(EventId = 21, EventName = "LogDtoObjectToCreate", Level = LogLevel.Information,
        Message = "{LogDtoObjectToCreate} with Id={Id} and RegisteredAt={DateTimeOffset} is created.")]
    public static partial void LogDtoObjectToCreate(this ILogger logger, string? LogDtoObjectToCreate, Guid? id, string dateTimeOffset);

    [LoggerMessage(EventId = 22, EventName = "LogDtoObjectToDelete", Level = LogLevel.Information,
        Message = "{LogDtoObjectToDelete} with Id={Id} and RegisteredAt={DateTimeOffset} is deleted.")]
    public static partial void LogDtoObjectToDelete(this ILogger logger, string? LogDtoObjectToDelete, Guid? id, string dateTimeOffset);

    [LoggerMessage(EventId = 23, EventName = "LogDtoObjectAlreadyExists", Level = LogLevel.Information,
        Message = "{ExceptionMessage}, {LogDtoObjectAlreadyExists} with Id={Id} already exists.")]
    public static partial void LogDtoObjectAlreadyExists(this ILogger logger, string? ExceptionMessage, string? LogDtoObjectAlreadyExists, Guid? id);

    // Healthcheck service.

    [LoggerMessage(EventId = 30, EventName = "HealthCheckStatusDegradedOrUnhealthy", Level = LogLevel.Error,
        Message = "Healthcheck report status is {Status}.")]
    public static partial void LogHealthCheckStatusDegradedOrUnhealthy(this ILogger logger, HealthStatus status);
}
