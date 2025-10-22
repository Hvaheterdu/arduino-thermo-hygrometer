using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Test.Data;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NSubstitute;
using NSubstitute.ReturnsExtensions;
using NUnit.Framework;

namespace ArduinoThermoHygrometer.Test.Controllers;

[TestFixture]
[FixtureLifeCycle(LifeCycle.InstancePerTestCase)]
public class TemperatureControllerTest
{
    private ITemperatureService _temperatureService = null!;
    private TemperatureController _temperatureController = null!;

    [SetUp]
    public void Init()
    {
        _temperatureService = Substitute.For<ITemperatureService>();
        _temperatureController = new TemperatureController(_temperatureService);
    }

    [Test]
    public async Task GetByIdAsync_Should_Return200OK_When_TemperatureDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoById(id);
        _temperatureService.GetTemperatureDtoByIdAsync(id).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.GetByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByIdAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _temperatureService.GetTemperatureDtoByIdAsync(id).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.GetByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return200OK_When_TemperatureDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoByTimestamp(timestamp);
        _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.GetByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.GetByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task GetByDateAsync_Should_Return200OK_When_TemperatureDtoFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<TemperatureDto> temperatureDto = TemperatureTestData.GetTemperatureDtoByDate(dateTimeOffset);
        _temperatureService.GetTemperatureDtosByDateAsync(dateTimeOffset).Returns(temperatureDto);

        ActionResult<IEnumerable<TemperatureDto>> result = await _temperatureController.GetByDateAsync(dateTimeOffset);

        Assert.That(result, Is.InstanceOf<ActionResult<IEnumerable<TemperatureDto>>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByDateAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _temperatureService.GetTemperatureDtosByDateAsync(dateTimeOffset).ReturnsNull();

        ActionResult<IEnumerable<TemperatureDto>> result = await _temperatureController.GetByDateAsync(dateTimeOffset);

        Assert.That(result, Is.InstanceOf<ActionResult<IEnumerable<TemperatureDto>>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task CreateAsync_Should_Return201Created_When_TemperatureDtoModelstateIsValid()
    {
        TemperatureDto temperatureDto = TemperatureTestData.CreateValidTemperatureDto();
        _temperatureService.CreateTemperatureDtoAsync(Arg.Any<TemperatureDto>()).Returns(Task.FromResult<TemperatureDto?>(temperatureDto));

        ActionResult<TemperatureDto> result = await _temperatureController.CreateAsync(temperatureDto);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<CreatedAtActionResult>());
    }

    [Test]
    public async Task CreateAsync_Should_Return400BadRequest_When_TemperatureDtoModelstateIsInvalid()
    {
        TemperatureDto temperatureDto = TemperatureTestData.CreateInvalidTemperatureDto();
        _temperatureController.ModelState.AddModelError(nameof(temperatureDto.Temp), "Temperature cannot be a negative number.");

        ActionResult<TemperatureDto> result = await _temperatureController.CreateAsync(temperatureDto);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<BadRequestObjectResult>());
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_TemperatureDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoById(id);
        _temperatureService.DeleteTemperatureDtoByIdAsync(id).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<NoContentResult>());
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _temperatureService.DeleteTemperatureDtoByIdAsync(id).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_TemperatureDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoByTimestamp(timestamp);
        _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<NoContentResult>());
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<TemperatureDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }
}
