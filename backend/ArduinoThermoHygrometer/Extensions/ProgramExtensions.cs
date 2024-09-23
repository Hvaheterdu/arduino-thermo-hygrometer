using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Options;
using ArduinoThermoHygrometer.Infrastructure;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.RateLimiting;
using Microsoft.EntityFrameworkCore;
using OpenTelemetry.Logs;
using OpenTelemetry.Metrics;
using OpenTelemetry.Resources;
using OpenTelemetry.Trace;
using System.Diagnostics;
using System.Globalization;
using System.Reflection;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.RateLimiting;

namespace ArduinoThermoHygrometer.Api.Extensions;

public static class ProgramExtensions
{
    /// <summary>
    /// Adds HTTP Strict Transport Security (HSTS) service to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="builder"/> is null.</exception>
    public static WebApplicationBuilder AddHsts(this WebApplicationBuilder builder)
    {
        ArgumentNullException.ThrowIfNull(builder, nameof(builder));

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
    /// Adds HTTP redirection service to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="builder"/> is null.</exception>
    public static WebApplicationBuilder AddHttpsRedirection(this WebApplicationBuilder builder)
    {
        ArgumentNullException.ThrowIfNull(builder, nameof(builder));

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
    /// Adds fixed window rate limiter service to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="builder"/> is null.</exception>
    public static WebApplicationBuilder AddRateLimiter(this WebApplicationBuilder builder)
    {
        ArgumentNullException.ThrowIfNull(builder, nameof(builder));

        ApiRateLimiterOptions myOptions = new();
        builder.Configuration.GetSection(ApiRateLimiterOptions.RateLimit).Bind(myOptions);

        builder.Services.AddRateLimiter(configureOptions =>
        {
            configureOptions.OnRejected = (context, cancellationToken) =>
            {
                if (context.Lease.TryGetMetadata(MetadataName.RetryAfter, out TimeSpan retryAfter))
                {
                    context.HttpContext.Response.Headers.RetryAfter = ((int)retryAfter.TotalMinutes).ToString(NumberFormatInfo.InvariantInfo);
                }

                string requestMethod = context.HttpContext.Request.Method.Replace(Environment.NewLine, "", 0);
                string requestPath = context.HttpContext.Request.Path.ToString().Replace(Environment.NewLine, "", 0);
                string retryRequestAfter = ((int)retryAfter.TotalMinutes).ToString(NumberFormatInfo.InvariantInfo);

                ProblemDetails problemDetails = CreateProblemDetailsForRateLimiter(context, requestMethod, retryRequestAfter);

                context.HttpContext.Response.StatusCode = StatusCodes.Status429TooManyRequests;
                string contentType = "application/problem+json";

                JsonSerializerOptions jsonOptions = new()
                {
                    WriteIndented = true,
                    Converters = { new JsonStringEnumConverter() },
                };

                context.HttpContext.Response.WriteAsJsonAsync(problemDetails, jsonOptions, contentType, cancellationToken);

                LoggerForRateLimiter(requestMethod, requestPath, retryRequestAfter);

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
    /// Adds API versioning service to the WebApplicationBuilder.
    /// </summary>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="builder"/> is null.</exception>
    public static WebApplicationBuilder AddApiVersioning(this WebApplicationBuilder builder)
    {
        ArgumentNullException.ThrowIfNull(builder, nameof(builder));

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
    /// Registers database and runs migrations on application startup.
    /// </summary>
    /// <typeparam name="T">The type of the DbContext.</typeparam>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="builder"/> is null.</exception>
    /// <exception cref="NotImplementedException">Thrown when the database connection string cannot be found.</exception>
    /// <exception cref="NotSupportedException">Thrown when the database provider currently in use is not the SQL Server provider.</exception>
    public static WebApplicationBuilder RegisterDatabaseAndRunMigrationsOnStartup<T>(this WebApplicationBuilder builder) where T : DbContext
    {
        ArgumentNullException.ThrowIfNull(builder, nameof(builder));

        builder.Services.AddSqlServer(builder.Configuration);

        IConfigurationSection databaseConfiguration = builder.Configuration.GetSection("Database");
        databaseConfiguration.GetValue("RunMigrationsOnStartup", true);

        using ServiceProvider serviceProvider = builder.Services.BuildServiceProvider();
        using IServiceScope scope = serviceProvider.CreateScope();
        ArduinoThermoHygrometerDbContext arduinoThermoHygrometerDbContext = scope.ServiceProvider.GetRequiredService<ArduinoThermoHygrometerDbContext>();

        bool isSqlServer = arduinoThermoHygrometerDbContext.Database.IsSqlServer();
        if (!isSqlServer)
        {
            throw new NotSupportedException("Database provider currently in use is not the SQL Server provider.");
        }

        arduinoThermoHygrometerDbContext.Database.Migrate();

        return builder;
    }

    /// <summary>
    /// Configures OpenTelemetry logging for the provided <see cref="WebApplicationBuilder"/> instance.
    /// </summary>
    /// <param name="builder">The <see cref="WebApplicationBuilder"/> to configure OpenTelemetry logging for.</param>
    /// <returns>The same <see cref="WebApplicationBuilder"/> instance, allowing for method chaining.</returns>
    /// <exception cref="ArgumentNullException">Thrown if the <paramref name="builder"/> is null.</exception>
    public static WebApplicationBuilder AddOpenTelemetryLogging(this WebApplicationBuilder builder)
    {
        ArgumentNullException.ThrowIfNull(builder, nameof(builder));

        builder.Logging.ClearProviders();

        Assembly? assembly = Assembly.GetAssembly(typeof(Program));

        if (!builder.Environment.IsProduction())
        {
            builder.Logging.AddOpenTelemetry(configure =>
            {
                configure.SetResourceBuilder(ResourceBuilder.CreateDefault()
                    .AddService(nameof(ArduinoThermoHygrometer))
                    .AddAttributes(new Dictionary<string, object>()
                    {
                        ["deployment.environment"] = builder.Environment.EnvironmentName,
                        ["service.version"] = assembly?.GetName()?.Version?.ToString() ?? string.Empty
                    }));

                configure.IncludeFormattedMessage = true;
                configure.IncludeScopes = true;
            });

            builder.Services.AddOpenTelemetry()
                .ConfigureResource(configure => configure.AddService(nameof(ArduinoThermoHygrometer)))
                .WithLogging(configure => configure.AddConsoleExporter());
        }
        // TODO: Add else statement with logging, metrics and tracing for production environment.

        return builder;
    }

    /// <summary>
    /// Creates a <see cref="ProblemDetails"/> instance for a rate limiter rejection.
    /// </summary>
    /// <param name="context">The context of the rejected request.</param>
    /// <param name="requestMethod">The HTTP method of the rejected request.</param>
    /// <param name="retryRequestAfter">The time in minutes after which the request can be retried.</param>
    /// <returns>A <see cref="ProblemDetails"/> instance containing details about the rate limit rejection.</returns>
    private static ProblemDetails CreateProblemDetailsForRateLimiter(OnRejectedContext context, string requestMethod, string retryRequestAfter)
    {
        return new()
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
    }

    /// <summary>
    /// Logs a warning message when a rate limit is reached.
    /// </summary>
    /// <param name="requestMethod">The HTTP method of the request that was rate limited.</param>
    /// <param name="requestPath">The path of the request that was rate limited.</param>
    /// <param name="retryRequestAfter">The time in minutes after which the request can be retried.</param>
    private static void LoggerForRateLimiter(string requestMethod, string requestPath, string retryRequestAfter)
    {
        using ILoggerFactory loggerFactory = LoggerFactory.Create(configure => configure.AddOpenTelemetry());

        ILogger logger = loggerFactory.CreateLogger("Rate limiter");
        logger.LogWarning("Rate limit reached for {RequestMethod} request to {RequestPath}. Please try again after {RetryRequestAfter} minute.",
            requestMethod, requestPath, retryRequestAfter);
    }
}
