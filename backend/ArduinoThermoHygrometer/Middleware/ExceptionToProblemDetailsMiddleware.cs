using ArduinoThermoHygrometer.Web.Exceptions;
using ArduinoThermoHygrometer.Web.Extensions;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Text.RegularExpressions;

#pragma warning disable CA2007

namespace ArduinoThermoHygrometer.Web.Middleware;

public class ExceptionToProblemDetailsMiddleware : IExceptionHandler
{
    private readonly ILogger<ExceptionToProblemDetailsMiddleware> _logger;

    public ExceptionToProblemDetailsMiddleware(ILogger<ExceptionToProblemDetailsMiddleware> logger)
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

        CreateLogsForExceptions(httpContext, exception);

        string contentType = "application/problem+json";
        JsonSerializerOptions jsonOptions = new()
        {
            Converters = { new JsonStringEnumConverter() }
        };

        switch (exception)
        {
            case BadRequestException badRequestException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, badRequestException, StatusCodes.Status400BadRequest),
                    jsonOptions, contentType, cancellationToken);
                break;
            case UnauthorizedException unauthorizedAccessException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, unauthorizedAccessException, StatusCodes.Status401Unauthorized),
                    jsonOptions, contentType, cancellationToken);
                break;
            case ForbiddenException forbiddenException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, forbiddenException, StatusCodes.Status403Forbidden),
                    jsonOptions, contentType, cancellationToken);
                break;
            case NotFoundException notFoundException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, notFoundException, StatusCodes.Status404NotFound),
                    jsonOptions, contentType, cancellationToken);
                break;
            default:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, exception, StatusCodes.Status500InternalServerError),
                    jsonOptions, contentType, cancellationToken);
                break;
        }

        return true;
    }

    /// <summary>
    /// Creates a <see cref="ProblemDetails"/> object that encapsulates information about an exception 
    /// that occurred during the handling of an HTTP request.
    /// </summary>
    /// <param name="httpContext">The <see cref="HttpContext"/> representing the current HTTP request.</param>
    /// <param name="exception">The <see cref="Exception"/> that was thrown during the handling of the HTTP request.</param>
    /// <param name="statusCode"></param>
    /// <returns>A <see cref="ProblemDetails"/> object containing detailed information about the exception and the context in which it occurred.</returns>
    private static ProblemDetails CreateProblemDetails(HttpContext httpContext, Exception exception, int statusCode)
    {
        return new ProblemDetails
        {
            Type = GetProblemDetailsType(exception),
            Title = Regex.Replace(exception.GetType().Name, "([a-z])([A-Z])", "$1 $2"),
            Detail = exception.Message,
            Status = statusCode,
            Instance = httpContext.Request.Path.ToString(),
            Extensions = new Dictionary<string, object?>
            {
                { "traceId", Activity.Current?.Id ?? httpContext.TraceIdentifier }
            }
        };
    }

    /// <summary>
    /// Determines the appropriate Problem Details type URL based on the provided exception.
    /// </summary>
    /// <param name="exception">The exception for which to determine the Problem Details type.</param>
    /// <returns>A string representing the URL of the RFC 9110 section that corresponds to the type of exception.</returns>
    private static string GetProblemDetailsType(Exception exception)
    {
        switch (exception)
        {
            case BadRequestException _:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1";
            case UnauthorizedException _:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.2";
            case ForbiddenException _:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.4";
            case NotFoundException _:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.5";
            default:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.1";
        }
    }

    /// <summary>
    /// Creates logs for exceptions.
    /// </summary>
    /// <param name="context">The HttpContext object.</param>
    /// <param name="exception">The exception that occurred.</param>
    private void CreateLogsForExceptions(HttpContext context, Exception exception)
    {
        LoggingExtensions.LoggingError(_logger,
            $"{GetRequestUriWithRequestMethod(context)}\n" +
            $"      Exception: {exception.Message}\n" +
            $"      InnerException: {exception.InnerException?.Message}");
    }

    /// <summary>
    /// Gets the request URI with the request method from the provided HttpContext.
    /// </summary>
    /// <param name="httpContext">The HttpContext object.</param>
    /// <returns>The request URI with the request method.</returns>
    private static string GetRequestUriWithRequestMethod(HttpContext httpContext)
    {
        string requestMethod = httpContext.Request.Method.Replace(Environment.NewLine, "", 0);
        string requestPath = httpContext.Request.Path.ToString().Replace(Environment.NewLine, "", 0);

        return $"{requestMethod} {requestPath}";
    }
}
