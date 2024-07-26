using Microsoft.AspNetCore.Diagnostics;

namespace ArduinoThermoHygrometer.Web.Middleware;

public class ExceptionToProblemDetailsMiddleware : IExceptionHandler
{
    private readonly ILogger<ExceptionToProblemDetailsMiddleware> _logger;

    public ExceptionToProblemDetailsMiddleware(ILogger<ExceptionToProblemDetailsMiddleware> logger)
    {
        _logger = logger;
    }

    public ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception, CancellationToken cancellationToken)
    {
        throw new NotImplementedException();
    }
}
