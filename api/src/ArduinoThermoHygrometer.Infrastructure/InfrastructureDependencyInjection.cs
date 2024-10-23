using ArduinoThermoHygrometer.Infrastructure.Data;
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
        string? connectionString = configuration.GetConnectionString("ArduinoThermoHygrometerLocal");
        if (connectionString is null)
        {
            throw new NotImplementedException("Connection string is not found in appsettings.Development.json");
        }

        IServiceCollection sqlServer = services.AddDbContext<ArduinoThermoHygrometerDbContext>(optionsAction =>
        {
            optionsAction.UseSqlServer(connectionString, sqlServerOptionsAction =>
            {
                sqlServerOptionsAction.EnableRetryOnFailure(3);
                sqlServerOptionsAction.CommandTimeout((int)TimeSpan.FromMinutes(5).TotalSeconds);
            });
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
