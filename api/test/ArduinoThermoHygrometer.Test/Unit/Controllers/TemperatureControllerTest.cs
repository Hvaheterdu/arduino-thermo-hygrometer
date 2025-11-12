using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Test.Helpers;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NSubstitute;
using NSubstitute.ReturnsExtensions;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Unit.Controllers;

[Collection("TemperatureController unit tests")]
public class TemperatureControllerTest
{
    private readonly ITemperatureService _temperatureService;
    private readonly TemperatureController _temperatureController;

    public TemperatureControllerTest()
    {
        _temperatureService = Substitute.For<ITemperatureService>();
        _temperatureController = new TemperatureController(_temperatureService);
    }

    [Fact]
    public async Task GetByIdAsync_Should_Return200OK_When_TemperatureDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoById(id);
        _temperatureService.GetTemperatureDtoByIdAsync(id).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.GetByIdAsync(id);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(temperatureDto, okResult.Value);
    }

    [Fact]
    public async Task GetByIdAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _temperatureService.GetTemperatureDtoByIdAsync(id).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.GetByIdAsync(id);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task GetByTimestampAsync_Should_Return200OK_When_TemperatureDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoByTimestamp(timestamp);
        _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.GetByTimestampAsync(timestamp);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(temperatureDto, okResult.Value);
    }

    [Fact]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.GetByTimestampAsync(timestamp);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task GetByDateAsync_Should_Return200OK_When_TemperatureDtoFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<TemperatureDto> temperatureDtos = TemperatureTestData.GetTemperatureDtoByDate(dateTimeOffset);
        _temperatureService.GetTemperatureDtosByDateAsync(dateTimeOffset).Returns(temperatureDtos);

        ActionResult<IEnumerable<TemperatureDto>> result = await _temperatureController.GetByDateAsync(dateTimeOffset);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(temperatureDtos, okResult.Value);
    }

    [Fact]
    public async Task GetByDateAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _temperatureService.GetTemperatureDtosByDateAsync(dateTimeOffset).ReturnsNull();

        ActionResult<IEnumerable<TemperatureDto>> result = await _temperatureController.GetByDateAsync(dateTimeOffset);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task CreateAsync_Should_Return201Created_When_TemperatureDtoModelstateIsValid()
    {
        TemperatureDto temperatureDto = TemperatureTestData.CreateValidTemperatureDto();
        _temperatureService.CreateTemperatureDtoAsync(Arg.Any<TemperatureDto>()).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.CreateAsync(temperatureDto);

        CreatedAtActionResult createdResult = Assert.IsType<CreatedAtActionResult>(result.Result);
        Assert.Equal(StatusCodes.Status201Created, createdResult.StatusCode);
        Assert.Equal(temperatureDto, createdResult.Value);
    }

    [Fact]
    public async Task CreateAsync_Should_Return400BadRequest_When_TemperatureDtoModelstateIsInvalid()
    {
        TemperatureDto temperatureDto = TemperatureTestData.CreateInvalidTemperatureDto();
        _temperatureController.ModelState.AddModelError(nameof(temperatureDto.Temp), "Temperature cannot be a negative number.");

        ActionResult<TemperatureDto> result = await _temperatureController.CreateAsync(temperatureDto);

        BadRequestObjectResult badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status400BadRequest, badRequestResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_TemperatureDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoById(id);
        _temperatureService.DeleteTemperatureDtoByIdAsync(id).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByIdAsync(id);

        NoContentResult noContentResult = Assert.IsType<NoContentResult>(result.Result);
        Assert.Equal(StatusCodes.Status204NoContent, noContentResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _temperatureService.DeleteTemperatureDtoByIdAsync(id).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByIdAsync(id);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_TemperatureDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoByTimestamp(timestamp);
        _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp).Returns(temperatureDto);

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByTimestampAsync(timestamp);

        NoContentResult noContentResult = Assert.IsType<NoContentResult>(result.Result);
        Assert.Equal(StatusCodes.Status204NoContent, noContentResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_TemperatureDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<TemperatureDto> result = await _temperatureController.DeleteByTimestampAsync(timestamp);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }
}
