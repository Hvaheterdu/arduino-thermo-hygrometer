using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Infrastructure.Options;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace ArduinoThermoHygrometer.Infrastructure;

public static class InfrastructureDependencyInjection
{
    /// <summary>
    /// Add SQL Server database configuration to the specified IServiceCollection.
    /// </summary>
    /// <param name="services">The <see cref="IServiceCollection"/> to add the services to.</param>
    /// <param name="configuration">The <see cref="IConfiguration"/> instance.</param>
    /// <returns>The modified <see cref="IServiceCollection"/>.</returns>
    public static IServiceCollection AddSqlServer(this IServiceCollection services, IConfiguration configuration)
    {
        DatabaseOptions databaseOptions = new();
        configuration.GetSection(DatabaseOptions.SectionName).Bind(databaseOptions);

        ConnectionStringsOptions connectionStringsOptions = new();
        configuration.GetSection(ConnectionStringsOptions.SectionName).Bind(connectionStringsOptions);

        string connectionString = connectionStringsOptions.ArduinoThermoHygrometerLocal;
        if (connectionString is null)
        {
            throw new NotImplementedException("Connection string is not found in appsettings.Development.json");
        }

        IServiceCollection sqlServer = services.AddDbContext<ArduinoThermoHygrometerDbContext>(optionsAction =>
        {
            optionsAction.UseSqlServer(connectionString, sqlServerOptionsAction => sqlServerOptionsAction.EnableRetryOnFailure(databaseOptions.RetryOnFailureAttempts));
        });

        return sqlServer;
    }

    /// <summary>
    /// Add HealthCheck services to the specified IServiceCollection.
    /// </summary>
    /// <param name="services">The <see cref="IServiceCollection"/> to add the services to.</param>
    /// <param name="configuration">The <see cref="IConfiguration"/> instance.</param>
    public static void AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {
        string? connectionString = configuration.GetConnectionString("ArduinoThermoHygrometerLocal")!;

        services.AddHealthChecks()
            .AddSqlServer(connectionString)
            .AddDbContextCheck<ArduinoThermoHygrometerDbContext>();
    }
}
