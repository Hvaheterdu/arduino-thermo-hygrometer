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
        // Arrange
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoById(id);
        _humidityService.GetHumidityDtoByIdAsync(id).Returns(humidityDto);

        // Act
        ActionResult<HumidityDto> act = await _humidityController.GetByIdAsync(id);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;
        Assert.That(okObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
            Assert.That(okObjectResult!.Value, Is.EqualTo(humidityDto));
        });
    }

    [Test]
    public async Task GetByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        _humidityService.GetHumidityDtoByIdAsync(id).ReturnsNull();

        // Act
        ActionResult<HumidityDto> act = await _humidityController.GetByIdAsync(id);

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
    public async Task GetByTimestampAsync_Should_Return200OK_When_HumidityDtoFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoByTimestamp(timestamp);
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        // Act
        ActionResult<HumidityDto> act = await _humidityController.GetByTimestampAsync(timestamp);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;
        Assert.That(okObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
            Assert.That(okObjectResult!.Value, Is.EqualTo(humidityDto));
        });
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).ReturnsNull();

        // Act
        ActionResult<HumidityDto> act = await _humidityController.GetByTimestampAsync(timestamp);

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
    public async Task GetByDateAsync_Should_Return200OK_When_HumidityDtoFoundByDate()
    {
        // Arrange
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<HumidityDto> humidityDto = HumidityTestData.GetHumidityDtoByDate(dateTimeOffset);
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).Returns(humidityDto);

        // Act
        ActionResult<IEnumerable<HumidityDto>> act = await _humidityController.GetByDateAsync(dateTimeOffset);

        // Assert
        OkObjectResult? okObjectResult = act.Result as OkObjectResult;
        Assert.That(okObjectResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
            Assert.That(okObjectResult!.Value, Is.EqualTo(humidityDto));
        });
    }

    [Test]
    public async Task GetByDateAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByDate()
    {
        // Arrange
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).ReturnsNull();

        // Act
        ActionResult<IEnumerable<HumidityDto>> act = await _humidityController.GetByDateAsync(dateTimeOffset);

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
    public async Task CreateAsync_Should_Return201Created_When_HumidityDtoModelstateIsValid()
    {
        // Arrange
        HumidityDto humidityDto = HumidityTestData.CreateValidHumidityDto();
        _humidityService.CreateHumidityDtoAsync(Arg.Any<HumidityDto>()).Returns(Task.FromResult<HumidityDto?>(humidityDto));

        // Act
        ActionResult<HumidityDto> act = await _humidityController.CreateAsync(humidityDto);

        // Assert
        CreatedAtActionResult? createdAtActionResult = act.Result as CreatedAtActionResult;
        Assert.That(createdAtActionResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(createdAtActionResult!.StatusCode, Is.EqualTo(StatusCodes.Status201Created));
            Assert.That(createdAtActionResult!.Value, Is.EqualTo(humidityDto));
        });
    }

    [Test]
    public async Task CreateAsync_Should_Return400BadRequest_When_HumidityDtoModelstateIsInvalid()
    {
        // Arrange
        HumidityDto humidityDto = HumidityTestData.CreateInvalidHumidityDto();
        _humidityController.ModelState.AddModelError(nameof(humidityDto.AirHumidity), "AirHumidity cannot be a negative number.");

        // Act
        ActionResult<HumidityDto> act = await _humidityController.CreateAsync(humidityDto);

        // Assert
        BadRequestObjectResult? badRequestResult = act.Result as BadRequestObjectResult;
        Assert.That(badRequestResult, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(badRequestResult!.StatusCode, Is.EqualTo(StatusCodes.Status400BadRequest));
            Assert.That(badRequestResult!.Value, Is.EqualTo(humidityDto));
        });
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_HumidityDtoFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoById(id);
        _humidityService.DeleteHumidityDtoByIdAsync(id).Returns(humidityDto);

        // Act
        ActionResult<HumidityDto> act = await _humidityController.DeleteByIdAsync(id);

        // Assert
        NoContentResult? noContentResult = act.Result as NoContentResult;
        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        // Arrange
        Guid id = Guid.NewGuid();
        _humidityService.DeleteHumidityDtoByIdAsync(id).ReturnsNull();

        // Act
        ActionResult<HumidityDto> act = await _humidityController.DeleteByIdAsync(id);

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
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_HumidityDtoFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoByTimestamp(timestamp);
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        // Act
        ActionResult<HumidityDto> act = await _humidityController.DeleteByTimestampAsync(timestamp);

        // Assert
        NoContentResult? noContentResult = act.Result as NoContentResult;
        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        // Arrange
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).ReturnsNull();

        // Act
        ActionResult<HumidityDto> act = await _humidityController.DeleteByTimestampAsync(timestamp);

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
