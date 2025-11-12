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

[Collection("BatteryController unit tests")]
public class BatteryControllerTest
{
    private readonly IBatteryService _batteryService;
    private readonly BatteryController _batteryController;

    public BatteryControllerTest()
    {
        _batteryService = Substitute.For<IBatteryService>();
        _batteryController = new BatteryController(_batteryService);
    }

    [Fact]
    public async Task GetByIdAsync_Should_Return200OK_When_BatteryDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoById(id);
        _batteryService.GetBatteryDtoByIdAsync(id).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.GetByIdAsync(id);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(batteryDto, okResult.Value);
    }

    [Fact]
    public async Task GetByIdAsync_Should_Return404NotFound_When_BatteryDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _batteryService.GetBatteryDtoByIdAsync(id).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.GetByIdAsync(id);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task GetByTimestampAsync_Should_Return200OK_When_BatteryDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoByTimestamp(timestamp);
        _batteryService.GetBatteryDtoByTimestampAsync(timestamp).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.GetByTimestampAsync(timestamp);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(batteryDto, okResult.Value);
    }

    [Fact]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryService.GetBatteryDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.GetByTimestampAsync(timestamp);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task GetByDateAsync_Should_Return200OK_When_BatteryDtoFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<BatteryDto> batteryDtos = BatteryTestData.GetBatteryDtoByDate(dateTimeOffset);
        _batteryService.GetBatteryDtosByDateAsync(dateTimeOffset).Returns(batteryDtos);

        ActionResult<IEnumerable<BatteryDto>> result = await _batteryController.GetByDateAsync(dateTimeOffset);

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(batteryDtos, okResult.Value);
    }

    [Fact]
    public async Task GetByDateAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _batteryService.GetBatteryDtosByDateAsync(dateTimeOffset).ReturnsNull();

        ActionResult<IEnumerable<BatteryDto>> result = await _batteryController.GetByDateAsync(dateTimeOffset);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task CreateAsync_Should_Return201Created_When_BatteryDtoModelstateIsValid()
    {
        BatteryDto batteryDto = BatteryTestData.CreateValidBatteryDto();
        _batteryService.CreateBatteryDtoAsync(Arg.Any<BatteryDto>()).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.CreateAsync(batteryDto);

        CreatedAtActionResult createdResult = Assert.IsType<CreatedAtActionResult>(result.Result);
        Assert.Equal(StatusCodes.Status201Created, createdResult.StatusCode);
        Assert.Equal(batteryDto, createdResult.Value);
    }

    [Fact]
    public async Task CreateAsync_Should_Return400BadRequest_When_BatteryDtoModelstateIsInvalid()
    {
        BatteryDto batteryDto = BatteryTestData.CreateInvalidBatteryDto();
        _batteryController.ModelState.AddModelError(nameof(batteryDto.BatteryStatus), "Battery status cannot be a negative number.");

        ActionResult<BatteryDto> result = await _batteryController.CreateAsync(batteryDto);

        BadRequestObjectResult badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status400BadRequest, badRequestResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_BatteryDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoById(id);
        _batteryService.DeleteBatteryDtoByIdAsync(id).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.DeleteByIdAsync(id);

        NoContentResult noContentResult = Assert.IsType<NoContentResult>(result.Result);
        Assert.Equal(StatusCodes.Status204NoContent, noContentResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_BatteryDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _batteryService.DeleteBatteryDtoByIdAsync(id).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.DeleteByIdAsync(id);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_BatteryDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoByTimestamp(timestamp);
        _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp).Returns(batteryDto);

        ActionResult<BatteryDto> result = await _batteryController.DeleteByTimestampAsync(timestamp);

        NoContentResult noContentResult = Assert.IsType<NoContentResult>(result.Result);
        Assert.Equal(StatusCodes.Status204NoContent, noContentResult.StatusCode);
    }

    [Fact]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_BatteryDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp).ReturnsNull();

        ActionResult<BatteryDto> result = await _batteryController.DeleteByTimestampAsync(timestamp);

        NotFoundObjectResult notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        Assert.Equal(StatusCodes.Status404NotFound, notFoundResult.StatusCode);
    }
}
