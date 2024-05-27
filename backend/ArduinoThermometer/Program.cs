using ArduinoThermometer.API;
using ArduinoThermometer.API.Data;
using ArduinoThermometer.API.DTOs;
using ArduinoThermometer.API.Middleware;
using ArduinoThermometer.API.Repositories;
using ArduinoThermometer.API.Services;
using ArduinoThermometer.API.Validators;
using FluentValidation;
using Microsoft.OpenApi.Models;

WebApplicationBuilder builder = WebApplication.CreateBuilder(args);

// Verify configuration.
builder.Configuration.VerifyConfiguration("AzureKeyVault", "Url");
builder.Configuration.VerifyConfiguration("MicrosoftGraph:GraphScope");
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
    options.Preload = true;
    options.IncludeSubDomains = true;
    options.MaxAge = TimeSpan.FromDays(30);
});

// Swagger/OpenAPI (https://aka.ms/aspnetcore/swashbuckle).
builder.Services.AddSwaggerGen(options =>
{
    options.SwaggerDoc("v1.0", new OpenApiInfo
    {
        Version = "v1.0",
        Title = "Arduino thermometer and air humidity API.",
        Description = "ASP.NET Core Web API for getting temperature and air humidity from a Arduino IoT Device.",
        Contact = new OpenApiContact
        {
            Name = "Burhan Mohammad Sarfraz",
            Email = "burhan.mohammad.sarfraz@outlook.com"
        },
        License = new OpenApiLicense
        {
            Name = "The MIT License",
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
builder.RegisterDatabaseAndRunMigrationsOnStartup<ArduinoThermometerDbContext>();

WebApplication app = builder.Build();

// Custom middleware.
app.UseMiddleware<SecurityHeadersMiddleware>();

// Swagger and SwaggerUI middleware.
if (!app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI(options =>
    {
        options.SwaggerEndpoint("/swagger/v1.0/swagger.json", "Arduino Thermometer API v1.0");
        options.OAuthAppName("Arduino Thermometer API Swagger API");
        options.OAuthUseBasicAuthenticationWithAccessCodeGrant();
    });
}

// HSTS (Strict-Transport-Security header) and HTTPS redirect middleware.
if (!app.Environment.IsDevelopment())
{
    app.UseHttpsRedirection();
    app.UseHsts();
}

// Test comment.

// CORS middleware.
app.UseCors();

// Authentication and authorization middleware.
app.UseAuthorization();
app.UseAuthentication();

// Endpoints for controllers.
app.MapControllers();

app.Run();
