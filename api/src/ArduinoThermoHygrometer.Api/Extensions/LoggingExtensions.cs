using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Extensions;
internal static partial class LoggingExtensions
{
    // GLOBAL EXCEPTION HANDLER LOGGING

    [LoggerMessage(EventId = 4, EventName = "LogUnexpectedException", Level = LogLevel.Error,
    Message = "An unexpected server error has occurred. Exception: {ExceptionMessage}\n InnerException: {InnerExceptionMessage}")]
    internal static partial void LogUnexpectedException(this ILogger logger, string exceptionMessage, string? innerExceptionMessage);

    // RATE LIMIT SERVICE LOGGING

    [LoggerMessage(EventId = 1003, EventName = "LogRateLimitExceeded", Level = LogLevel.Warning,
    Message = "Rate limit reached for {RequestMethod} request to {RequestPath}. Please try again after {RetryRequestAfter} minute.")]
    internal static partial void LogRateLimitExceeded(this ILogger logger, string requestMethod, string requestPath, string retryRequestAfter);

    // HEALTHCHECK SERVICE LOGGING

    [LoggerMessage(EventId = 2002, EventName = "LogHealthCheckReportRetrieving", Level = LogLevel.Information,
    Message = "Retrieving health check report.")]
    internal static partial void LogHealthCheckReportRetrieving(this ILogger logger);

    [LoggerMessage(EventId = 2002, EventName = "LogHealthCheckReportRetrived", Level = LogLevel.Information,
    Message = "Retrived health check report.")]
    internal static partial void LogHealthCheckReportRetrieved(this ILogger logger);

    [LoggerMessage(EventId = 2004, EventName = "LogHealthCheckReportStatus", Level = LogLevel.Error,
    Message = "Health check report status is {Status}.")]
    internal static partial void LogHealthCheckReportStatus(this ILogger logger, HealthStatus status);
}
