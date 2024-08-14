#pragma warning disable CA2007

namespace ArduinoThermoHygrometer.Web.Middleware;

public class SecurityHeadersMiddleware
{
    private readonly RequestDelegate _next;

    public SecurityHeadersMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    /// <summary>
    /// Invokes the middleware for security headers.
    /// </summary>
    /// <param name="httpContext">The HTTP context.</param>
    /// <returns>A task that represents the asynchronous middleware operation.</returns>
    public async Task InvokeAsync(HttpContext httpContext)
    {
        ArgumentNullException.ThrowIfNull(httpContext, nameof(httpContext));

        httpContext.Response.Headers.TryAdd("Cache-Control", "no-store, no-cache");
        httpContext.Response.Headers.TryAdd("Content-Security-Policy", "connect-src 'none'; default-src 'none'; frame-ancestors 'none'; img-src 'none'; script-src 'none'; style-src 'none';");
        httpContext.Response.Headers.TryAdd("Referrer-Policy", "no-referrer");
        httpContext.Response.Headers.TryAdd("X-Content-Type-Options", "nosniff");

        await _next(httpContext);
    }
}
