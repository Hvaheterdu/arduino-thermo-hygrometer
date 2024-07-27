using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace ArduinoThermoHygrometer.Web.Exceptions;

public class ExceptionToProblemDetailsHandler : IExceptionHandler
{
    private readonly IProblemDetailsService _problemDetailsService;
    private readonly ILogger<ExceptionToProblemDetailsHandler> _logger;

    public ExceptionToProblemDetailsHandler(IProblemDetailsService problemDetailsService, ILogger<ExceptionToProblemDetailsHandler> logger)
    {
        _problemDetailsService = problemDetailsService;
        _logger = logger;
    }

    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception, CancellationToken cancellationToken)
    {
        CreateLogsForExceptions(httpContext, exception);

        string contentType = "application/problem+json";
        JsonSerializerOptions jsonOptions = new()
        {
            Converters = { new JsonStringEnumConverter() }
        };

        await httpContext.Response.WriteAsJsonAsync(new ProblemDetails
        {
            Type = exception.GetType().Name,
            Title = "Internal server error.",
            Detail = exception.Message,
            Status = StatusCodes.Status500InternalServerError,
            Instance = $"{httpContext.Request.Method} {httpContext.Request.Path}",
            Extensions =
            {
                ["traceId"] = httpContext.TraceIdentifier
            }
        }, jsonOptions, contentType, cancellationToken);

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
            "{GetRequestUriWithRequestMethod}\n      Exception: {Message}\n      InnerException: {Message}",
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
        string requestMethod = httpContext.Request.Method.Replace(Environment.NewLine, "");
        string requestPath = httpContext.Request.Path.ToString().Replace(Environment.NewLine, "");

        return $"{requestMethod} {requestPath}";
    }

    /// <summary>
    /// Gets the request URI from the provided HttpContext.
    /// </summary>
    /// <param name="httpContext">The HttpContext object.</param>
    /// <returns>The request URI.</returns>
    private static string GetRequestUri(HttpContext httpContext)
    {
        return $"{httpContext.Request.Path}";
    }
}
