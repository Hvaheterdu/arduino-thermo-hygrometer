using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Diagnostics;
using System.Globalization;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.RateLimiting;

namespace ArduinoThermoHygrometer.Web.Extensions;

public static class ProgramExtensions
{
    /// <summary>
    /// Adds HTTP Strict Transport Security (HSTS) to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    public static WebApplicationBuilder AddHsts(this WebApplicationBuilder builder)
    {
        if (builder.Environment.IsDevelopment())
        {
            builder.Services.AddHsts(configureOptions =>
            {
                configureOptions.IncludeSubDomains = true;
                configureOptions.MaxAge = TimeSpan.FromSeconds(60);
            });
        }
        else
        {
            builder.Services.AddHsts(configureOptions =>
            {
                configureOptions.IncludeSubDomains = true;
                configureOptions.MaxAge = TimeSpan.FromSeconds(31536000);
            });
        }

        return builder;
    }

    /// <summary>
    /// Adds HTTP redirection to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    public static WebApplicationBuilder AddHttpsRedirection(this WebApplicationBuilder builder)
    {
        if (builder.Environment.IsDevelopment())
        {
            builder.Services.AddHttpsRedirection(configureOptions =>
            {
                configureOptions.HttpsPort = builder.Configuration.GetValue<int>("HTTPS_PORTS:Development");
                configureOptions.RedirectStatusCode = StatusCodes.Status307TemporaryRedirect;
            });
        }
        else
        {
            builder.Services.AddHttpsRedirection(configureOptions =>
            {
                configureOptions.HttpsPort = builder.Configuration.GetValue<int>("HTTPS_PORT:Production");
                configureOptions.RedirectStatusCode = StatusCodes.Status308PermanentRedirect;
            });
        }

        return builder;
    }

    /// <summary>
    /// Adds fixed window rate limiter middleware to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    public static WebApplicationBuilder AddRateLimiter(this WebApplicationBuilder builder)
    {
        MyRateLimitOptions myOptions = new();
        builder.Configuration.GetSection(MyRateLimitOptions.MyRateLimit).Bind(myOptions);

        builder.Services.AddRateLimiter(configureOptions =>
        {
            configureOptions.OnRejected = (context, cancellationToken) =>
            {
                if (context.Lease.TryGetMetadata(MetadataName.RetryAfter, out TimeSpan retryAfter))
                {
                    context.HttpContext.Response.Headers.RetryAfter = ((int)retryAfter.TotalMinutes).ToString(NumberFormatInfo.InvariantInfo);
                }

                string retryRequestAfter = ((int)retryAfter.TotalMinutes).ToString(NumberFormatInfo.InvariantInfo);
                string requestMethod = context.HttpContext.Request.Method.Replace(Environment.NewLine, "");
                string requestPath = context.HttpContext.Request.Path.ToString().Replace(Environment.NewLine, "");

                ProblemDetails problemDetails = new()
                {
                    Type = context.HttpContext.Request.Path,
                    Title = "Rate limit reached.",
                    Detail = $"Rate limit reached for {requestMethod} method. Please try again after {retryRequestAfter} minute.",
                    Status = StatusCodes.Status429TooManyRequests,
                    Extensions =
                    {
                        ["traceId"] = Activity.Current?.Id ?? context.HttpContext.TraceIdentifier,
                    },
                };

                context.HttpContext.Response.StatusCode = StatusCodes.Status429TooManyRequests;
                string contentType = "application/problem+json";

                JsonSerializerOptions jsonOptions = new()
                {
                    WriteIndented = true,
                    Converters = { new JsonStringEnumConverter() },
                };

                context.HttpContext.Response.WriteAsJsonAsync(problemDetails, jsonOptions, contentType, cancellationToken);

                ILoggerFactory loggerFactory = LoggerFactory.Create(builder => builder.AddConsole());
                ILogger logger = loggerFactory.CreateLogger("ArduinoThermoHygrometer.Web.Extensions.ProgramExtensions.RateLimiter");
                logger.LogWarning("Rate limit reached for {RequestMethod} request to {RequestPath}. Please try again after {RetryRequestAfter} minute.", requestMethod, requestPath, retryRequestAfter);

                return new ValueTask();
            };

            configureOptions.GlobalLimiter = PartitionedRateLimiter.Create<HttpContext, string>(httpContext =>
            {
                string factory = httpContext.Request.Headers.UserAgent.ToString();

                return RateLimitPartition.GetFixedWindowLimiter(
                    httpContext.User.Identity?.Name?.ToString() ?? factory,
                    _ => new FixedWindowRateLimiterOptions
                    {
                        PermitLimit = myOptions.PermitLimit,
                        Window = TimeSpan.FromSeconds(myOptions.Window),
                    }
                );
            });
        });

        return builder;
    }

    /// <summary>
    /// Adds API versioning to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    public static WebApplicationBuilder AddApiVersioning(this WebApplicationBuilder builder)
    {
        builder.Services.AddApiVersioning(setupAction =>
        {
            setupAction.ApiVersionReader = new UrlSegmentApiVersionReader();
            setupAction.AssumeDefaultVersionWhenUnspecified = true;
            setupAction.DefaultApiVersion = new ApiVersion(0, 1);
            setupAction.ReportApiVersions = true;
        })
            .AddMvc()
            .AddApiExplorer(setupAction =>
            {
                setupAction.GroupNameFormat = "'v'VVVV";
                setupAction.SubstituteApiVersionInUrl = true;
            });

        return builder;
    }

    /// <summary>
    /// Adds database registration and runs migrations on application startup.
    /// </summary>
    /// <typeparam name="T">The type of the DbContext.</typeparam>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="NotImplementedException">Thrown when the database connection string cannot be found.</exception>
    /// <exception cref="NotSupportedException">Thrown when the database provider currently in use is not the SQL Server provider.</exception>
    /// <exception cref="ArgumentNullException">Thrown when database migrations are not set to run on application startup.</exception>
    public static WebApplicationBuilder RegisterDatabaseAndRunMigrationsOnStartup<T>(this WebApplicationBuilder builder) where T : DbContext
    {
        string? databaseConnectionString = builder.Configuration.GetConnectionString("DefaultConnection");
        if (databaseConnectionString is null)
        {
            throw new NotImplementedException("Database connection string cannot be found." +
                "Set the connection string in appsettings.Development.json and ensure that it is correct.");
        }

        builder.Services.AddDbContext<ArduinoThermoHygrometerDbContext>(optionsAction =>
        {
            optionsAction.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection"),
            sqlServerOptionsAction => sqlServerOptionsAction.MigrationsAssembly("ArduinoThermoHygrometer.Infrastructure"));
        });

        bool.TryParse(builder.Configuration.GetSection("Database")["RunDatabaseMigrationsOnStartup"], out bool runDatabaseMigrationOnStartup);

        if (runDatabaseMigrationOnStartup)
        {
            using IServiceScope scope = builder.Services.BuildServiceProvider().CreateScope();
            ArduinoThermoHygrometerDbContext arduinoThermoHygrometerDbContext = scope.ServiceProvider.GetRequiredService<ArduinoThermoHygrometerDbContext>();

            bool isSqlServer = arduinoThermoHygrometerDbContext.Database.IsSqlServer();
            if (!isSqlServer)
            {
                throw new NotSupportedException("Database provider currently in use is not the SQL Server provider.");
            }

            arduinoThermoHygrometerDbContext.Database.Migrate();
        }
        else
        {
            throw new ArgumentNullException(runDatabaseMigrationOnStartup.ToString(),
                "Database migrations are not set to run on application startup. Set the 'Database' key with 'RunDatabaseMigrationsOnStartup' value to 'true' in appsettings.Development.json.");
        }

        return builder;
    }
}
