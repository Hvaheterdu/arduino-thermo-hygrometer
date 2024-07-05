using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web;
using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Middleware;
using ArduinoThermoHygrometer.Web.Repositories;
using ArduinoThermoHygrometer.Web.Services;
using ArduinoThermoHygrometer.Web.Validators;
using FluentValidation;
using Microsoft.OpenApi.Models;

WebApplicationBuilder builder = WebApplication.CreateBuilder(args);

// Verify configuration.
builder.Configuration.VerifyConfiguration("CORS", "AllowedOrigin");

// Configure CORS.
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy =>
    {
        policy.WithOrigins(builder.Configuration.GetSection("CORS")["AllowedOrigin"]!)
            .AllowAnyHeader()
            .AllowAnyMethod();
    });
});

// HTTPS redirect service.
if (builder.Environment.IsDevelopment())
{
    builder.Services.AddHttpsRedirection(options =>
    {
        options.RedirectStatusCode = StatusCodes.Status307TemporaryRedirect;
        options.HttpsPort = builder.Configuration.GetValue<int>("HTTPS_PORTS:Development");
    });
}
else
{
    builder.Services.AddHttpsRedirection(options =>
    {
        options.RedirectStatusCode = StatusCodes.Status308PermanentRedirect;
        options.HttpsPort = builder.Configuration.GetValue<int>("HTTPS_PORT:Production");
    });
}

// HSTS (Strict-Transport-Security header) service.
builder.Services.AddHsts(options =>
{
    options.MaxAge = TimeSpan.FromSeconds(31536000);
    options.IncludeSubDomains = true;
    options.Preload = true;
});

// Swagger/OpenAPI (https://aka.ms/aspnetcore/swashbuckle).
builder.Services.AddSwaggerGen(options =>
{
    options.SwaggerDoc("v1.0", new OpenApiInfo
    {
        Version = "v1.0",
        Title = "Arduino Thermo Hygrometer API.",
        Description = "An ASP.NET Core Web API for an Arduino Thermo Hygrometer IoT device.",
        Contact = new OpenApiContact
        {
            Name = "Burhan Mohammad Sarfraz",
            Email = "burhan.mohammad.sarfraz@outlook.com"
        },
        License = new OpenApiLicense
        {
            Name = "MIT License",
            Url = new Uri("https://mit-license.org/")
        }
    });
});

// Dependency injection DTOs, services, repositories and validators.
builder.Services.AddTransient<TemperatureService>();
builder.Services.AddTransient<BatteryService>();

builder.Services.AddScoped<ITemperatureRepository, TemperatureRepository>();
builder.Services.AddScoped<IBatteryRepository, BatteryRepository>();

builder.Services.AddScoped<IValidator<TemperatureDto>, TemperatureDtoValidator>();
builder.Services.AddScoped<IValidator<BatteryDto>, BatteryDtoValidator>();

// Register controller service.
builder.Services.AddControllers();

// Lowercase API routes.
builder.Services.Configure<RouteOptions>(options => options.LowercaseUrls = true);

// HTTP/HTTPS logging service
builder.Services.AddHttpLogging(logging =>
{
    logging.ResponseBodyLogLimit = 8096;
    logging.RequestBodyLogLimit = 8096;
});

// Register database, migrations and run migrations on startup
builder.RegisterDatabaseAndRunMigrationsOnStartup<ArduinoThermoHygrometerDbContext>();

WebApplication app = builder.Build();

// Custom middleware.
app.UseMiddleware<SecurityHeadersMiddleware>();

// Swagger and SwaggerUI middleware.
if (!app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI(options =>
    {
        options.SwaggerEndpoint("/swagger/v1.0/swagger.json", "Arduino Thermo Hygrometer API v1.0");
        options.OAuthAppName("Arduino Thermo Hygrometer API");
        options.OAuthUseBasicAuthenticationWithAccessCodeGrant();
    });
}

// HSTS (Strict-Transport-Security header) and HTTPS redirect middleware.
app.UseHsts();
app.UseHttpsRedirection();

// CORS middleware.
app.UseCors();

// Authentication and authorization middleware.
app.UseAuthentication();
app.UseAuthorization();

// Endpoints for controllers.
app.MapControllers();

app.Run();
