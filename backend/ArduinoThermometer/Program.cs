using ArduinoThermometer.API;
using ArduinoThermometer.API.Data;
using Microsoft.OpenApi.Models;

WebApplicationBuilder builder = WebApplication.CreateBuilder(args);

// Configuration check.
builder.Configuration.VerifyConfigurationExists("AzureKeyVault", "Url");
builder.Configuration.VerifyConfigurationExists("MicrosoftGraph:GraphScope");
builder.Configuration.VerifyConfigurationExists("CORS", "AllowedOrigin");

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
        options.HttpsPort = 5001;
    });
}
else
{
    builder.Services.AddHttpsRedirection(options =>
    {
        options.RedirectStatusCode = StatusCodes.Status308PermanentRedirect;
        options.HttpsPort = 443;
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
        Title = "Arduino Thermometer API.",
        Description = "ASP.NET Core Web API for getting temperature from a Arduino Thermometer IoT Device.",
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

// Dependency injection DTOs, services and repositories.

// Register controller service.
builder.Services.AddControllers();

// HTTP/HTTPS logging service
builder.Services.AddHttpLogging(logging => logging.RequestBodyLogLimit = 4096);

// Register database, migrations and run migrations on startup
builder.RegisterDatabaseAndRunMigrationsOnStartup<ArduinoThermometerDbContext>();

WebApplication app = builder.Build();

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

// CORS middleware.
app.UseCors();

// Authentication and authorization middleware.
app.UseAuthorization();
app.UseAuthentication();

// Serve frontend as static files from web application.
app.UseStaticFiles();

// Endpoints for controllers.
app.MapControllers();

app.Run();
