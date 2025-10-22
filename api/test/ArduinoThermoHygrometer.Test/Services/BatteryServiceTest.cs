﻿using System.Globalization;
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
        Guid id = Guid.NewGuid();
        Battery battery = BatteryTestData.GetBatteryById(id);
        _batteryRepository.GetBatteryByIdAsync(id).Returns(battery);

        BatteryDto? result = await _batteryService.GetBatteryDtoByIdAsync(id);

        string logDtoObjectToReturn =
            $"{nameof(BatteryDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(battery.Id));
        });
    }

    [Test]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;
        _batteryRepository.GetBatteryByIdAsync(id).ReturnsNull();

        BatteryDto? result = await _batteryService.GetBatteryDtoByIdAsync(id);

        string logInvalidId = $"{id} is invalid id.";
        Assert.Multiple(() =>
        {
            Assert.That(logInvalidId, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        Guid id = Guid.NewGuid();
        _batteryRepository.GetBatteryByIdAsync(id).ReturnsNull();

        BatteryDto? result = await _batteryService.GetBatteryDtoByIdAsync(id);

        string logIsNull = $"{nameof(Battery)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetBatteryDtoByTimestampAsync_Should_ReturnBatteryDto_When_BatteryDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Battery battery = BatteryTestData.GetBatteryByTimestamp(timestamp);
        _batteryRepository.GetBatteryByTimestampAsync(timestamp).Returns(battery);

        BatteryDto? result = await _batteryService.GetBatteryDtoByTimestampAsync(timestamp);

        string logDtoObjectToReturn =
            $"{nameof(BatteryDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.RegisteredAt, Is.EqualTo(battery.RegisteredAt));
        });
    }

    [Test]
    public async Task GetBatteryDtoByTimestampAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryRepository.GetBatteryByTimestampAsync(timestamp).ReturnsNull();

        BatteryDto? result = await _batteryService.GetBatteryDtoByTimestampAsync(timestamp);

        string logIsNull = $"{nameof(Battery)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetBatteryDtoByDateAsync_Should_ReturnBatteryDto_When_BatteryDtoFoundByDate()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Battery> battery = BatteryTestData.GetBatteryByDate(date);
        _batteryRepository.GetBatteryByDateAsync(date).Returns(battery);

        IEnumerable<BatteryDto>? result = await _batteryService.GetBatteryDtosByDateAsync(date);

        string logDtoObjectToReturn =
            $"{nameof(BatteryDto)} with Id={result?.Last().Id} and RegisteredAt={result?.Last().RegisteredAt.Date.ToShortDateString()} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Empty);
            Assert.That(result?.Last().RegisteredAt, Is.EqualTo(battery.Last().RegisteredAt));
        });
    }

    [Test]
    public async Task GetBatteryDtoByDateAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        _batteryRepository.GetBatteryByDateAsync(date).ReturnsNull();

        IEnumerable<BatteryDto>? result = await _batteryService.GetBatteryDtosByDateAsync(date);

        string logIsNullOrEmpty = $"{nameof(Battery)} for {date.Date.ToShortDateString()} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNullOrEmpty, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetBatteryDtoByDateAsync_Should_ReturnNull_When_BatteryListIsEmpty()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Battery> battery = Enumerable.Empty<Battery>();
        _batteryRepository.GetBatteryByDateAsync(date).Returns(battery);

        IEnumerable<BatteryDto>? result = await _batteryService.GetBatteryDtosByDateAsync(date);

        string logIsNullOrEmpty = $"{nameof(Battery)} for {date.Date.ToShortDateString()} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNullOrEmpty, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }
}
