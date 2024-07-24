using ArduinoThermoHygrometer.Infrastructure.Data;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace ArduinoThermoHygrometer.Infrastructure;
public static class DependencyInjection
{
    public static void AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {

        string? databaseConnectionString = configuration.GetConnectionString("DefaultConnection")!;

        services.AddHealthChecks()
            .AddSqlServer(databaseConnectionString)
            .AddDbContextCheck<ArduinoThermoHygrometerDbContext>();
    }
}
