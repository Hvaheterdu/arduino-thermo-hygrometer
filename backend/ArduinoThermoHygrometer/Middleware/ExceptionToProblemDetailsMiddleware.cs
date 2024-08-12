using ArduinoThermoHygrometer.Web.Exceptions;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Text.RegularExpressions;

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
    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception, CancellationToken cancellationToken)
    {
        CreateLogsForExceptions(httpContext, exception);

        string contentType = "application/problem+json";
        JsonSerializerOptions jsonOptions = new()
        {
            Converters = { new JsonStringEnumConverter() }
        };

        switch (exception)
        {
            case BadRequestException badRequestException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, exception, GetProblemDetailsType(badRequestException),
                    StatusCodes.Status400BadRequest), jsonOptions, contentType, cancellationToken);
                break;
            case UnauthorizedException unauthorizedAccessException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, exception, GetProblemDetailsType(unauthorizedAccessException),
                    StatusCodes.Status401Unauthorized), jsonOptions, contentType, cancellationToken);
                break;
            case ForbiddenException forbiddenException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, exception, GetProblemDetailsType(forbiddenException),
                    StatusCodes.Status403Forbidden), jsonOptions, contentType, cancellationToken);
                break;
            case NotFoundException notFoundException:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, exception, GetProblemDetailsType(notFoundException),
                    StatusCodes.Status404NotFound), jsonOptions, contentType, cancellationToken);
                break;
            default:
                await httpContext.Response.WriteAsJsonAsync(CreateProblemDetails(httpContext, exception, GetProblemDetailsType(exception),
                    StatusCodes.Status500InternalServerError), jsonOptions, contentType, cancellationToken);
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
    /// <param name="type"></param>
    /// <param name="statusCode"></param>
    /// <returns>A <see cref="ProblemDetails"/> object containing detailed information about the exception and the context in which it occurred.</returns>
    private static ProblemDetails CreateProblemDetails(HttpContext httpContext, Exception exception, string type, int statusCode)
    {
        return new ProblemDetails
        {
            Type = type,
            Title = Regex.Replace(exception.GetType().Name, "([a-z])([A-Z])", "$1 $2"),
            Detail = exception.Message,
            Status = statusCode,
            Instance = GetRequestUri(httpContext),
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
            case BadRequestException badRequestException:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1";
            case UnauthorizedException unauthorizedAccessException:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.2";
            case ForbiddenException forbiddenException:
                return "https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.4";
            case NotFoundException notFoundException:
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
        return httpContext.Request.Path.ToString();
    }
}
