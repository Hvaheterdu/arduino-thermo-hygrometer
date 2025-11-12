using System.Data.Common;
using System.Runtime.InteropServices;
using ArduinoThermoHygrometer.Infrastructure.Data;
using DotNet.Testcontainers.Builders;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.AspNetCore.TestHost;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Testcontainers.MsSql;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Fixtures;

public class TestContainerFixture : WebApplicationFactory<Program>, IAsyncLifetime
{
    private readonly MsSqlContainer _msSqlContainer = RuntimeInformation.IsOSPlatform(OSPlatform.Linux)
        ? new MsSqlBuilder()
            .WithImage("mcr.microsoft.com/mssql/server:2022-latest")
            .WithPortBinding(5689, true)
            .WithEnvironment("SQLCMDUSER", "sa")
            .WithPassword("P@assw0rd1!")
            .WithEnvironment("ACCEPT_EULA", "Y")
            .WithWaitStrategy(Wait.ForUnixContainer()
                .UntilPortIsAvailable(5689)
                .UntilCommandIsCompleted("/opt/mssql-tools/bin/sqlcmd", "-U", "sa", "-P", "P@assw0rd1!", "-Q", "SELECT 1"))
            .WithCleanUp(true)
            .WithAutoRemove(true)
            .Build()
        : new MsSqlBuilder()
            .WithPortBinding(5689, true)
            .WithEnvironment("SQLCMDUSER", "sa")
            .WithPassword("P@assw0rd1!")
            .WithEnvironment("ACCEPT_EULA", "Y")
            .WithWaitStrategy(Wait.ForUnixContainer()
                .UntilPortIsAvailable(5689)
                .UntilCommandIsCompleted("/opt/mssql-tools/bin/sqlcmd", "-U", "sa", "-P", "P@assw0rd1!", "-Q", "SELECT 1"))
            .WithCleanUp(true)
            .WithAutoRemove(true)
            .Build();

    /// <summary>
    /// Initializes the test fixture asynchronously by starting the SQL Server container
    /// and applying the baseline SQL schema.
    /// </summary>
    /// <returns>A task representing the asynchronous initialization operation.</returns>
    public async ValueTask InitializeAsync()
    {
        await _msSqlContainer.StartAsync();
        string baseline = await File.ReadAllTextAsync("../Scripts/baseline.sql");
        await _msSqlContainer.ExecScriptAsync(baseline);
    }

    /// <summary>
    /// Disposes of the test fixture asynchronously by stopping and removing
    /// the SQL Server container.
    /// </summary>
    /// <returns>A task representing the asynchronous disposal operation.</returns>
    public new async Task DisposeAsync()
    {
        await _msSqlContainer.StopAsync();
        await _msSqlContainer.DisposeAsync();
    }

    /// <summary>
    /// Configures the test web host by overriding the application's DbContext
    /// to use the containerized SQL Server instance.
    /// </summary>
    /// <param name="builder">The web host builder used to configure the test server.</param>
    protected override void ConfigureWebHost(IWebHostBuilder builder)
    {
        builder.ConfigureTestServices(servicesConfiguration =>
        {
            ServiceDescriptor? dbOptionsDescriptor = servicesConfiguration.SingleOrDefault(
                defaultValue => defaultValue.ServiceType == typeof(DbContextOptions<ArduinoThermoHygrometerDbContext>));
            if (dbOptionsDescriptor is not null)
            {
                servicesConfiguration.Remove(dbOptionsDescriptor);
            }

            ServiceDescriptor? dbConnectionDescriptor = servicesConfiguration.SingleOrDefault(
                defaultValue => defaultValue.ServiceType == typeof(DbConnection));
            if (dbConnectionDescriptor is not null)
            {
                servicesConfiguration.Remove(dbConnectionDescriptor);
            }

            servicesConfiguration.AddDbContext<ArduinoThermoHygrometerDbContext>(
                optionsAction => optionsAction.UseSqlServer(_msSqlContainer.GetConnectionString(),
                    sqlServerOptionsAction => sqlServerOptionsAction.EnableRetryOnFailure(3)));
        });
    }
}
