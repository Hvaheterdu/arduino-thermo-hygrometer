using ArduinoThermoHygrometer.Test.Configs;
using ArduinoThermoHygrometer.Test.Fixtures;
using Microsoft.Data.SqlClient;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Integration.Repositories;

[Collection("BatteryRepository integration tests.")]
public class BatteryRepositoryTest : IntegrationTestBase
{
    public BatteryRepositoryTest(TestContainerFixture testContainerFixture)
        : base(testContainerFixture)
    {
    }
}
