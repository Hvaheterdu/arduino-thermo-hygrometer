using System.Net;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Primitives;

namespace ArduinoThermoHygrometer.Api.Attributes;

[AttributeUsage(AttributeTargets.Method)]
public sealed class ApiKeyAuthorizationAttribute : Attribute, IAsyncAuthorizationFilter
{
    private const string ApiKeyHeaderName = "X-API-KEY";

    public async Task OnAuthorizationAsync(AuthorizationFilterContext context)
    {
        ArgumentNullException.ThrowIfNull(context, nameof(context));

        IConfiguration configuration = context.HttpContext.RequestServices.GetService<IConfiguration>()!;

        bool isApiKey = context.HttpContext.Request.Headers.TryGetValue(ApiKeyHeaderName, out StringValues usedApiKey);

        if (!isApiKey)
        {
            context.Result = new ContentResult()
            {
                Content = "API Key was not provided.",
                StatusCode = (int)HttpStatusCode.Unauthorized
            };

            return;
        }

        string apiKey = configuration["ApiKey"]!;

        if (!apiKey.Equals(usedApiKey, StringComparison.Ordinal))
        {
            context.Result = new ContentResult()
            {
                Content = "Unauthorized access.",
                StatusCode = (int)HttpStatusCode.Unauthorized
            };

            return;
        }

        await Task.CompletedTask;
    }
}
