namespace ArduinoThermoHygrometer.Middleware;

public class SecurityHeadersMiddleware
{
    private readonly RequestDelegate _requestDelegate;

    public SecurityHeadersMiddleware(RequestDelegate requestDelegate)
    {
        _requestDelegate = requestDelegate;
    }

    public Task Invoke(HttpContext httpContext)
    {
        // Referrer-Policy. HTTP header that controls how much referrer information (sent with the Referer header) should be included with requests.
        if (!httpContext.Response.Headers.ContainsKey("Referrer-Policy"))
        {
            httpContext.Response.Headers.Append("Referrer-Policy", "no-referrer");
        }

        // X-Content-Type-Options. HTTP response header used by the server to indicate that the MIME types advertised in the Content-Type headers should be followed and not be changed.
        if (!httpContext.Response.Headers.ContainsKey("X-Content-Type-Options"))
        {
            httpContext.Response.Headers.Append("X-Content-Type-Options", "nosniff");
        }

        return _requestDelegate.Invoke(httpContext);
    }
}
