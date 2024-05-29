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
        // Content-Security-Policy. Added layer of security that helps to detect and mitigate certain types of attacks,
        // including Cross-Site Scripting (XSS) and data injection attacks.
        if (!httpContext.Response.Headers.ContainsKey("Content-Security-Policy"))
        {
            httpContext.Response.Headers.Append("Content-Security-Policy", "default-src 'self'; connect-src ws: http: https: 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;");
        }

        // Permisson-Policy. Provides mechanisms for web developers to explicitly declare what functionality can and cannot be used on a website.
        if (!httpContext.Response.Headers.ContainsKey("Permisson-Policy"))
        {
            httpContext.Response.Headers.Append("Permisson-Policy", "accelerometer 'none'; camera 'none'; geolocation 'none'; gyroscope 'none'; magnetometer 'none'; microphone 'none'; payment 'none'; usb 'none'");
        }

        // Referrer-Policy. HTTP header that controls how much referrer information (sent with the Referer header) should be included with requests.
        if (!httpContext.Response.Headers.ContainsKey("Referrer-Policy"))
        {
            httpContext.Response.Headers.Append("Referrer-Policy", "no-referrer");
        }

        // X-Frame-Options. HTTP response header used to indicate whether a browser should be allowed to render a page in a <frame>, <iframe>, <embed> or <object>.
        if (!httpContext.Response.Headers.ContainsKey("X-Frame-Options"))
        {
            httpContext.Response.Headers.Append("X-Frame-Options", "SAMEORIGIN");
        }

        // X-Content-Type-Options. HTTP response header used by the server to indicate that the MIME types advertised in the Content-Type headers should be followed and not be changed.
        if (!httpContext.Response.Headers.ContainsKey("X-Content-Type-Options"))
        {
            httpContext.Response.Headers.Append("X-Content-Type-Options", "nosniff");
        }

        // X-Permitted-Cross-Domain-Policies. Used to control how web browsers handle cross-domain requests, such as loading resources or making requests between different domains.
        if (!httpContext.Response.Headers.ContainsKey("X-Permitted-Cross-Domain-Policies"))
        {
            httpContext.Response.Headers.Append("X-Permitted-Cross-Domain-Policies", "none");
        }

        return _requestDelegate.Invoke(httpContext);
    }
}