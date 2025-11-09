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
public class BatteryControllerTest
{
    private IBatteryService _batteryService = null!;
    private BatteryController _batteryController = null!;

    [SetUp]
    public void Init()
    {
        _batteryService = Substitute.For<IBatteryService>();
        _batteryController = new BatteryController(_batteryService);
    }

    [Test]
    public async Task GetByIdAsync_Should_Return200OK_When_BatteryDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoById(id);
        _batteryService.GetBatteryDtoByIdAsync(id).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.GetByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByIdAsync_Should_Return404NotFound_When_BatteryDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _batteryService.GetBatteryDtoByIdAsync(id).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.GetByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return200OK_When_BatteryDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoByTimestamp(timestamp);
        _batteryService.GetBatteryDtoByTimestampAsync(timestamp).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.GetByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryService.GetBatteryDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.GetByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task GetByDateAsync_Should_Return200OK_When_BatteryDtoFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<BatteryDto> batteryDto = BatteryTestData.GetBatteryDtoByDate(dateTimeOffset);
        _batteryService.GetBatteryDtosByDateAsync(dateTimeOffset).Returns(batteryDto);

        ActionResult<IEnumerable<BatteryDto>> result = await _batteryController.GetByDateAsync(dateTimeOffset);

        Assert.That(result, Is.InstanceOf<ActionResult<IEnumerable<BatteryDto>>>());
        Assert.That(result.Result, Is.InstanceOf<OkObjectResult>());
    }

    [Test]
    public async Task GetByDateAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _batteryService.GetBatteryDtosByDateAsync(dateTimeOffset).ReturnsNull();

        ActionResult<IEnumerable<BatteryDto>> result = await _batteryController.GetByDateAsync(dateTimeOffset);

        Assert.That(result, Is.InstanceOf<ActionResult<IEnumerable<BatteryDto>>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task CreateAsync_Should_Return201Created_When_BatteryDtoModelstateIsValid()
    {
        BatteryDto batteryDto = BatteryTestData.CreateValidBatteryDto();
        _batteryService.CreateBatteryDtoAsync(Arg.Any<BatteryDto>()).Returns(Task.FromResult<BatteryDto?>(batteryDto));

        ActionResult<BatteryDto> result = await _batteryController.CreateAsync(batteryDto);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<CreatedAtActionResult>());
    }

    [Test]
    public async Task CreateAsync_Should_Return400BadRequest_When_BatteryDtoModelstateIsInvalid()
    {
        BatteryDto batteryDto = BatteryTestData.CreateInvalidBatteryDto();
        _batteryController.ModelState.AddModelError(nameof(batteryDto.BatteryStatus), "Battery status cannot be a negative number.");

        ActionResult<BatteryDto> result = await _batteryController.CreateAsync(batteryDto);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<BadRequestObjectResult>());
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_BatteryDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoById(id);
        _batteryService.DeleteBatteryDtoByIdAsync(id).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.DeleteByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<NoContentResult>());
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_BatteryDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _batteryService.DeleteBatteryDtoByIdAsync(id).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.DeleteByIdAsync(id);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_BatteryDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoByTimestamp(timestamp);
        _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.DeleteByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<NoContentResult>());
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.DeleteByTimestampAsync(timestamp);

        Assert.That(result, Is.InstanceOf<ActionResult<BatteryDto>>());
        Assert.That(result.Result, Is.InstanceOf<NotFoundObjectResult>());
    }
}
