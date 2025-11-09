using System.Globalization;
using ArduinoThermoHygrometer.Core.Repositories.Contracts;
using ArduinoThermoHygrometer.Core.Services;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Test.Helpers;
using Microsoft.Extensions.Logging.Testing;
using NSubstitute;
using NSubstitute.ReturnsExtensions;
using NUnit.Framework;

namespace ArduinoThermoHygrometer.Test.Unit.Services;

[TestFixture]
[FixtureLifeCycle(LifeCycle.InstancePerTestCase)]
public class TemperatureServiceTest
{
    private ITemperatureRepository _temperatureRepository;
    private FakeLogger<TemperatureService> _fakeLogger;
    private TemperatureService _temperatureService;

    [SetUp]
    public void Init()
    {
        _temperatureRepository = Substitute.For<ITemperatureRepository>();
        _fakeLogger = new FakeLogger<TemperatureService>();
        _temperatureService = new TemperatureService(_temperatureRepository, _fakeLogger);
    }

    [Test]
    public async Task GetTemperatureDtoByIdAsync_Should_ReturnTemperatureDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Temperature temperature = TemperatureTestData.GetTemperatureById(id);
        _temperatureRepository.GetTemperatureByIdAsync(id).Returns(temperature);

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByIdAsync(id);

        string logDtoObjectToReturn =
            $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(temperature.Id));
        });
    }

    [Test]
    public async Task GetTemperatureDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByIdAsync(id);

        string logInvalidId = $"{id} is invalid id.";
        Assert.Multiple(() =>
        {
            Assert.That(logInvalidId, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetTemperatureDtoByIdAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        Guid id = Guid.NewGuid();
        _temperatureRepository.GetTemperatureByIdAsync(id).ReturnsNull();

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByIdAsync(id);

        await _temperatureRepository.Received(1).GetTemperatureByIdAsync(Arg.Is(id));
        string logIsNull = $"{nameof(Temperature)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetTemperatureDtoByTimestampAsync_Should_ReturnTemperatureDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Temperature temperature = TemperatureTestData.GetTemperatureByTimestamp(timestamp);
        _temperatureRepository.GetTemperatureByTimestampAsync(timestamp).Returns(temperature);

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp);

        string logDtoObjectToReturn =
            $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.RegisteredAt, Is.EqualTo(temperature.RegisteredAt));
        });
    }

    [Test]
    public async Task GetTemperatureDtoByTimestampAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureRepository.GetTemperatureByTimestampAsync(timestamp).ReturnsNull();

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp);

        await _temperatureRepository.Received(1).GetTemperatureByTimestampAsync(Arg.Is(timestamp));
        string logIsNull = $"{nameof(Temperature)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetTemperatureDtoByDateAsync_Should_ReturnTemperatureDto_When_FoundByDate()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Temperature> temperatures = TemperatureTestData.GetTemperatureByDate(date);
        _temperatureRepository.GetTemperatureByDateAsync(date).Returns(temperatures);

        IEnumerable<TemperatureDto>? result = await _temperatureService.GetTemperatureDtosByDateAsync(date);

        string logDtoObjectToReturn =
            $"{nameof(TemperatureDto)} with Id={result?.Last().Id} and RegisteredAt={result?.Last().RegisteredAt.Date.ToShortDateString()} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Empty);
            Assert.That(result?.Last().RegisteredAt, Is.EqualTo(temperatures.Last().RegisteredAt));
        });
    }

    [Test]
    public async Task GetTemperatureDtoByDateAsync_Should_ReturnNull_When_TemperatureListIsEmpty()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Temperature> temperatures = [];
        _temperatureRepository.GetTemperatureByDateAsync(date).Returns(temperatures);

        IEnumerable<TemperatureDto>? result = await _temperatureService.GetTemperatureDtosByDateAsync(date);

        await _temperatureRepository.Received(1).GetTemperatureByDateAsync(Arg.Is(date));
        string logIsNullOrEmpty = $"{nameof(Temperature)} for {date.Date:d} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNullOrEmpty, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetTemperatureDtoByDateAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        _temperatureRepository.GetTemperatureByDateAsync(date).ReturnsNull();

        IEnumerable<TemperatureDto>? result = await _temperatureService.GetTemperatureDtosByDateAsync(date);

        await _temperatureRepository.Received(1).GetTemperatureByDateAsync(Arg.Is(date));
        string logIsNullOrEmpty = $"{nameof(Temperature)} for {date.Date:d} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNullOrEmpty, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task CreateTemperatureDtoAsync_Should_ReturnTemperatureDto_When_TemperatureIsCreated()
    {
        TemperatureDto? temperatureDto = TemperatureTestData.CreateValidTemperatureDto();

        TemperatureDto? result = await _temperatureService.CreateTemperatureDtoAsync(temperatureDto);

        string LogDtoObjectToCreate =
            $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is created.";
        Assert.Multiple(() =>
        {
            Assert.That(LogDtoObjectToCreate, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(temperatureDto.Id));
        });
    }

    [Test]
    public async Task CreateTemperatureDtoAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        TemperatureDto? result = await _temperatureService.CreateTemperatureDtoAsync(null);

        string logIsNull = $"{nameof(TemperatureDto)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task DeleteTemperatureDtoByIdAsync_Should_ReturnTemperatureDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Temperature temperature = TemperatureTestData.GetTemperatureById(id);
        _temperatureRepository.DeleteTemperatureByIdAsync(id).Returns(temperature);

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByIdAsync(id);

        string LogDtoObjectToDelete =
            $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Multiple(() =>
        {
            Assert.That(LogDtoObjectToDelete, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(temperature.Id));
        });
    }

    [Test]
    public async Task DeleteTemperatureDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByIdAsync(id);

        string logIsNull = $"{id} is invalid id.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task DeleteTemperatureDtoByIdAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        Guid id = Guid.NewGuid();
        _temperatureRepository.DeleteTemperatureByIdAsync(id).ReturnsNull();

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByIdAsync(id);

        await _temperatureRepository.Received(1).DeleteTemperatureByIdAsync(Arg.Is(id));
        string logIsNull = $"{nameof(Temperature)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task DeleteTemperatureDtoByTimestampAsync_Should_ReturnTemperatureDto_When_FoundById()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Temperature temperature = TemperatureTestData.GetTemperatureByTimestamp(timestamp);
        _temperatureRepository.DeleteTemperatureByTimestampAsync(timestamp).Returns(temperature);

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp);

        string LogDtoObjectToDelete =
            $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Multiple(() =>
        {
            Assert.That(LogDtoObjectToDelete, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(temperature.Id));
        });
    }

    [Test]
    public async Task DeleteTemperatureDtoByTimestampAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureRepository.DeleteTemperatureByTimestampAsync(timestamp).ReturnsNull();

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp);

        await _temperatureRepository.Received(1).DeleteTemperatureByTimestampAsync(Arg.Is(timestamp));
        string logIsNull = $"{nameof(Temperature)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }
}
