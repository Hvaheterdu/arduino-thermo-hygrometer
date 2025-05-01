using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Test.Data;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NSubstitute;
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
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoTestObjectById(id);
        _humidityService.GetHumidityDtoByIdAsync(id).Returns(humidityDto);

        ActionResult<HumidityDto> act = await _humidityController.GetByIdAsync(id);

        OkObjectResult? okObjectResult = act.Result as OkObjectResult;

        Assert.That(okObjectResult, Is.Not.Null);
        Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
        Assert.That(okObjectResult.Value, Is.EqualTo(humidityDto));
    }

    [Test]
    public async Task GetByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _humidityService.GetHumidityDtoByIdAsync(id).Returns((HumidityDto?)null);

        ActionResult<HumidityDto> act = await _humidityController.GetByIdAsync(id);

        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult.Value, Is.Null);
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return200OK_When_HumidityDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoTestObjectByTimestamp(timestamp);
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        ActionResult<HumidityDto> act = await _humidityController.GetByTimestampAsync(timestamp);

        OkObjectResult? okObjectResult = act.Result as OkObjectResult;

        Assert.That(okObjectResult, Is.Not.Null);
        Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
        Assert.That(okObjectResult.Value, Is.EqualTo(humidityDto));
    }

    [Test]
    public async Task GetByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.GetHumidityDtoByTimestampAsync(timestamp).Returns((HumidityDto?)null);

        ActionResult<HumidityDto> act = await _humidityController.GetByTimestampAsync(timestamp);

        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult.Value, Is.Null);
    }

    [Test]
    public async Task GetByDateAsync_Should_Return200OK_When_HumidityDtoFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        IEnumerable<HumidityDto> humidityDto = HumidityTestData.GetHumidityDtoTestObjectByDate(dateTimeOffset);
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).Returns(humidityDto);

        ActionResult<IEnumerable<HumidityDto>> act = await _humidityController.GetByDateAsync(dateTimeOffset);

        OkObjectResult? okObjectResult = act.Result as OkObjectResult;

        Assert.That(okObjectResult, Is.Not.Null);
        Assert.That(okObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
        Assert.That(okObjectResult.Value, Is.EqualTo(humidityDto));
    }

    [Test]
    public async Task GetByDateAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByDate()
    {
        DateTimeOffset dateTimeOffset = DateTimeOffset.Now;
        _humidityService.GetHumidityDtosByDateAsync(dateTimeOffset).Returns((IEnumerable<HumidityDto>?)null);

        ActionResult<IEnumerable<HumidityDto>> act = await _humidityController.GetByDateAsync(dateTimeOffset);

        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult.Value, Is.Null);
    }

    [Test]
    public async Task CreateAsync_Should_Return201Created_When_HumidityDtoModelstateIsValid()
    {
        HumidityDto humidityDto = HumidityTestData.CreateValidHumidityDtoTestObject();
        _humidityService.CreateHumidityDtoAsync(Arg.Any<HumidityDto>()).Returns(Task.FromResult(humidityDto));

        ActionResult<HumidityDto> act = await _humidityController.CreateAsync(humidityDto);

        CreatedAtActionResult? createdAtActionResult = act.Result as CreatedAtActionResult;

        Assert.That(createdAtActionResult, Is.Not.Null);
        Assert.That(createdAtActionResult!.StatusCode, Is.EqualTo(StatusCodes.Status201Created));
        Assert.That(createdAtActionResult.Value, Is.EqualTo(humidityDto));
    }

    [Test]
    public async Task CreateAsync_Should_Return400BadRequest_When_HumidityDtoModelstateIsInvalid()
    {
        HumidityDto humidityDto = HumidityTestData.CreateInvalidHumidityDtoTestObject();
        _humidityController.ModelState.AddModelError(nameof(humidityDto.AirHumidity), "AirHumidity cannot be a negative number.");

        ActionResult<HumidityDto> act = await _humidityController.CreateAsync(humidityDto);

        BadRequestObjectResult? badRequestResult = act.Result as BadRequestObjectResult;

        Assert.That(badRequestResult, Is.Not.Null);
        Assert.That(badRequestResult!.StatusCode, Is.EqualTo(StatusCodes.Status400BadRequest));
        Assert.That(badRequestResult.Value, Is.EqualTo(humidityDto));
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return204NoContent_When_HumidityDtoFoundById()
    {
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoTestObjectById(id);
        _humidityService.DeleteHumidityDtoByIdAsync(id).Returns(humidityDto);

        ActionResult<HumidityDto> act = await _humidityController.DeleteByIdAsync(id);

        NoContentResult? noContentResult = act.Result as NoContentResult;

        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByIdAsync_Should_Return404NotFound_When_HumidityDtoNotFoundById()
    {
        Guid id = Guid.NewGuid();
        _humidityService.DeleteHumidityDtoByIdAsync(id).Returns((HumidityDto?)null);

        ActionResult<HumidityDto> act = await _humidityController.DeleteByIdAsync(id);

        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult.Value, Is.Null);
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return204NoContent_When_HumidityDtoFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoTestObjectByTimestamp(timestamp);
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).Returns(humidityDto);

        ActionResult<HumidityDto> act = await _humidityController.DeleteByTimestampAsync(timestamp);

        NoContentResult? noContentResult = act.Result as NoContentResult;

        Assert.That(noContentResult, Is.Not.Null);
        Assert.That(noContentResult!.StatusCode, Is.EqualTo(StatusCodes.Status204NoContent));
    }

    [Test]
    public async Task DeleteByTimestampAsync_Should_Return404NotFound_When_HumidityDtoNotFoundByTimestamp()
    {
        DateTimeOffset timestamp = DateTimeOffset.Now;
        _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp).Returns((HumidityDto?)null);

        ActionResult<HumidityDto> act = await _humidityController.DeleteByTimestampAsync(timestamp);

        NotFoundObjectResult? notFoundObjectResult = act.Result as NotFoundObjectResult;

        Assert.That(notFoundObjectResult, Is.Not.Null);
        Assert.That(notFoundObjectResult!.StatusCode, Is.EqualTo(StatusCodes.Status404NotFound));
        Assert.That(notFoundObjectResult.Value, Is.Null);
    }
}
