using ArduinoThermoHygrometer.Data;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer;

public static class ProgramExtensions
{

    /// <summary>
    /// Verifies the configuration value for the specified key.
    /// </summary>
    /// <param name="configuration">The IConfiguration instance.</param>
    /// <param name="key">The key of the configuration value.</param>
    /// <param name="value">The optional value of the configuration section.</param>
    /// <returns>The updated IConfiguration instance.</returns>
    /// <exception cref="ArgumentException">Thrown when the configuration value does not exist.</exception>
    public static IConfiguration VerifyConfiguration(this IConfiguration configuration, string key, string? value = null)
    {
        string? configurationValue = value == null ? configuration.GetSection(key)?.Value : configuration.GetSection(key)[value!];

        if (configurationValue is null)
        {
            throw new ArgumentException($"Configuration with name {nameof(configuration)} does not exist.");
        }

        return configuration;
    }

    /// <summary>
    /// Registers the database and runs migrations on application startup.
    /// </summary>
    /// <typeparam name="T">The type of the DbContext.</typeparam>
    /// <param name="builder">The WebApplicationBuilder instance.</param>
    /// <returns>The updated WebApplicationBuilder instance.</returns>
    /// <exception cref="NotImplementedException">Thrown when unable to connect to the database.</exception>"
    /// <exception cref="NotImplementedException">Thrown when the database provider is not SQL Server.</exception>"
    public static WebApplicationBuilder RegisterDatabaseAndRunMigrationsOnStartup<T>(this WebApplicationBuilder builder) where T : DbContext
    {
        builder.Services.AddDbContext<ArduinoThermoHygrometerDbContext>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

        bool.TryParse(builder.Configuration.GetSection("Database")["RunDatabaseMigrationsOnStartup"], out bool runDatabaseMigrationOnStartup);

        if (runDatabaseMigrationOnStartup)
        {
            using IServiceScope scope = builder.Services.BuildServiceProvider().CreateScope();
            ArduinoThermoHygrometerDbContext arduinoThermoHygrometerDbContext = scope.ServiceProvider.GetRequiredService<ArduinoThermoHygrometerDbContext>();

            bool attemptDatabaseConnection = arduinoThermoHygrometerDbContext.Database.CanConnect();
            if (!attemptDatabaseConnection)
            {
                arduinoThermoHygrometerDbContext.Database.EnsureCreated();
                arduinoThermoHygrometerDbContext.Database.Migrate();
            }

            if (!attemptDatabaseConnection)
            {
                throw new NotImplementedException("Unable to connect to database. Check if the connection string is correct in appsettings.Development.json.");
            }

            bool isSqlServer = arduinoThermoHygrometerDbContext.Database.IsSqlServer();
            if (!isSqlServer)
            {
                throw new NotImplementedException("Database provider is not SQL Server.");
            }
        }

        return builder;
    }
}
