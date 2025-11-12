using ArduinoThermoHygrometer.Test.Configs;
using ArduinoThermoHygrometer.Test.Fixtures;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Integration.Repositories;

[Collection("TemperatureRepository integration tests.")]
public class TemperatureRepositoryTest : IntegrationTestBase
{
    public TemperatureRepositoryTest(TestContainerFixture testContainerFixture) : base(testContainerFixture)
    {

    }
}
