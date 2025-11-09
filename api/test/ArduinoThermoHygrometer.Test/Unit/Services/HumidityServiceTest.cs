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
public class HumidityServiceTest
{
    private IHumidityRepository _humidityRepository;
    private FakeLogger<HumidityService> _fakeLogger;
    private HumidityService _humidityService;

    [SetUp]
    public void Init()
    {
        _humidityRepository = Substitute.For<IHumidityRepository>();
        _fakeLogger = new FakeLogger<HumidityService>();
        _humidityService = new HumidityService(_humidityRepository, _fakeLogger);
    }

    [Test]
    public async Task GetHumidityDtoByIdAsync_Should_ReturnHumidityDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Humidity humidity = HumidityTestData.GetHumidityById(id);
        _humidityRepository.GetHumidityByIdAsync(id).Returns(humidity);

        HumidityDto? result = await _humidityService.GetHumidityDtoByIdAsync(id);

        string logDtoObjectToReturn =
            $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(humidity.Id));
        });
    }

    [Test]
    public async Task GetHumidityDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        HumidityDto? result = await _humidityService.GetHumidityDtoByIdAsync(id);

        string logInvalidId = $"{id} is invalid id.";
        Assert.Multiple(() =>
        {
            Assert.That(logInvalidId, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetHumidityDtoByIdAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        Guid id = Guid.NewGuid();
        _humidityRepository.GetHumidityByIdAsync(id).ReturnsNull();

        HumidityDto? result = await _humidityService.GetHumidityDtoByIdAsync(id);

        await _humidityRepository.Received(1).GetHumidityByIdAsync(Arg.Is(id));
        string logIsNull = $"{nameof(Humidity)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetHumidityDtoByTimestampAsync_Should_ReturnHumidityDto_When_FoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Humidity humidity = HumidityTestData.GetHumidityByTimestamp(timestamp);
        _humidityRepository.GetHumidityByTimestampAsync(timestamp).Returns(humidity);

        HumidityDto? result = await _humidityService.GetHumidityDtoByTimestampAsync(timestamp);

        string logDtoObjectToReturn =
            $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.RegisteredAt, Is.EqualTo(humidity.RegisteredAt));
        });
    }

    [Test]
    public async Task GetHumidityDtoByTimestampAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityRepository.GetHumidityByTimestampAsync(timestamp).ReturnsNull();

        HumidityDto? result = await _humidityService.GetHumidityDtoByTimestampAsync(timestamp);

        await _humidityRepository.Received(1).GetHumidityByTimestampAsync(Arg.Is(timestamp));
        string logIsNull = $"{nameof(Humidity)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetHumidityDtoByDateAsync_Should_ReturnHumidityDto_When_FoundByDate()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Humidity> humidities = HumidityTestData.GetHumidityByDate(date);
        _humidityRepository.GetHumidityByDateAsync(date).Returns(humidities);

        IEnumerable<HumidityDto>? result = await _humidityService.GetHumidityDtosByDateAsync(date);

        string logDtoObjectToReturn =
            $"{nameof(HumidityDto)} with Id={result?.Last().Id} and RegisteredAt={result?.Last().RegisteredAt.Date.ToShortDateString()} is found.";
        Assert.Multiple(() =>
        {
            Assert.That(logDtoObjectToReturn, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Empty);
            Assert.That(result?.Last().RegisteredAt, Is.EqualTo(humidities.Last().RegisteredAt));
        });
    }

    [Test]
    public async Task GetHumidityDtoByDateAsync_Should_ReturnNull_When_HumidityListIsEmpty()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        IEnumerable<Humidity> humidities = [];
        _humidityRepository.GetHumidityByDateAsync(date).Returns(humidities);

        IEnumerable<HumidityDto>? result = await _humidityService.GetHumidityDtosByDateAsync(date);

        await _humidityRepository.Received(1).GetHumidityByDateAsync(Arg.Is(date));
        string logIsNullOrEmpty = $"{nameof(Humidity)} for {date.Date:d} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNullOrEmpty, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task GetHumidityDtoByDateAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        DateTimeOffset date = DateTimeOffset.Now;
        _humidityRepository.GetHumidityByDateAsync(date).ReturnsNull();

        IEnumerable<HumidityDto>? result = await _humidityService.GetHumidityDtosByDateAsync(date);

        await _humidityRepository.Received(1).GetHumidityByDateAsync(Arg.Is(date));
        string logIsNullOrEmpty = $"{nameof(Humidity)} for {date.Date:d} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNullOrEmpty, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task CreateHumidityDtoAsync_Should_ReturnHumidityDto_When_HumidityIsCreated()
    {
        HumidityDto? humidityDto = HumidityTestData.CreateValidHumidityDto();

        HumidityDto? result = await _humidityService.CreateHumidityDtoAsync(humidityDto);

        string LogDtoObjectToCreate =
            $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is created.";
        Assert.Multiple(() =>
        {
            Assert.That(LogDtoObjectToCreate, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(humidityDto.Id));
        });
    }

    [Test]
    public async Task CreateHumidityDtoAsync_Should_ReturnNull_When_HumidityDtoIsNull()
    {
        HumidityDto? result = await _humidityService.CreateHumidityDtoAsync(null);

        string logIsNull = $"{nameof(HumidityDto)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task DeleteHumidityDtoByIdAsync_Should_ReturnHumidityDto_When_FoundById()
    {
        Guid id = Guid.NewGuid();
        Humidity humidity = HumidityTestData.GetHumidityById(id);
        _humidityRepository.DeleteHumidityByIdAsync(id).Returns(humidity);

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByIdAsync(id);

        string LogDtoObjectToDelete =
            $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Multiple(() =>
        {
            Assert.That(LogDtoObjectToDelete, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(humidity.Id));
        });
    }

    [Test]
    public async Task DeleteHumidityDtoByIdAsync_Should_ReturnNull_When_IdIsEmpty()
    {
        Guid id = Guid.Empty;

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByIdAsync(id);

        string logIsNull = $"{id} is invalid id.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task DeleteHumidityDtoByIdAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        Guid id = Guid.NewGuid();
        _humidityRepository.DeleteHumidityByIdAsync(id).ReturnsNull();

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByIdAsync(id);

        await _humidityRepository.Received(1).DeleteHumidityByIdAsync(Arg.Is(id));
        string logIsNull = $"{nameof(Humidity)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }

    [Test]
    public async Task DeleteHumidityDtoByTimestampAsync_Should_ReturnHumidityDto_When_FoundById()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        Humidity humidity = HumidityTestData.GetHumidityByTimestamp(timestamp);
        _humidityRepository.DeleteHumidityByTimestampAsync(timestamp).Returns(humidity);

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp);

        string LogDtoObjectToDelete =
            $"{nameof(HumidityDto)} with Id={result?.Id} and RegisteredAt={result?.RegisteredAt.ToString(CultureInfo.InvariantCulture)} is deleted.";
        Assert.Multiple(() =>
        {
            Assert.That(LogDtoObjectToDelete, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Not.Null);
            Assert.That(result?.Id, Is.EqualTo(humidity.Id));
        });
    }

    [Test]
    public async Task DeleteHumidityDtoByTimestampAsync_Should_ReturnNull_When_HumidityIsNull()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityRepository.DeleteHumidityByTimestampAsync(timestamp).ReturnsNull();

        HumidityDto? result = await _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp);

        await _humidityRepository.Received(1).DeleteHumidityByTimestampAsync(Arg.Is(timestamp));
        string logIsNull = $"{nameof(Humidity)} not found.";
        Assert.Multiple(() =>
        {
            Assert.That(logIsNull, Is.EqualTo(_fakeLogger.Collector.LatestRecord.Message));
            Assert.That(result, Is.Null);
        });
    }
}
