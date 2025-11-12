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

[Collection("HumidityController unit tests")]
public class HumidityControllerTest
{
    private readonly IHumidityService _humidityService;
    private readonly HumidityController _humidityController;

    public HumidityControllerTest()
    {
        _humidityService = Substitute.For<IHumidityService>();
        _humidityController = new HumidityController(_humidityService);
    }

    [Fact]
    public async Task GetByIdAsync_Should_Return200OK_When_HumidityDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoById(id);
        _humidityService.GetHumidityDtoByIdAsync(id).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.GetByIdAsync(id);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(humidityDto, okResult.Value);
    }

    [Fact]
    public async Task GetByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _humidityService.GetHumidityDtoByIdAsync(id).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.GetByIdAsync(id);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task GetByTimestampAsync_Should_Return200OK_When_HumidityDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoByTimestamp(timestamp);
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.GetByTimestampAsync(timestamp);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(humidityDto, okResult.Value);
    }

    [Fact]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.GetByTimestampAsync(timestamp);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task GetByDateAsync_Should_Return200OK_When_HumidityDtoFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<HumidityDto> humidityDtos = HumidityTestData.GetHumidityDtoByDate(dateTimeOffset);
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).Returns(humidityDtos);

        ActionResult<IEnumerable<HumidityDto>> result = await _humidityController.GetByDateAsync(dateTimeOffset);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(humidityDtos, okResult.Value);
    }

    [Fact]
    public async Task GetByDateAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).ReturnsNull();

        ActionResult<IEnumerable<HumidityDto>> result = await _humidityController.GetByDateAsync(dateTimeOffset);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task CreateAsync_Should_Return201Created_When_HumidityDtoModelstateIsValid()
    {
        HumidityDto humidityDto = HumidityTestData.CreateValidHumidityDto();
        _humidityService.CreateHumidityDtoAsync(Arg.Any<HumidityDto>()).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.CreateAsync(humidityDto);

        CreatedAtActionResult createdResult = Assert.IsType<CreatedAtActionResult>(result.Result);
        Assert.Equal(StatusCodes.Status201Created, createdResult.StatusCode);
        Assert.Equal(humidityDto, createdResult.Value);
    }

    [Fact]
    public async Task CreateAsync_Should_Return400BadRequest_When_HumidityDtoModelstateIsInvalid()
    {
        HumidityDto humidityDto = HumidityTestData.CreateInvalidHumidityDto();
        _humidityController.ModelState.AddModelError(nameof(humidityDto.AirHumidity), "AirHumidity cannot be a negative number.");

        ActionResult<HumidityDto> result = await _humidityController.CreateAsync(humidityDto);

        BadRequestObjectResult badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status400BadRequest, badRequestResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_HumidityDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoById(id);
        _humidityService.DeleteHumidityDtoByIdAsync(id).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.DeleteByIdAsync(id);

        NoContentResult noContentResult = Assert.IsType<NoContentResult>(result.Result);
        Assert.Equal(StatusCodes.Status204NoContent, noContentResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _humidityService.DeleteHumidityDtoByIdAsync(id).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.DeleteByIdAsync(id);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_HumidityDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoByTimestamp(timestamp);
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        ActionResult<HumidityDto> result = await _humidityController.DeleteByTimestampAsync(timestamp);

        NoContentResult noContentResult = Assert.IsType<NoContentResult>(result.Result);
        Assert.Equal(StatusCodes.Status204NoContent, noContentResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<HumidityDto> result = await _humidityController.DeleteByTimestampAsync(timestamp);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }
}
