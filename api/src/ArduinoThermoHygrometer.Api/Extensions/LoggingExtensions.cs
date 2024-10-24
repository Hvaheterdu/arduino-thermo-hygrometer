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

    // Object is null or empty.

    [LoggerMessage(EventId = 11, EventName = "ObjectIsNull", Level = LogLevel.Error,
        Message = "{ObjectIsNull} not found.")]
    internal static partial void LogIsNull(this ILogger logger, string objectIsNull);

    [LoggerMessage(EventId = 12, EventName = "ObjectIsNullOrEmpty", Level = LogLevel.Error,
        Message = "{ObjectIsNullOrEmpty} for {DateTimeOffset} not found.")]
    internal static partial void LogIsNullOrEmpty(this ILogger logger, string? objectIsNullOrEmpty, string dateTimeOffset);

    // Healthcheck service.

    [LoggerMessage(EventId = 20, EventName = "HealthCheckStatusDegradedOrUnhealthy", Level = LogLevel.Error,
        Message = "Healthcheck report status is {Status}.")]
    internal static partial void LogHealthCheckStatusDegradedOrUnhealthy(this ILogger logger, HealthStatus status);
}
