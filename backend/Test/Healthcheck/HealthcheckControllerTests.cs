using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;
using NSubstitute;
using NUnit.Framework;

namespace ArduinoThermoHygrometer.Test.HealthCheck;

[TestFixture]
public class HealthCheckControllerTests
{
    private IHealthCheckService _healthCheckService;
    private HealthCheckController _healthCheckController;

    [SetUp]
    public void Setup()
    {
        _healthCheckService = Substitute.For<IHealthCheckService>();
        _healthCheckController = new HealthCheckController(_healthCheckService);
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return200OK_When_HealthReportStatusIsHealthy()
    {
        // Arrange
        HealthReport healthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Healthy, TimeSpan.Zero);
        _ = _healthCheckService.GetHealthCheckReportAsync().Returns(healthyReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();
        OkObjectResult? result = act as OkObjectResult;

        // Assert
        int actualStatusCode = StatusCodes.Status200OK;
        Assert.That(actualStatusCode, Is.EqualTo(result?.StatusCode));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsDegraded()
    {
        // Arrange
        HealthReport degradedReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Degraded, TimeSpan.Zero);
        _ = _healthCheckService.GetHealthCheckReportAsync().Returns(degradedReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();
        ObjectResult? result = act as ObjectResult;

        // Assert
        int actualStatusCode = StatusCodes.Status500InternalServerError;
        Assert.That(actualStatusCode, Is.EqualTo(result?.StatusCode));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsUnhealthy()
    {
        // Arrange
        HealthReport unhealthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Unhealthy, TimeSpan.Zero);
        _ = _healthCheckService.GetHealthCheckReportAsync().Returns(unhealthyReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();
        ObjectResult? result = act as ObjectResult;

        // Assert
        int actualStatusCode = StatusCodes.Status500InternalServerError;
        Assert.That(actualStatusCode, Is.EqualTo(result?.StatusCode));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_ReturnHealthReportWithHealthyHealthStatus_When_HealthStatusIsHealthy()
    {
        // Arrange
        HealthReport healthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Healthy, TimeSpan.Zero);
        _ = _healthCheckService.GetHealthCheckReportAsync().Returns(healthyReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();
        OkObjectResult? result = act as OkObjectResult;

        // Assert
        Assert.That(healthyReport, Is.EqualTo(result?.Value));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_ReturnHealthReportWithDegradedHealthCheckStatus_When_HealthStatusIsDegraded()
    {
        // Arrange
        HealthReport degradedReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Degraded, TimeSpan.Zero);
        _ = _healthCheckService.GetHealthCheckReportAsync().Returns(degradedReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();
        ObjectResult? result = act as ObjectResult;

        // Assert
        Assert.That(degradedReport, Is.EqualTo(result?.Value));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_ReturnHealthReportWithUnhealthyHealthCheckStatus_When_HealthStatusIsUnHealthy()
    {
        // Arrange
        HealthReport unhealthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Unhealthy, TimeSpan.Zero);
        _ = _healthCheckService.GetHealthCheckReportAsync().Returns(unhealthyReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();
        ObjectResult? result = act as ObjectResult;

        // Assert
        Assert.That(unhealthyReport, Is.EqualTo(result?.Value));
    }
}
