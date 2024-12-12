using System.Diagnostics;
using System.Net;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Text.RegularExpressions;
using ArduinoThermoHygrometer.Core.Logging;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Api.Middleware;

public class GlobalExceptionHandlerMiddleware : IExceptionHandler
{
    private readonly ILogger<GlobalExceptionHandlerMiddleware> _logger;

    public GlobalExceptionHandlerMiddleware(ILogger<GlobalExceptionHandlerMiddleware> logger)
    {
        _logger = logger;
    }

    /// <summary>
    /// Attempts to handle an exception by creating logs and writing a ProblemDetails response.
    /// </summary>
    /// <param name="httpContext">The current HttpContext.</param>
    /// <param name="exception">The exception that occurred.</param>
    /// <param name="cancellationToken">A CancellationToken to observe while waiting for the task to complete.</param>
    /// <returns>A ValueTask of bool indicating whether the exception was handled. Always returns true.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="exception"/> is null.</exception>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="httpContext"/> is null.</exception>
    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception, CancellationToken cancellationToken)
    {
        ArgumentNullException.ThrowIfNull(exception, nameof(exception));
        ArgumentNullException.ThrowIfNull(httpContext, nameof(httpContext));

        LoggingExtensions.LogUnexpectedException(_logger, exception.Message, exception.InnerException?.Message);

        ProblemDetails problemDetails = new()
        {
            Type = "https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.1",
            Title = Regex.Replace(exception.GetType().Name, "([a-z])([A-Z])", "$1 $2"),
            Detail = exception.Message,
            Status = (int)HttpStatusCode.InternalServerError,
            Instance = httpContext.Request.Path.ToString(),
            Extensions = new Dictionary<string, object?>
            {
                { "traceId", Activity.Current?.Id ?? httpContext.TraceIdentifier }
            }
        };

        string contentType = "application/problem+json";
        JsonSerializerOptions jsonOptions = new()
        {
            Converters = { new JsonStringEnumConverter() }
        };

        await httpContext.Response.WriteAsJsonAsync(problemDetails, jsonOptions, contentType, cancellationToken).ConfigureAwait(false);

        return true;
    }
}
