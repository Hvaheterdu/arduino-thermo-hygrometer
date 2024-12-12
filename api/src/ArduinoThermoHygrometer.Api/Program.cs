using System.Text.Json.Serialization;
using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Middleware;
using ArduinoThermoHygrometer.Api.OpenApi;
using ArduinoThermoHygrometer.Api.Services;
using ArduinoThermoHygrometer.Core.Repositories;
using ArduinoThermoHygrometer.Core.Repositories.Contracts;
using ArduinoThermoHygrometer.Core.Services;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Infrastructure;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Asp.Versioning.ApiExplorer;
using HealthChecks.UI.Client;
using Microsoft.AspNetCore.Diagnostics.HealthChecks;
using Microsoft.Extensions.Options;
using Swashbuckle.AspNetCore.SwaggerGen;

// Services.
WebApplicationBuilder builder = WebApplication.CreateBuilder(args);

// Response headers service.
builder.WebHost.ConfigureKestrel(hostbuilder => hostbuilder.AddServerHeader = false);

// CORS service.
builder.Services.AddCors(setupAction =>
{
    setupAction.AddDefaultPolicy(configurePolicy =>
    {
        configurePolicy.WithOrigins(builder.Configuration.GetSection("CORS")["AllowedOrigins"]!)
            .AllowAnyHeader()
            .AllowAnyMethod();
    });
});

// HSTS (Strict-Transport-Security header) service.
builder.AddHsts();

// HTTPS redirect service.
builder.AddHttpsRedirection();

// Rate limiter service.
builder.AddRateLimiter();

// Opentelemetry logging service.
builder.AddOpenTelemetryLogging();

// Dependency injection from other projects.
builder.Services.AddInfrastructure(builder.Configuration);

// Dependency injection DTOs, services, repositories and validators.
builder.Services.AddScoped<IBatteryService, BatteryService>();
builder.Services.AddScoped<IHumidityService, HumidityService>();
builder.Services.AddScoped<ITemperatureService, TemperatureService>();

builder.Services.AddScoped<IBatteryRepository, BatteryRepository>();
builder.Services.AddScoped<IHumidityRepository, HumidityRepository>();
builder.Services.AddScoped<ITemperatureRepository, TemperatureRepository>();

builder.Services.AddScoped<IHealthCheckService, HealthCheckServiceWrapper>();

// Exception handling service.
builder.Services.AddExceptionHandler<GlobalExceptionHandlerMiddleware>();

// ProblemDetails service.
builder.Services.AddProblemDetails(configure =>
{
    configure.CustomizeProblemDetails = context => context.ProblemDetails.Instance =
        $"{context.HttpContext.Request.Method} {context.HttpContext.Request.Path}";
});

// Register controller service.
builder.Services.AddControllers(configure => configure.ReturnHttpNotAcceptable = true)
    .AddJsonOptions(configure => configure.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter()));

// Lowercase API routes service.
builder.Services.Configure<RouteOptions>(options => options.LowercaseUrls = true);

// Register database, migrations and run migrations on startup service.
builder.RegisterDatabaseAndRunMigrationsOnStartup<ArduinoThermoHygrometerDbContext>();

// API versioning service.
builder.AddApiVersioning();

// Dependency injection for Swagger generated options service.
builder.Services.AddTransient<IConfigureOptions<SwaggerGenOptions>, ConfigureSwaggerGenOptions>();

// Swagger/OpenAPI service (https://aka.ms/aspnetcore/swashbuckle).
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Middleware.
WebApplication app = builder.Build();

// Exception handler and status code pages middleware.
app.UseExceptionHandler();
app.UseStatusCodePages();

// HSTS (Strict-Transport-Security header) and HTTPS redirect middleware.
app.UseHsts();
app.UseHttpsRedirection();

// Routing middleware.
app.UseRouting();

// Rate limiter middleware.
app.UseRateLimiter();

// CORS middleware.
app.UseCors();

// Authentication and authorization middleware.
app.UseAuthentication();
app.UseAuthorization();

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

// Healthcheck middleware.
app.MapHealthChecks($"/api/health", new HealthCheckOptions
{
    ResponseWriter = UIResponseWriter.WriteHealthCheckUIResponse
});

// Custom middleware.
app.UseMiddleware<SecurityHeadersMiddleware>();

// Seed database.
if (app.Environment.IsDevelopment())
{
    using IServiceScope scope = app.Services.CreateScope();
    IServiceProvider services = scope.ServiceProvider;

    ArduinoThermoHygrometerDbContext dbContext = services.GetRequiredService<ArduinoThermoHygrometerDbContext>();
    DatabaseInitialiser.SeedDatabase(dbContext);
}

// Endpoints for controllers.
app.MapControllers();

app.Run();
