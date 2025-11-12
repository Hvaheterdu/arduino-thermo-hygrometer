using ArduinoThermoHygrometer.Test.Configs;
using ArduinoThermoHygrometer.Test.Fixtures;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Integration.Repositories;

[Collection("HumidityRepository integration tests.")]
public class HumidityRepositoryTest : IntegrationTestBase
{
    public HumidityRepositoryTest(TestContainerFixture testContainerFixture) : base(testContainerFixture)
    {

    }
}
