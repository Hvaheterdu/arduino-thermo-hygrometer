using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Test.Helpers;
using Microsoft.AspNetCore.Mvc;
using NSubstitute;
using NSubstitute.ReturnsExtensions;
using NUnit.Framework;

namespace ArduinoThermoHygrometer.Test.Unit.Controllers;

[TestFixture]
[FixtureLifeCycle(LifeCycle.InstancePerTestCase)]
public class HumidityControllerTest
{
    private IHumidityService _humidityService = null!;
    private HumidityController _humidityController = null!;

    [SetUp]
    public void Init()
    {
        _humidityService = Substitute.For<IHumidityService>();
        _humidityController = new HumidityController(_humidityService);
    }

    [Test]
    public async Task GetByIdAsync_Should_Return200OK_When_HumidityDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoById(id);
        _humidityService.GetHumidityDtoByIdAsync(id).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.GetByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _humidityService.GetHumidityDtoByIdAsync(id).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.GetByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return200OK_When_HumidityDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoByTimestamp(timestamp);
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.GetByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.GetByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task GetByDateAsync_Should_Return200OK_When_HumidityDtoFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<HumidityDto> humidityDto = HumidityTestData.GetHumidityDtoByDate(dateTimeOffset);
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).Returns(humidityDto);

        ActionResult<IEnumerable<HumidityDto>> result = await _humidityController.GetByDateAsync(dateTimeOffset);

        Assert.That(result, Is.InstanceOf<ActionResult<IEnumerable<HumidityDto>>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByDateAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).ReturnsNull();

        ActionResult<IEnumerable<HumidityDto>> result = await _humidityController.GetByDateAsync(dateTimeOffset);

        Assert.That(result, Is.InstanceOf<ActionResult<IEnumerable<HumidityDto>>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task CreateAsync_Should_Return201Created_When_HumidityDtoModelstateIsValid()
    {
        HumidityDto humidityDto = HumidityTestData.CreateValidHumidityDto();
        _humidityService.CreateHumidityDtoAsync(Arg.Any<HumidityDto>()).Returns(Task.FromResult<HumidityDto?>(humidityDto));

        ActionResult<HumidityDto> result = await _humidityController.CreateAsync(humidityDto);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<CreatedAtActionResult>());
    }

    [Test]
    public async Task CreateAsync_Should_Return400BadRequest_When_HumidityDtoModelstateIsInvalid()
    {
        HumidityDto humidityDto = HumidityTestData.CreateInvalidHumidityDto();
        _humidityController.ModelState.AddModelError(nameof(humidityDto.AirHumidity), "AirHumidity cannot be a negative number.");

        ActionResult<HumidityDto> result = await _humidityController.CreateAsync(humidityDto);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<BadRequestObjectResult>());
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_HumidityDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoById(id);
        _humidityService.DeleteHumidityDtoByIdAsync(id).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.DeleteByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<NoContentResult>());
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _humidityService.DeleteHumidityDtoByIdAsync(id).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.DeleteByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_HumidityDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoByTimestamp(timestamp);
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.DeleteByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<NoContentResult>());
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.DeleteByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<HumidityDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }
}
