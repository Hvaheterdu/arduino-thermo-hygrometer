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
                ProblemDetails problemDetails = new()
                {
                    Type = context.HttpContext.Request.Path,
                    Title = "Rate limit reached.",
                    Detail = $"Rate limit reached for {context.HttpContext.Request.Method} method. Please try again after {retryRequestAfter} minute.",
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
                logger.LogWarning($"Rate limit reached for {context.HttpContext.Request.Method} request to {context.HttpContext.Request.Path}. Please try again after {retryRequestAfter} minute.");

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
    /// Registers the database and runs migrations on application startup.
    /// </summary>
    /// <typeparam name="T">The type of the DbContext.</typeparam>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="NotSupportedException">Thrown when the database provider is not SQL Server.</exception>"
    /// <exception cref="NotImplementedException">Thrown when unable to connect to the database.</exception>"
    public static WebApplicationBuilder RegisterDatabaseAndRunMigrationsOnStartup<T>(this WebApplicationBuilder builder) where T : DbContext
    {
        builder.Services.AddDbContext<ArduinoThermoHygrometerDbContext>(options =>
        {
            options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection"),
            assembly => assembly.MigrationsAssembly("ArduinoThermoHygrometer.Infrastructure"));
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

            string? isConnectionStringSet = arduinoThermoHygrometerDbContext.Database.GetConnectionString();
            if (isConnectionStringSet is null)
            {
                throw new NotImplementedException("Database connection cannot be established. Set the connection string in appsettings.Development.json and ensure that it is correct.");
            }

            arduinoThermoHygrometerDbContext.Database.Migrate();
        }

        return builder;
    }
}
