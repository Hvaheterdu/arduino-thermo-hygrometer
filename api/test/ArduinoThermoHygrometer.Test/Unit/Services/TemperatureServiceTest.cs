using System.Globalization;
using ArduinoThermoHygrometer.Core.Repositories.Contracts;
using ArduinoThermoHygrometer.Core.Services;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Test.Helpers;
using Microsoft.Extensions.Logging.Testing;
using NSubstitute;
using NSubstitute.ReturnsExtensions;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Unit.Services;

[Collection("TemperatureService unit tests")]
public class TemperatureServiceTest
{
    private readonly ITemperatureRepository _temperatureRepository;
    private readonly TemperatureService _temperatureService;
    private readonly FakeLogger<TemperatureService> _fakeLogger;

    public TemperatureServiceTest()
    {
        _temperatureRepository = Substitute.For<ITemperatureRepository>();
        _fakeLogger = new FakeLogger<TemperatureService>();
        _temperatureService = new TemperatureService(_temperatureRepository, _fakeLogger);
    }

    [Fact]
    public async Task GetTemperatureDtoByIdAsync_Should_ReturnTemperatureDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Temperature temperature = TemperatureTestData.GetTemperatureById(id);
        _temperatureRepository.GetTemperatureByIdAsync(id).Returns(temperature);

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByIdAsync(id);

        string expectedLog = $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(temperature.Id, result?.Id);
    }

    [Fact]
    public async Task GetTemperatureDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByIdAsync(id);

        string expectedLog = $"{id} is invalid id.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetTemperatureDtoByIdAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        Guid id = Guid.NewGuid();
        _temperatureRepository.GetTemperatureByIdAsync(id).ReturnsNull();

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByIdAsync(id);

        await _temperatureRepository.Received(1).GetTemperatureByIdAsync(id);
        string expectedLog = $"{nameof(Temperature)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetTemperatureDtoByTimestampAsync_Should_ReturnTemperatureDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Temperature temperature = TemperatureTestData.GetTemperatureByTimestamp(timestamp);
        _temperatureRepository.GetTemperatureByTimestampAsync(timestamp).Returns(temperature);

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp);

        string expectedLog = $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(temperature.RegisteredAt, result?.RegisteredAt);
    }

    [Fact]
    public async Task GetTemperatureDtoByTimestampAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureRepository.GetTemperatureByTimestampAsync(timestamp).ReturnsNull();

        TemperatureDto? result = await _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp);

        await _temperatureRepository.Received(1).GetTemperatureByTimestampAsync(timestamp);
        string expectedLog = $"{nameof(Temperature)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetTemperatureDtosByDateAsync_Should_ReturnTemperatureDtos_When_FoundByDate()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Temperature> temperatures = TemperatureTestData.GetTemperatureByDate(date);
        _temperatureRepository.GetTemperatureByDateAsync(date).Returns(temperatures);

        IEnumerable<TemperatureDto>? result = await _temperatureService.GetTemperatureDtosByDateAsync(date);

        string expectedLog = $"{nameof(TemperatureDto)} with Id={result?.Last().Id} and RegisteredAt={result?.Last().RegisteredAt.Date.ToShortDateString()} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotEmpty(result!);
        Assert.Equal(temperatures.Last().RegisteredAt, result?.Last().RegisteredAt);
    }

    [Fact]
    public async Task GetTemperatureDtosByDateAsync_Should_ReturnNull_When_TemperatureListIsEmpty()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Temperature> temperatures = new List<Temperature>();
        _temperatureRepository.GetTemperatureByDateAsync(date).Returns(temperatures);

        IEnumerable<TemperatureDto>? result = await _temperatureService.GetTemperatureDtosByDateAsync(date);

        await _temperatureRepository.Received(1).GetTemperatureByDateAsync(date);
        string expectedLog = $"{nameof(Temperature)} for {date.Date:d} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetTemperatureDtosByDateAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        _temperatureRepository.GetTemperatureByDateAsync(date).ReturnsNull();

        IEnumerable<TemperatureDto>? result = await _temperatureService.GetTemperatureDtosByDateAsync(date);

        await _temperatureRepository.Received(1).GetTemperatureByDateAsync(date);
        string expectedLog = $"{nameof(Temperature)} for {date.Date:d} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task CreateTemperatureDtoAsync_Should_ReturnTemperatureDto_When_TemperatureIsCreated()
    {
        TemperatureDto? temperatureDto = TemperatureTestData.CreateValidTemperatureDto();

        TemperatureDto? result = await _temperatureService.CreateTemperatureDtoAsync(temperatureDto);

        string expectedLog = $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is created.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(temperatureDto.Id, result?.Id);
    }

    [Fact]
    public async Task CreateTemperatureDtoAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        TemperatureDto? result = await _temperatureService.CreateTemperatureDtoAsync(null);

        string expectedLog = $"{nameof(TemperatureDto)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteTemperatureDtoByIdAsync_Should_ReturnTemperatureDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Temperature temperature = TemperatureTestData.GetTemperatureById(id);
        _temperatureRepository.DeleteTemperatureByIdAsync(id).Returns(temperature);

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByIdAsync(id);

        string expectedLog = $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(temperature.Id, result?.Id);
    }

    [Fact]
    public async Task DeleteTemperatureDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByIdAsync(id);

        string expectedLog = $"{id} is invalid id.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteTemperatureDtoByIdAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        Guid id = Guid.NewGuid();
        _temperatureRepository.DeleteTemperatureByIdAsync(id).ReturnsNull();

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByIdAsync(id);

        await _temperatureRepository.Received(1).DeleteTemperatureByIdAsync(id);
        string expectedLog = $"{nameof(Temperature)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteTemperatureDtoByTimestampAsync_Should_ReturnTemperatureDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Temperature temperature = TemperatureTestData.GetTemperatureByTimestamp(timestamp);
        _temperatureRepository.DeleteTemperatureByTimestampAsync(timestamp).Returns(temperature);

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp);

        string expectedLog = $"{nameof(TemperatureDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(temperature.Id, result?.Id);
    }

    [Fact]
    public async Task DeleteTemperatureDtoByTimestampAsync_Should_ReturnNull_When_TemperatureIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureRepository.DeleteTemperatureByTimestampAsync(timestamp).ReturnsNull();

        TemperatureDto? result = await _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp);

        await _temperatureRepository.Received(1).DeleteTemperatureByTimestampAsync(timestamp);
        string expectedLog = $"{nameof(Temperature)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }
}
