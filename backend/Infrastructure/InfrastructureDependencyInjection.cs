using ArduinoThermoHygrometer.Infrastructure.Data;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace ArduinoThermoHygrometer.Infrastructure;

public static class InfrastructureDependencyInjection
{
    /// <summary>
    /// Gets the name of the assembly.
    /// </summary>
    /// <returns>The name of the assembly.</returns>
    public static string GetAssemblyName()
    {
        return typeof(InfrastructureDependencyInjection).Assembly.GetName().Name!;
    }

    /// <summary>
    /// Adds infrastructure services to the specified <see cref="IServiceCollection"/>.
    /// </summary>
    /// <param name="services">The <see cref="IServiceCollection"/> to add the services to.</param>
    /// <param name="configuration">The <see cref="IConfiguration"/> instance.</param>
    public static void AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {
        string? databaseConnectionString = configuration.GetConnectionString("DefaultConnection")!;

        services.AddHealthChecks()
            .AddSqlServer(databaseConnectionString)
            .AddDbContextCheck<ArduinoThermoHygrometerDbContext>();
    }
}
