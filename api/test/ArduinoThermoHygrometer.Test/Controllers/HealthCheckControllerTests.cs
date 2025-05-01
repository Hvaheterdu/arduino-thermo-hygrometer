using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;
using NSubstitute;
using NUnit.Framework;

namespace ArduinoThermoHygrometer.Test.Controllers;

[TestFixture]
[FixtureLifeCycle(LifeCycle.InstancePerTestCase)]
public class HealthCheckControllerTests
{
    private IHealthCheckService _healthCheckService = null!;
    private HealthCheckController _healthCheckController = null!;

    [SetUp]
    public void Init()
    {
        _healthCheckService = Substitute.For<IHealthCheckService>();
        _healthCheckController = new HealthCheckController(_healthCheckService);
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return200OK_When_HealthReportStatusIsHealthy()
    {
        // Arrange
        HealthReport healthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Healthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(healthyReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();

        // Assert
        OkObjectResult? okObjectResult = act as OkObjectResult;

        Assert.That(okObjectResult, Is.Not.Null);
        Assert.That(okObjectResult.StatusCode, Is.EqualTo(StatusCodes.Status200OK));
        Assert.That(okObjectResult.Value, Is.EqualTo(healthyReport));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsDegraded()
    {
        // Arrange
        HealthReport degradedReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Degraded, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(degradedReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();

        // Assert
        ObjectResult? objectResult = act as ObjectResult;

        Assert.That(objectResult, Is.Not.Null);
        Assert.That(objectResult.StatusCode, Is.EqualTo(StatusCodes.Status500InternalServerError));
        Assert.That(objectResult.Value, Is.EqualTo(degradedReport));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsUnhealthy()
    {
        // Arrange
        HealthReport unhealthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Unhealthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(unhealthyReport);

        // Act
        IActionResult act = await _healthCheckController.GetHealthCheckReportAsync();

        // Assert
        ObjectResult? objectResult = act as ObjectResult;

        Assert.That(objectResult, Is.Not.Null);
        Assert.That(objectResult.StatusCode, Is.EqualTo(StatusCodes.Status500InternalServerError));
        Assert.That(objectResult.Value, Is.EqualTo(unhealthyReport));
    }
}
