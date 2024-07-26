using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace ArduinoThermoHygrometer.Web.Middleware;

public class GlobalExceptionHandlingMiddleware : IExceptionHandler
{
    private readonly ILogger<GlobalExceptionHandlingMiddleware> _logger;

    public GlobalExceptionHandlingMiddleware(ILogger<GlobalExceptionHandlingMiddleware> logger)
    {
        _logger = logger;
    }

    /// <summary>
    /// Tries to handle the exception asynchronously.
    /// </summary>
    /// <param name="httpContext">The HttpContext object.</param>
    /// <param name="exception">The exception that occurred.</param>
    /// <param name="cancellationToken">The cancellation token.</param>
    /// <returns>A task representing the asynchronous operation. The task result indicates whether the exception was handled successfully.</returns>
    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception, CancellationToken cancellationToken)
    {
        CreateLogsForExceptions(httpContext, exception);

        ProblemDetails problemDetails = new()
        {
            Type = GetRequestUri(httpContext),
            Title = "Server Error.",
            Detail = "An unhandled server error has occurred while executing the request.",
            Status = StatusCodes.Status500InternalServerError,
            Extensions =
            {
                ["errors"] = exception.Message,
                ["innerException"] = exception.InnerException?.Message,
                ["traceId"] = Activity.Current?.Id ?? httpContext.TraceIdentifier,
            },
        };

        httpContext.Response.StatusCode = StatusCodes.Status500InternalServerError;
        string contentType = "application/problem+json";

        JsonSerializerOptions jsonOptions = new()
        {
            WriteIndented = true,
            Converters = { new JsonStringEnumConverter() },
        };

        await httpContext.Response.WriteAsJsonAsync(problemDetails, jsonOptions, contentType, cancellationToken);

        return true;
    }

    /// <summary>
    /// Creates logs for exceptions.
    /// </summary>
    /// <param name="context">The HttpContext object.</param>
    /// <param name="exception">The exception that occurred.</param>
    private void CreateLogsForExceptions(HttpContext context, Exception exception)
    {
        _logger.LogError(
            exception,
            "{requestPath}\n      Exception: {Message}\n      InnerException: {Message}",
            GetRequestUriWithRequestMethod(context),
            exception.Message,
            exception.InnerException?.Message);
    }

    /// <summary>
    /// Gets the request URI with the request method from the provided HttpContext.
    /// </summary>
    /// <param name="httpContext">The HttpContext object.</param>
    /// <returns>The request URI with the request method.</returns>
    private static string GetRequestUriWithRequestMethod(HttpContext httpContext)
    {
        return $"{httpContext.Request.Method.ToUpperInvariant()} {httpContext?.Request?.Path}";
    }

    /// <summary>
    /// Gets the request URI from the provided HttpContext.
    /// </summary>
    /// <param name="httpContext">The HttpContext object.</param>
    /// <returns>The request URI.</returns>
    private static string GetRequestUri(HttpContext httpContext)
    {
        return $"{httpContext?.Request?.Path}";
    }
}
