namespace ArduinoThermoHygrometer.Web.Extensions;

public static partial class LoggingExtensions
{
    [LoggerMessage(EventId = 0, EventName = "LogTrace", Level = LogLevel.Trace, Message = "{Message}")]
    public static partial void LoggingTrace(this ILogger logger, string Message);

    [LoggerMessage(EventId = 1, EventName = "LogDebug", Level = LogLevel.Debug, Message = "{Message}")]
    public static partial void LoggingDebug(this ILogger logger, string Message);

    [LoggerMessage(EventId = 2, EventName = "LogInformation", Level = LogLevel.Information, Message = "{Message}")]
    public static partial void LoggingInformation(this ILogger logger, string Message);

    [LoggerMessage(EventId = 3, EventName = "LogWarning", Level = LogLevel.Warning, Message = "{Message}")]
    public static partial void LoggingWarning(this ILogger logger, string Message);

    [LoggerMessage(EventId = 4, EventName = "LogError", Level = LogLevel.Error, Message = "{Message}")]
    public static partial void LoggingError(this ILogger logger, string Message);

    [LoggerMessage(EventId = 5, EventName = "LogCritical", Level = LogLevel.Critical, Message = "{Message}")]
    public static partial void LoggingCritical(this ILogger logger, string Message);
}
