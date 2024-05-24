using ArduinoThermometer.API.Data;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermometer.API;

public static class ProgramExtensions
{
    /// <summary>
    /// Verifies if the specified configuration key exists and optionally checks if the value matches the specified value.
    /// </summary>
    /// <param name="configuration">The configuration object.</param>
    /// <param name="key">The configuration key to verify.</param>
    /// <param name="value">The optional value to check against the configuration value.</param>
    /// <returns>The configuration object.</returns>
    /// <exception cref="ArgumentException"></exception>
    public static IConfiguration VerifyConfigurationExists(this IConfiguration configuration, string key, string? value = null)
    {
        string? configurationValue = value == null ? configuration.GetSection(key)?.Value : configuration.GetSection(key)[value!];

        if (configurationValue is null)
        {
            throw new ArgumentException($"Configuration with name {nameof(configuration)} does not exist.");
        }

        return configuration;
    }

    /// <summary>
    /// Registers the specified database context and runs the database migration from another project
    /// on application startup if configured to do so.
    /// </summary>
    /// <typeparam name="T">The type of database context.</typeparam>
    /// <param name="builder">The web application builder.</param>
    /// <returns>The web application builder.</returns>
    /// <exception cref="NotImplementedException"></exception>
    public static WebApplicationBuilder RegisterDatabaseAndRunMigrationsOnStartup<T>(this WebApplicationBuilder builder) where T : DbContext
    {
        builder.Services.AddDbContext<ArduinoThermometerDbContext>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

        bool.TryParse(builder.Configuration.GetSection("Database")["RunDatabaseMigrationOnStartup"], out bool runDatabaseMigrationOnStartup);

        if (runDatabaseMigrationOnStartup)
        {
            using IServiceScope scope = builder.Services.BuildServiceProvider().CreateScope();
            ArduinoThermometerDbContext arduinoThermometerDbContext = scope.ServiceProvider.GetRequiredService<ArduinoThermometerDbContext>();

            if (!arduinoThermometerDbContext.Database.CanConnect())
            {
                throw new NotImplementedException("Unable to connect to database. Check if the connection string is correct in appsettings.Development.json.");
            }

            arduinoThermometerDbContext.Database.Migrate();
        }

        return builder;
    }
}
