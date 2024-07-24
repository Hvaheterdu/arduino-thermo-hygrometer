using ArduinoThermoHygrometer.Infrastructure;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Extensions;
using ArduinoThermoHygrometer.Web.Middleware;
using ArduinoThermoHygrometer.Web.OpenApi;
using ArduinoThermoHygrometer.Web.Repositories;
using ArduinoThermoHygrometer.Web.Services;
using ArduinoThermoHygrometer.Web.Validators;
using Asp.Versioning.ApiExplorer;
using FluentValidation;
using HealthChecks.UI.Client;
using Microsoft.AspNetCore.Diagnostics.HealthChecks;
using Microsoft.Extensions.Options;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.Text.Json.Serialization;

// Services.
WebApplicationBuilder builder = WebApplication.CreateBuilder(args);

// Configure CORS.
builder.Services.AddCors(setupAction =>
{
    setupAction.AddDefaultPolicy(configurePolicy =>
    {
        configurePolicy.WithOrigins(builder.Configuration.GetSection("CORS")["AllowedOrigins"]!)
            .AllowAnyHeader()
            .AllowAnyMethod();
    });
});

// HTTPS redirect service.
builder.AddHttpsRedirection();

// HSTS (Strict-Transport-Security header) service.
builder.Services.AddHsts(configureOptions =>
{
    configureOptions.IncludeSubDomains = true;
    configureOptions.MaxAge = TimeSpan.FromSeconds(31536000);
    configureOptions.Preload = true;
});

// Dependency injection from other projects.
builder.Services.AddInfrastructure(builder.Configuration);

// Dependency injection DTOs, services, repositories and validators.
builder.Services.AddTransient<ITemperatureService, TemperatureService>();
builder.Services.AddTransient<IBatteryService, BatteryService>();

builder.Services.AddScoped<ITemperatureRepository, TemperatureRepository>();
builder.Services.AddScoped<IBatteryRepository, BatteryRepository>();

builder.Services.AddScoped<IHealthcheckService, HealthcheckService>();

builder.Services.AddScoped<IValidator<TemperatureDto>, TemperatureDtoValidator>();
builder.Services.AddScoped<IValidator<BatteryDto>, BatteryDtoValidator>();

// Register controller service.
builder.Services.AddControllers(configure => configure.ReturnHttpNotAcceptable = true)
    .AddJsonOptions(configure =>
    {
        configure.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
        configure.JsonSerializerOptions.DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull;
    });

// Lowercase API routes.
builder.Services.Configure<RouteOptions>(options => options.LowercaseUrls = true);

// Register database, migrations and run migrations on startup.
builder.RegisterDatabaseAndRunMigrationsOnStartup<ArduinoThermoHygrometerDbContext>();

// API versioning.
builder.AddApiVersioning();

// Dependency injection for Swagger generated options.
builder.Services.AddTransient<IConfigureOptions<SwaggerGenOptions>, ConfigureSwaggerGenOptions>();

// Swagger/OpenAPI (https://aka.ms/aspnetcore/swashbuckle).
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Middleware.
WebApplication app = builder.Build();

// Custom middleware.
app.UseMiddleware<SecurityHeadersMiddleware>();

// Swagger and SwaggerUI middleware.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI(setupAction =>
    {
        IReadOnlyList<ApiVersionDescription> apiVersionProvider = app.DescribeApiVersions();
        foreach (ApiVersionDescription description in apiVersionProvider)
        {
            setupAction.SwaggerEndpoint($"/swagger/{description.GroupName}/swagger.json", description.GroupName);
        }

        setupAction.OAuthAppName("Arduino Thermo Hygrometer API");
        setupAction.OAuthUseBasicAuthenticationWithAccessCodeGrant();
    });
}

// HSTS (Strict-Transport-Security header) and HTTPS redirect middleware.
app.UseHsts();
app.UseHttpsRedirection();

// Healthcheck middleware.
app.MapHealthChecks($"/api/_health", new HealthCheckOptions
{
    ResponseWriter = UIResponseWriter.WriteHealthCheckUIResponse
});

// CORS middleware.
app.UseCors();

// Authentication and authorization middleware.
app.UseAuthentication();
app.UseAuthorization();

// Endpoints for controllers.
app.MapControllers();

app.Run();
