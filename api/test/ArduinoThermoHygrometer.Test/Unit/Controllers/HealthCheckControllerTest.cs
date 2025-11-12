using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;
using NSubstitute;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Unit.Controllers;

[Collection("HealthCheckController unit tests.")]
public class HealthCheckControllerTest
{
    private readonly IHealthCheckService _healthCheckService;
    private readonly HealthCheckController _healthCheckController;

    public HealthCheckControllerTest()
    {
        _healthCheckService = Substitute.For<IHealthCheckService>();
        _healthCheckController = new HealthCheckController(_healthCheckService);
    }

    [Fact]
    public async Task GetHealthCheckReportAsync_Should_Return200OK_When_HealthReportStatusIsHealthy()
    {
        HealthReport healthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Healthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(healthyReport);

        IActionResult result = await _healthCheckController.GetHealthCheckReportAsync();

        OkObjectResult okResult = Assert.IsType<OkObjectResult>(result);
        Assert.Equal(StatusCodes.Status200OK, okResult.StatusCode);
        Assert.Equal(healthyReport, okResult.Value);
    }

    [Fact]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsDegraded()
    {
        HealthReport degradedReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Degraded, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(degradedReport);

        IActionResult result = await _healthCheckController.GetHealthCheckReportAsync();

        ObjectResult objectResult = Assert.IsType<ObjectResult>(result);
        Assert.Equal(StatusCodes.Status500InternalServerError, objectResult.StatusCode);
        Assert.Equal(degradedReport, objectResult.Value);
    }

    [Fact]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsUnhealthy()
    {
        HealthReport unhealthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Unhealthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(unhealthyReport);

        IActionResult result = await _healthCheckController.GetHealthCheckReportAsync();

        ObjectResult objectResult = Assert.IsType<ObjectResult>(result);
        Assert.Equal(StatusCodes.Status500InternalServerError, objectResult.StatusCode);
        Assert.Equal(unhealthyReport, objectResult.Value);
    }
}
