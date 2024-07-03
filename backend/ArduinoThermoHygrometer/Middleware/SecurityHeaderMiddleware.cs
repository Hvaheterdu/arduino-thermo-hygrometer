namespace ArduinoThermoHygrometer.Middleware;

public class SecurityHeadersMiddleware
{
    private readonly RequestDelegate _requestDelegate;

    public SecurityHeadersMiddleware(RequestDelegate requestDelegate)
    {
        _requestDelegate = requestDelegate;
    }

    /// <summary>
    /// Invokes the middleware for security headers.
    /// </summary>
    /// <param name="httpContext">The HTTP context.</param>
    /// <returns>A task that represents the asynchronous middleware operation.</returns>
    public async Task InvokeAsync(HttpContext httpContext)
    {
        httpContext.Response.Headers.TryAdd("Cache-Control", "no-store");
        httpContext.Response.Headers.TryAdd("Content-Security-Policy", "connect-src http: wss: 'self'; default-src 'self'; frame-ancestors 'none'; img-src data: 'self'; script-src 'unsafe-inline' 'self'; style-src 'unsafe-inline' 'self';");
        httpContext.Response.Headers.TryAdd("Referrer-Policy", "no-referrer");
        httpContext.Response.Headers.TryAdd("X-Content-Type-Options", "nosniff");
        httpContext.Response.Headers.TryAdd("X-Frame-Options", "DENY");

        await _requestDelegate(httpContext);
    }
}
