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
        // Arrange
        Guid id = Guid.NewGuid();
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoTestObjectById(id);
        _batteryService.GetBatteryDtoByIdAsync(id).Returns(batteryDto);

        // Act
        ActionResult<BatteryDto> act = await _batteryController.GetByIdAsync(id);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;

        Assert.That(okObjectResult, Is.Not.Null);
        Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
        Assert.That(okObjectResult!.Value, Is.EqualTo(batteryDto));
    }

    [Test]
    public async Task GetByIdAsync_Should_Return404NotFound_When_BatteryDtoNotFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        _batteryService.GetBatteryDtoByIdAsync(id).ReturnsNull();

        // Act
        ActionResult<BatteryDto> act = await _batteryController.GetByIdAsync(id);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult!.Value, Is.Null);
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return200OK_When_BatteryDtoFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoTestObjectByTimestamp(timestamp);
        _batteryService.GetBatteryDtoByTimestampAsync(timestamp).Returns(batteryDto);

        // Act
        ActionResult<BatteryDto> act = await _batteryController.GetByTimestampAsync(timestamp);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;

        Assert.That(okObjectResult, Is.Not.Null);
        Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
        Assert.That(okObjectResult!.Value, Is.EqualTo(batteryDto));
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryService.GetBatteryDtoByTimestampAsync(timestamp).ReturnsNull();

        // Act
        ActionResult<BatteryDto> act = await _batteryController.GetByTimestampAsync(timestamp);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult!.Value, Is.Null);
    }

    [Test]
    public async Task GetByDateAsync_Should_Return200OK_When_BatteryDtoFoundByDate()
    {
        // Arrange
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<BatteryDto> batteryDto = BatteryTestData.GetBatteryDtoTestObjectByDate(dateTimeOffset);
        _batteryService.GetBatteryDtosByDateAsync(dateTimeOffset).Returns(batteryDto);

        // Act
        ActionResult<IEnumerable<BatteryDto>> act = await _batteryController.GetByDateAsync(dateTimeOffset);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;

        Assert.That(okObjectResult, Is.Not.Null);
        Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
        Assert.That(okObjectResult!.Value, Is.EqualTo(batteryDto));
    }

    [Test]
    public async Task GetByDateAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByDate()
    {
        // Arrange
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _batteryService.GetBatteryDtosByDateAsync(dateTimeOffset).ReturnsNull();

        // Act
        ActionResult<IEnumerable<BatteryDto>> act = await _batteryController.GetByDateAsync(dateTimeOffset);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult!.Value, Is.Null);
    }

    [Test]
    public async Task CreateAsync_Should_Return201Created_When_BatteryDtoModelstateIsValid()
    {
        // Arrange
        BatteryDto batteryDto = BatteryTestData.CreateValidBatteryDtoTestObject();
        _batteryService.CreateBatteryDtoAsync(Arg.Any<BatteryDto>()).Returns(Task.FromResult(batteryDto));

        // Act
        ActionResult<BatteryDto> act = await _batteryController.CreateAsync(batteryDto);

        // Assert
        CreatedAtActionResult? createdAtActionResult = act.Result as CreatedAtActionResult;

        Assert.That(createdAtActionResult, Is.Not.Null);
        Assert.That(createdAtActionResult!.StatusCode, Is.EqualTo(StatusCodes.Status201Created));
        Assert.That(createdAtActionResult!.Value, Is.EqualTo(batteryDto));
    }

    [Test]
    public async Task CreateAsync_Should_Return400BadRequest_When_BatteryDtoModelstateIsInvalid()
    {
        // Arrange
        BatteryDto batteryDto = BatteryTestData.CreateInvalidBatteryDtoTestObject();
        _batteryController.ModelState.AddModelError(nameof(batteryDto.BatteryStatus), "Battery status cannot be a negative number.");

        // Act
        ActionResult<BatteryDto> act = await _batteryController.CreateAsync(batteryDto);

        // Assert
        BadRequestObjectResult? badRequestResult = act.Result as BadRequestObjectResult;

        Assert.That(badRequestResult, Is.Not.Null);
        Assert.That(badRequestResult!.StatusCode, Is.EqualTo(StatusCodes.Status400BadRequest));
        Assert.That(badRequestResult!.Value, Is.EqualTo(batteryDto));
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_BatteryDtoFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoTestObjectById(id);
        _batteryService.DeleteBatteryDtoByIdAsync(id).Returns(batteryDto);

        // Act
        ActionResult<BatteryDto> act = await _batteryController.DeleteByIdAsync(id);

        // Assert
        NoContentResult? noContentResult = act.Result as NoContentResult;

        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_BatteryDtoNotFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        _batteryService.DeleteBatteryDtoByIdAsync(id).ReturnsNull();

        // Act
        ActionResult<BatteryDto> act = await _batteryController.DeleteByIdAsync(id);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult!.Value, Is.Null);
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_BatteryDtoFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoTestObjectByTimestamp(timestamp);
        _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp).Returns(batteryDto);

        // Act
        ActionResult<BatteryDto> act = await _batteryController.DeleteByTimestampAsync(timestamp);

        // Assert
        NoContentResult? noContentResult = act.Result as NoContentResult;

        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp).ReturnsNull();

        // Act
        ActionResult<BatteryDto> act = await _batteryController.DeleteByTimestampAsync(timestamp);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult!.Value, Is.Null);
    }
}
