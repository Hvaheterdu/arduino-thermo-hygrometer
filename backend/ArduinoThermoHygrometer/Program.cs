using ArduinoThermoHygrometer.Infrastructure;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Extensions;
using ArduinoThermoHygrometer.Web.Middleware;
using ArduinoThermoHygrometer.Web.OpenApi;
using ArduinoThermoHygrometer.Web.Repositories;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;
using ArduinoThermoHygrometer.Web.Services;
using ArduinoThermoHygrometer.Web.Services.Contracts;
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

// HSTS (Strict-Transport-Security header) service.
builder.AddHsts();

// HTTPS redirect service.
builder.AddHttpsRedirection();

// Rate limiter service.
builder.AddRateLimiter();

// Dependency injection from other projects.
builder.Services.AddInfrastructure(builder.Configuration);

// Dependency injection DTOs, services, repositories and validators.
builder.Services.AddScoped<IBatteryService, BatteryService>();
builder.Services.AddScoped<IHumidityService, HumidityService>();
builder.Services.AddScoped<ITemperatureService, TemperatureService>();

builder.Services.AddScoped<IBatteryRepository, BatteryRepository>();
builder.Services.AddScoped<IHumidityRepository, HumidityRepository>();
builder.Services.AddScoped<ITemperatureRepository, TemperatureRepository>();

builder.Services.AddScoped<IHealthcheckService, HealthcheckService>();

builder.Services.AddScoped<IValidator<BatteryDto>, BatteryDtoValidator>();
builder.Services.AddScoped<IValidator<HumidityDto>, HumidityDtoValidator>();
builder.Services.AddScoped<IValidator<TemperatureDto>, TemperatureDtoValidator>();

// Exception handling.
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
app.MapHealthChecks($"/api/_health", new HealthCheckOptions
{
    ResponseWriter = UIResponseWriter.WriteHealthCheckUIResponse
});

// Custom middleware.
app.UseMiddleware<SecurityHeadersMiddleware>();

// Seed database.
if (!app.Environment.IsProduction())
{
    using IServiceScope scope = app.Services.CreateScope();
    IServiceProvider services = scope.ServiceProvider;

    ArduinoThermoHygrometerDbContext dbContext = services.GetRequiredService<ArduinoThermoHygrometerDbContext>();
    DatabaseInitialiser.SeedDatabase(dbContext);
}

// Endpoints for controllers.
app.MapControllers();

app.Run();
