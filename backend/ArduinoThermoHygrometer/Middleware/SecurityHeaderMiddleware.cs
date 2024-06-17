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
        // Cache-Control. Header used to direct caching done by browsers. Providing no-store indicates that any caches of any kind (private or shared)
        // should not store the response that contains the header.
        if (!httpContext.Response.Headers.ContainsKey("Cache-Control"))
        {
            httpContext.Response.Headers.Append("Cache-Control", "no-store");
        }

        // Content-Security-Policy. Added layer of security that helps to detect and mitigate certain types of attacks,
        // including Cross-Site Scripting (XSS) and data injection attacks.
        if (!httpContext.Response.Headers.ContainsKey("Content-Security-Policy"))
        {
            httpContext.Response.Headers.Append("Content-Security-Policy", "default-src 'self'; connect-src 'self' data: http: ws:; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'; script-src 'self' 'unsafe-inline'; frame-ancestors 'none';");
        }

        // Permisson-Policy. Provides mechanisms for web developers to explicitly declare what functionality can and cannot be used on a website.
        if (!httpContext.Response.Headers.ContainsKey("Permisson-Policy"))
        {
            httpContext.Response.Headers.Append("Permisson-Policy", "accelerometer=(), ambient-light-sensor=(), autoplay=(), battery=(), camera=(), cross-origin-isolated=(), display-capture=(), document-domain=(), encrypted-media=(), execution-while-not-rendered=(), execution-while-out-of-viewport=(), fullscreen=(), geolocation=(), gyroscope=(), keyboard-map=(), magnetometer=(), microphone=(), midi=(), navigation-override=(), payment=(), picture-in-picture=(), publickey-credentials-get=(), screen-wake-lock=(), sync-xhr=(), usb=(), web-share=(), xr-spatial-tracking=()");
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
