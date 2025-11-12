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

[Collection("HumidityService unit tests")]
public class HumidityServiceTest
{
    private readonly IHumidityRepository _humidityRepository;
    private readonly HumidityService _humidityService;
    private readonly FakeLogger<HumidityService> _fakeLogger;

    public HumidityServiceTest()
    {
        _humidityRepository = Substitute.For<IHumidityRepository>();
        _fakeLogger = new FakeLogger<HumidityService>();
        _humidityService = new HumidityService(_humidityRepository, _fakeLogger);
    }

    [Fact]
    public async Task GetHumidityDtoByIdAsync_Should_ReturnHumidityDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Humidity humidity = HumidityTestData.GetHumidityById(id);
        _humidityRepository.GetHumidityByIdAsync(id).Returns(humidity);

        HumidityDto? result = await _humidityService.GetHumidityDtoByIdAsync(id);

        string expectedLog = $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(humidity.Id, result?.Id);
    }

    [Fact]
    public async Task GetHumidityDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        HumidityDto? result = await _humidityService.GetHumidityDtoByIdAsync(id);

        string expectedLog = $"{id} is invalid id.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetHumidityDtoByIdAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        Guid id = Guid.NewGuid();
        _humidityRepository.GetHumidityByIdAsync(id).ReturnsNull();

        HumidityDto? result = await _humidityService.GetHumidityDtoByIdAsync(id);

        await _humidityRepository.Received(1).GetHumidityByIdAsync(id);
        string expectedLog = $"{nameof(Humidity)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetHumidityDtoByTimestampAsync_Should_ReturnHumidityDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Humidity humidity = HumidityTestData.GetHumidityByTimestamp(timestamp);
        _humidityRepository.GetHumidityByTimestampAsync(timestamp).Returns(humidity);

        HumidityDto? result = await _humidityService.GetHumidityDtoByTimestampAsync(timestamp);

        string expectedLog = $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(humidity.RegisteredAt, result?.RegisteredAt);
    }

    [Fact]
    public async Task GetHumidityDtoByTimestampAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityRepository.GetHumidityByTimestampAsync(timestamp).ReturnsNull();

        HumidityDto? result = await _humidityService.GetHumidityDtoByTimestampAsync(timestamp);

        await _humidityRepository.Received(1).GetHumidityByTimestampAsync(timestamp);
        string expectedLog = $"{nameof(Humidity)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetHumidityDtosByDateAsync_Should_ReturnHumidityDtos_When_FoundByDate()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Humidity> humidities = HumidityTestData.GetHumidityByDate(date);
        _humidityRepository.GetHumidityByDateAsync(date).Returns(humidities);

        IEnumerable<HumidityDto>? result = await _humidityService.GetHumidityDtosByDateAsync(date);

        string expectedLog = $"{nameof(HumidityDto)} with Id={result?.Last().Id} and RegisteredAt={result?.Last().RegisteredAt.Date.ToShortDateString()} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotEmpty(result!);
        Assert.Equal(humidities.Last().RegisteredAt, result?.Last().RegisteredAt);
    }

    [Fact]
    public async Task GetHumidityDtosByDateAsync_Should_ReturnNull_When_HumidityListIsEmpty()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Humidity> humidities = new List<Humidity>();
        _humidityRepository.GetHumidityByDateAsync(date).Returns(humidities);

        IEnumerable<HumidityDto>? result = await _humidityService.GetHumidityDtosByDateAsync(date);

        await _humidityRepository.Received(1).GetHumidityByDateAsync(date);
        string expectedLog = $"{nameof(Humidity)} for {date.Date:d} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetHumidityDtosByDateAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        _humidityRepository.GetHumidityByDateAsync(date).ReturnsNull();

        IEnumerable<HumidityDto>? result = await _humidityService.GetHumidityDtosByDateAsync(date);

        await _humidityRepository.Received(1).GetHumidityByDateAsync(date);
        string expectedLog = $"{nameof(Humidity)} for {date.Date:d} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task CreateHumidityDtoAsync_Should_ReturnHumidityDto_When_HumidityIsCreated()
    {
        HumidityDto? humidityDto = HumidityTestData.CreateValidHumidityDto();

        HumidityDto? result = await _humidityService.CreateHumidityDtoAsync(humidityDto);

        string expectedLog = $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is created.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(humidityDto.Id, result?.Id);
    }

    [Fact]
    public async Task CreateHumidityDtoAsync_Should_ReturnNull_When_HumidityDtoIsNull()
    {
        HumidityDto? result = await _humidityService.CreateHumidityDtoAsync(null);

        string expectedLog = $"{nameof(HumidityDto)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteHumidityDtoByIdAsync_Should_ReturnHumidityDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Humidity humidity = HumidityTestData.GetHumidityById(id);
        _humidityRepository.DeleteHumidityByIdAsync(id).Returns(humidity);

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByIdAsync(id);

        string expectedLog = $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(humidity.Id, result?.Id);
    }

    [Fact]
    public async Task DeleteHumidityDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByIdAsync(id);

        string expectedLog = $"{id} is invalid id.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteHumidityDtoByIdAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        Guid id = Guid.NewGuid();
        _humidityRepository.DeleteHumidityByIdAsync(id).ReturnsNull();

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByIdAsync(id);

        await _humidityRepository.Received(1).DeleteHumidityByIdAsync(id);
        string expectedLog = $"{nameof(Humidity)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteHumidityDtoByTimestampAsync_Should_ReturnHumidityDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Humidity humidity = HumidityTestData.GetHumidityByTimestamp(timestamp);
        _humidityRepository.DeleteHumidityByTimestampAsync(timestamp).Returns(humidity);

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp);

        await _humidityRepository.Received(1).DeleteHumidityByTimestampAsync(timestamp);
        string expectedLog = $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(humidity.Id, result?.Id);
    }

    [Fact]
    public async Task DeleteHumidityDtoByTimestampAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityRepository.DeleteHumidityByTimestampAsync(timestamp).ReturnsNull();

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp);

        await _humidityRepository.Received(1).DeleteHumidityByTimestampAsync(timestamp);
        string expectedLog = $"{nameof(Humidity)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }
}
