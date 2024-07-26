namespace ArduinoThermoHygrometer.Web.Middleware;

public class SecurityHeadersMiddleware
{
    private readonly RequestDelegate _next;

    public SecurityHeadersMiddleware(RequestDelegate requestDelegate)
    {
        _next = requestDelegate;
    }

    /// <summary>
    /// Invokes the middleware for security headers.
    /// </summary>
    /// <param name="httpContext">The HTTP context.</param>
    /// <returns>A task that represents the asynchronous middleware operation.</returns>
    public async Task InvokeAsync(HttpContext httpContext)
    {
        httpContext.Response.Headers.TryAdd("Cache-Control", "no-store, no-cache");
        httpContext.Response.Headers.TryAdd("Content-Security-Policy", "connect-src http: ws: wss: 'self'; default-src 'self'; frame-ancestors 'none'; img-src data: 'self'; script-src 'unsafe-inline' 'self'; style-src 'unsafe-inline' 'self';");
        httpContext.Response.Headers.TryAdd("Referrer-Policy", "no-referrer");
        httpContext.Response.Headers.TryAdd("X-Content-Type-Options", "nosniff");

        await _next(httpContext);
    }
}
