using ArduinoThermoHygrometer.Core.Repositories.Contracts;
using ArduinoThermoHygrometer.Core.Services;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Test.Data;
using Microsoft.Extensions.Logging.Testing;
using NSubstitute;
using NSubstitute.ReturnsExtensions;
using NUnit.Framework;

namespace ArduinoThermoHygrometer.Test.Services;

[TestFixture]
[FixtureLifeCycle(LifeCycle.InstancePerTestCase)]
public class BatteryServiceTest
{
    private IBatteryRepository _batteryRepository;
    private FakeLogger<BatteryService> _fakeLogger;
    private BatteryService _batteryService;

    [SetUp]
    public void Init()
    {
        _batteryRepository = Substitute.For<IBatteryRepository>();
        _fakeLogger = new FakeLogger<BatteryService>();
        _batteryService = new BatteryService(_batteryRepository, _fakeLogger);
    }

    [Test]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnBatteryDto_When_BatteryDtoFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        Battery battery = BatteryTestData.GetBatteryTestObjectById(id);
        _batteryRepository.GetBatteryByIdAsync(id).Returns(battery);

        // Act
        BatteryDto? act = await _batteryService.GetBatteryDtoByIdAsync(id);

        // Assert
        string logDtoObjectToReturn =
            $"{nameof(BatteryDto)} with Id={act?.Id} and RegisteredAt={act?.RegisteredAt.Date.ToShortDateString()} is found.";
        Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
        Assert.That(act, Is.Not.Null);
        Assert.That(act.Id, Is.EqualTo(battery.Id));
    }

    [Test]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        // Arrange
        Guid id = Guid.Empty;
        Battery battery = BatteryTestData.GetBatteryTestObjectById(id);
        _batteryRepository.GetBatteryByIdAsync(id).Returns(battery);

        // Act
        BatteryDto? act = await _batteryService.GetBatteryDtoByIdAsync(id);

        // Assert
        string logInvalidId = $"{id} is invalid id.";
        Assert.That(logInvalidId, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
        Assert.That(act, Is.Null);
    }

    [Test]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        _batteryRepository.GetBatteryByIdAsync(id).ReturnsNull();

        // Act
        BatteryDto? act = await _batteryService.GetBatteryDtoByIdAsync(id);

        // Assert
        string logIsNull = $"{nameof(Battery)} not found.";
        Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
        Assert.That(act, Is.Null);
    }
}
