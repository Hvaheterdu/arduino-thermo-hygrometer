namespace ArduinoThermometer.API.Middleware;

public class SecurityHeadersMiddleware
{
    private readonly RequestDelegate _requestDelegate;

    public SecurityHeadersMiddleware(RequestDelegate requestDelegate)
    {
        _requestDelegate = requestDelegate;
    }

    public Task Invoke(HttpContext httpContext)
    {
        // Content-Security-Policy
        if (!httpContext.Response.Headers.ContainsKey("Content-Security-Policy"))
        {
            httpContext.Response.Headers.Append("Content-Security-Policy", "default-src 'self'; connect-src ws: http: https: 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;");
        }

        // Permisson-Policy
        if (!httpContext.Response.Headers.ContainsKey("Permisson-Policy"))
        {
            httpContext.Response.Headers.Append("Permisson-Policy", "accelerometer 'none'; camera 'none'; geolocation 'none'; gyroscope 'none'; magnetometer 'none'; microphone 'none'; payment 'none'; usb 'none'");
        }

        // Referrer-Policy
        if (!httpContext.Response.Headers.ContainsKey("Referrer-Policy"))
        {
            httpContext.Response.Headers.Append("Referrer-Policy", "no-referrer");
        }

        // X-Frame-Options
        if (!httpContext.Response.Headers.ContainsKey("X-Frame-Options"))
        {
            httpContext.Response.Headers.Append("X-Frame-Options", "SAMEORIGIN");
        }

        // X-Content-Type-Options
        if (!httpContext.Response.Headers.ContainsKey("X-Content-Type-Options"))
        {
            httpContext.Response.Headers.Append("X-Content-Type-Options", "nosniff");
        }

        // X-Xss-Protection
        if (!httpContext.Response.Headers.ContainsKey("X-Xss-Protection"))
        {
            httpContext.Response.Headers.Append("X-Xss-Protection", "1; mode=block");
        }

        // X-Permitted-Cross-Domain-Policies
        if (!httpContext.Response.Headers.ContainsKey("X-Permitted-Cross-Domain-Policies"))
        {
            httpContext.Response.Headers.Append("X-Permitted-Cross-Domain-Policies", "none");
        }

        return _requestDelegate.Invoke(httpContext);
    }
}