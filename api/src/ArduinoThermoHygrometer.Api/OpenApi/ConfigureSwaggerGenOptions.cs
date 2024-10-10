using System.Reflection;
using Asp.Versioning.ApiExplorer;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace ArduinoThermoHygrometer.Api.OpenApi;

public class ConfigureSwaggerGenOptions : IConfigureNamedOptions<SwaggerGenOptions>
{
    private readonly IApiVersionDescriptionProvider _apiVersionDescriptionProvider;

    public ConfigureSwaggerGenOptions(IApiVersionDescriptionProvider apiVersionDescriptionProvider)
    {
        _apiVersionDescriptionProvider = apiVersionDescriptionProvider;
    }

    /// <summary>
    /// Configures the SwaggerGenOptions for generating Swagger documentation.
    /// </summary>
    /// <param name="options">The SwaggerGenOptions to configure.</param>
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

    /// <summary>
    /// Creates an instance of OpenApiInfo for the specified apiDescription.
    /// </summary>
    /// <param name="apiDescription">The <see cref="ApiVersionDescription"/> object.</param>
    /// <returns>An instance of <see cref="OpenApiInfo"/>.</returns>
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

        if (apiDescription.IsDeprecated)
        {
            openApiInfo.Description += $"<b> API version {apiDescription.ApiVersion} has been deprecated. Please select a newer version from the definition.</b>";
        }

        return openApiInfo;
    }

    /// <summary>
    /// Has to be implemented because of the interface.
    /// </summary>
    /// <param name="name">The name of the options instance to configure.</param>
    /// <param name="options">The SwaggerGenOptions to configure.</param>
    public void Configure(string? name, SwaggerGenOptions options) => Configure(options);
}
