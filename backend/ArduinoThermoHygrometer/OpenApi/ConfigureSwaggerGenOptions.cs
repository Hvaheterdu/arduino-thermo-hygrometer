using Asp.Versioning.ApiExplorer;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.Reflection;

namespace ArduinoThermoHygrometer.Web.OpenApi;

public class ConfigureSwaggerGenOptions : IConfigureNamedOptions<SwaggerGenOptions>
{
    private readonly IApiVersionDescriptionProvider _apiVersionDescriptionProvider;

    public ConfigureSwaggerGenOptions(IApiVersionDescriptionProvider apiVersionDescriptionProvider)
    {
        _apiVersionDescriptionProvider = apiVersionDescriptionProvider;
    }

    public void Configure(SwaggerGenOptions options)
    {
        foreach (ApiVersionDescription apiDescription in _apiVersionDescriptionProvider.ApiVersionDescriptions)
        {
            options.SwaggerDoc(apiDescription.GroupName, CreateOpenApiInfoForVersion(apiDescription));
        }

        string xmlCommentsFile = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
        string xmlCommentsFullPath = Path.Combine(AppContext.BaseDirectory, xmlCommentsFile);

        options.IncludeXmlComments(xmlCommentsFullPath);
    }

    public void Configure(string? name, SwaggerGenOptions options)
    {
        Configure(options);
    }

    private static OpenApiInfo CreateOpenApiInfoForVersion(ApiVersionDescription apiDescription)
    {
        OpenApiInfo openApiInfo = new()
        {
            Version = $"v{apiDescription.ApiVersion}",
            Title = "Arduino Thermo Hygrometer API.",
            Description = "An ASP.NET Core Web API for an Arduino Thermo Hygrometer IoT device.",
            Contact = new()
            {
                Name = "Burhan Mohammad Sarfraz",
                Email = "burhan.mohammad.sarfraz@outlook.com"
            },
            License = new()
            {
                Name = "MIT License",
                Url = new Uri("https://mit-license.org/")
            }
        };

        return openApiInfo;
    }
}
