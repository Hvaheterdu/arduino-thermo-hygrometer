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

[Collection("BatteryService unit tests.")]
public class BatteryServiceTest
{
    private readonly IBatteryRepository _batteryRepository;
    private readonly BatteryService _batteryService;
    private readonly FakeLogger<BatteryService> _fakeLogger;

    public BatteryServiceTest()
    {
        _batteryRepository = Substitute.For<IBatteryRepository>();
        _fakeLogger = new FakeLogger<BatteryService>();
        _batteryService = new BatteryService(_batteryRepository, _fakeLogger);
    }

    [Fact]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnBatteryDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Battery battery = BatteryTestData.GetBatteryById(id);
        _batteryRepository.GetBatteryByIdAsync(id).Returns(battery);

        BatteryDto? result = await _batteryService.GetBatteryDtoByIdAsync(id);

        string expectedLog =
            $"{nameof(BatteryDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(battery.Id, result?.Id);
    }

    [Fact]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        BatteryDto? result = await _batteryService.GetBatteryDtoByIdAsync(id);

        string expectedLog = $"{id} is invalid id.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetBatteryDtoByIdAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        Guid id = Guid.NewGuid();
        _batteryRepository.GetBatteryByIdAsync(id).ReturnsNull();

        BatteryDto? result = await _batteryService.GetBatteryDtoByIdAsync(id);

        await _batteryRepository.Received(1).GetBatteryByIdAsync(id);
        string expectedLog = $"{nameof(Battery)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetBatteryDtoByTimestampAsync_Should_ReturnBatteryDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Battery battery = BatteryTestData.GetBatteryByTimestamp(timestamp);
        _batteryRepository.GetBatteryByTimestampAsync(timestamp).Returns(battery);

        BatteryDto? result = await _batteryService.GetBatteryDtoByTimestampAsync(timestamp);

        string expectedLog =
            $"{nameof(BatteryDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(battery.RegisteredAt, result?.RegisteredAt);
    }

    [Fact]
    public async Task GetBatteryDtoByTimestampAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryRepository.GetBatteryByTimestampAsync(timestamp).ReturnsNull();

        BatteryDto? result = await _batteryService.GetBatteryDtoByTimestampAsync(timestamp);

        await _batteryRepository.Received(1).GetBatteryByTimestampAsync(timestamp);
        string expectedLog = $"{nameof(Battery)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetBatteryDtosByDateAsync_Should_ReturnBatteryDtos_When_FoundByDate()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Battery> batteries = BatteryTestData.GetBatteryByDate(date);
        _batteryRepository.GetBatteryByDateAsync(date).Returns(batteries);

        IEnumerable<BatteryDto>? result = await _batteryService.GetBatteryDtosByDateAsync(date);

        string expectedLog =
            $"{nameof(BatteryDto)} with Id={result?.Last().Id} and RegisteredAt={result?.Last().RegisteredAt.Date.ToShortDateString()} is found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotEmpty(result!);
        Assert.Equal(batteries.Last().RegisteredAt, result?.Last().RegisteredAt);
    }

    [Fact]
    public async Task GetBatteryDtosByDateAsync_Should_ReturnNull_When_BatteryListIsEmpty()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Battery> batteries = Enumerable.Empty<Battery>();
        _batteryRepository.GetBatteryByDateAsync(date).Returns(batteries);

        IEnumerable<BatteryDto>? result = await _batteryService.GetBatteryDtosByDateAsync(date);

        await _batteryRepository.Received(1).GetBatteryByDateAsync(date);
        string expectedLog = $"{nameof(Battery)} for {date.Date:d} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task GetBatteryDtosByDateAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        _batteryRepository.GetBatteryByDateAsync(date).ReturnsNull();

        IEnumerable<BatteryDto>? result = await _batteryService.GetBatteryDtosByDateAsync(date);

        await _batteryRepository.Received(1).GetBatteryByDateAsync(date);
        string expectedLog = $"{nameof(Battery)} for {date.Date:d} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task CreateBatteryDtoAsync_Should_ReturnBatteryDto_When_BatteryIsCreated()
    {
        BatteryDto batteryDto = BatteryTestData.CreateValidBatteryDto();

        BatteryDto? result = await _batteryService.CreateBatteryDtoAsync(batteryDto);

        string expectedLog =
            $"{nameof(BatteryDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is created.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(batteryDto.Id, result?.Id);
    }

    [Fact]
    public async Task CreateBatteryDtoAsync_Should_ReturnNull_When_BatteryDtoIsNull()
    {
        BatteryDto? result = await _batteryService.CreateBatteryDtoAsync(null);

        string expectedLog = $"{nameof(BatteryDto)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteBatteryDtoByIdAsync_Should_ReturnBatteryDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Battery battery = BatteryTestData.GetBatteryById(id);
        _batteryRepository.DeleteBatteryByIdAsync(id).Returns(battery);

        BatteryDto? result = await _batteryService.DeleteBatteryDtoByIdAsync(id);

        string expectedLog =
            $"{nameof(BatteryDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(battery.Id, result?.Id);
    }

    [Fact]
    public async Task DeleteBatteryDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        BatteryDto? result = await _batteryService.DeleteBatteryDtoByIdAsync(id);

        string expectedLog = $"{id} is invalid id.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteBatteryDtoByIdAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        Guid id = Guid.NewGuid();
        _batteryRepository.DeleteBatteryByIdAsync(id).ReturnsNull();

        BatteryDto? result = await _batteryService.DeleteBatteryDtoByIdAsync(id);

        await _batteryRepository.Received(1).DeleteBatteryByIdAsync(id);
        string expectedLog = $"{nameof(Battery)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }

    [Fact]
    public async Task DeleteBatteryDtoByTimestampAsync_Should_ReturnBatteryDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Battery battery = BatteryTestData.GetBatteryByTimestamp(timestamp);
        _batteryRepository.DeleteBatteryByTimestampAsync(timestamp).Returns(battery);

        BatteryDto? result = await _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp);

        await _batteryRepository.Received(1).DeleteBatteryByTimestampAsync(timestamp);
        await _batteryRepository.Received(1).SaveChangesAsync();
        string expectedLog =
            $"{nameof(BatteryDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.NotNull(result);
        Assert.Equal(battery.Id, result?.Id);
    }

    [Fact]
    public async Task DeleteBatteryDtoByTimestampAsync_Should_ReturnNull_When_BatteryIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryRepository.DeleteBatteryByTimestampAsync(timestamp).ReturnsNull();

        BatteryDto? result = await _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp);

        await _batteryRepository.Received(1).DeleteBatteryByTimestampAsync(timestamp);
        string expectedLog = $"{nameof(Battery)} not found.";
        Assert.Equal(expectedLog, _fakeLogger.Collector.LatestRecord.Message);
        Assert.Null(result);
    }
}
