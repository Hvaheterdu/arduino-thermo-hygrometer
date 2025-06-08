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
        // Arrange
        Guid id = Guid.NewGuid();
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoTestObjectById(id);
        _temperatureService.GetTemperatureDtoByIdAsync(id).Returns(temperatureDto);

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.GetByIdAsync(id);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;
        Assert.That(okObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
            Assert.That(okObjectResult!.Value, Is.EqualTo(temperatureDto));
        });
    }

    [Test]
    public async Task GetByIdAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        _temperatureService.GetTemperatureDtoByIdAsync(id).ReturnsNull();

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.GetByIdAsync(id);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;
        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
            Assert.That(notFoundObjectResult!.Value, Is.Null);
        });
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return200OK_When_TemperatureDtoFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoTestObjectByTimestamp(timestamp);
        _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp).Returns(temperatureDto);

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.GetByTimestampAsync(timestamp);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;
        Assert.That(okObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
            Assert.That(okObjectResult!.Value, Is.EqualTo(temperatureDto));
        });
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp).ReturnsNull();

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.GetByTimestampAsync(timestamp);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;
        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
            Assert.That(notFoundObjectResult!.Value, Is.Null);
        });
    }

    [Test]
    public async Task GetByDateAsync_Should_Return200OK_When_TemperatureDtoFoundByDate()
    {
        // Arrange
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<TemperatureDto> temperatureDto = TemperatureTestData.GetTemperatureDtoTestObjectByDate(dateTimeOffset);
        _temperatureService.GetTemperatureDtosByDateAsync(dateTimeOffset).Returns(temperatureDto);

        // Act
        ActionResult<IEnumerable<TemperatureDto>> act = await _temperatureController.GetByDateAsync(dateTimeOffset);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;
        Assert.That(okObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
            Assert.That(okObjectResult!.Value, Is.EqualTo(temperatureDto));
        });
    }

    [Test]
    public async Task GetByDateAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByDate()
    {
        // Arrange
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _temperatureService.GetTemperatureDtosByDateAsync(dateTimeOffset).ReturnsNull();

        // Act
        ActionResult<IEnumerable<TemperatureDto>> act = await _temperatureController.GetByDateAsync(dateTimeOffset);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;
        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
            Assert.That(notFoundObjectResult!.Value, Is.Null);
        });
    }

    [Test]
    public async Task CreateAsync_Should_Return201Created_When_TemperatureDtoModelstateIsValid()
    {
        // Arrange
        TemperatureDto temperatureDto = TemperatureTestData.CreateValidTemperatureDtoTestObject();
        _temperatureService.CreateTemperatureDtoAsync(Arg.Any<TemperatureDto>()).Returns(Task.FromResult(temperatureDto));

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.CreateAsync(temperatureDto);

        // Assert
        CreatedAtActionResult? createdAtActionResult = act.Result as CreatedAtActionResult;
        Assert.That(createdAtActionResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(createdAtActionResult!.StatusCode, Is.EqualTo(StatusCodes.Status201Created));
            Assert.That(createdAtActionResult!.Value, Is.EqualTo(temperatureDto));
        });
    }

    [Test]
    public async Task CreateAsync_Should_Return400BadRequest_When_TemperatureDtoModelstateIsInvalid()
    {
        // Arrange
        TemperatureDto temperatureDto = TemperatureTestData.CreateInvalidTemperatureDtoTestObject();
        _temperatureController.ModelState.AddModelError(nameof(temperatureDto.Temp), "Temperature cannot be a negative number.");

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.CreateAsync(temperatureDto);

        // Assert
        BadRequestObjectResult? badRequestResult = act.Result as BadRequestObjectResult;
        Assert.That(badRequestResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(badRequestResult!.StatusCode, Is.EqualTo(StatusCodes.Status400BadRequest));
            Assert.That(badRequestResult!.Value, Is.EqualTo(temperatureDto));
        });
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_TemperatureDtoFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoTestObjectById(id);
        _temperatureService.DeleteTemperatureDtoByIdAsync(id).Returns(temperatureDto);

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.DeleteByIdAsync(id);

        // Assert
        NoContentResult? noContentResult = act.Result as NoContentResult;
        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        _temperatureService.DeleteTemperatureDtoByIdAsync(id).ReturnsNull();

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.DeleteByIdAsync(id);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;
        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
            Assert.That(notFoundObjectResult!.Value, Is.Null);
        });
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_TemperatureDtoFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoTestObjectByTimestamp(timestamp);
        _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp).Returns(temperatureDto);

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.DeleteByTimestampAsync(timestamp);

        // Assert
        NoContentResult? noContentResult = act.Result as NoContentResult;
        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp).ReturnsNull();

        // Act
        ActionResult<TemperatureDto> act = await _temperatureController.DeleteByTimestampAsync(timestamp);

        // Assert
        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;
        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
            Assert.That(notFoundObjectResult!.Value, Is.Null);
        });
    }
}
