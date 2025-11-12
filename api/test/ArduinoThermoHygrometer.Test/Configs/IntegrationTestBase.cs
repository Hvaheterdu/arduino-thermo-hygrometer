using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Test.Fixtures;
using ArduinoThermoHygrometer.Test.Helpers;
using Microsoft.Extensions.DependencyInjection;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Configs;

public abstract class IntegrationTestBase : IClassFixture<TestContainerFixture>, IAsyncLifetime
{
    internal readonly IServiceScope _serviceScope;
    internal readonly ArduinoThermoHygrometerDbContext _dbContext;
    internal readonly IntegrationTestDataSeeder _integrationTestDataSeeder;

    protected IntegrationTestBase(TestContainerFixture testContainerFixture)
    {
        ArgumentNullException.ThrowIfNull(testContainerFixture);

        _serviceScope = testContainerFixture.Services.CreateScope();
        _dbContext = _serviceScope.ServiceProvider.GetRequiredService<ArduinoThermoHygrometerDbContext>();
        _integrationTestDataSeeder = new IntegrationTestDataSeeder(_dbContext);
    }

    /// <summary>
    /// Asynchronously initializes the test class by seeding test data into the database.
    /// </summary>
    /// <returns>
    /// A <see cref="ValueTask"/> representing the asynchronous initialization operation.
    /// </returns>
    public async ValueTask InitializeAsync() => await _integrationTestDataSeeder.SeedAsync();

    /// <summary>
    /// Asynchronously disposes of the test resources, including the service scope and database context.
    /// </summary>
    /// <returns>
    /// A <see cref="ValueTask"/> representing the asynchronous disposal operation.
    /// </returns>
    public async ValueTask DisposeAsync()
    {
        _serviceScope.Dispose();
        await _dbContext.DisposeAsync();
        await Task.CompletedTask;
        GC.SuppressFinalize(this);
    }
}
