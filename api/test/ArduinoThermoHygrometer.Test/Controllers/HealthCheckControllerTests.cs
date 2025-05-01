using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
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
        Assert.That(act, Is.TypeOf<OkObjectResult>());
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
        Assert.That(act, Is.TypeOf<ObjectResult>());
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
        Assert.That(act, Is.TypeOf<ObjectResult>());
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_ReturnHealthReportWithHealthStatusOfHealthy_When_HealthStatusIsHealthy()
    {
        // Arrange
        HealthReport healthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Healthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(healthyReport);

        // Act
        OkObjectResult? act = await _healthCheckController.GetHealthCheckReportAsync() as OkObjectResult;

        // Assert
        Assert.That(act, Is.Not.Null);
        Assert.That(act?.Value, Is.EqualTo(healthyReport));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_ReturnHealthReportWithHealthStatusOfDegraded_When_HealthStatusIsDegraded()
    {
        // Arrange
        HealthReport degradedReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Degraded, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(degradedReport);

        // Act
        ObjectResult? act = await _healthCheckController.GetHealthCheckReportAsync() as ObjectResult;

        // Assert
        Assert.That(act, Is.Not.Null);
        Assert.That(act?.Value, Is.EqualTo(degradedReport));
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_ReturnHealthReportWithHealthStatusOfUnhealthy_When_HealthStatusIsUnhealthy()
    {
        // Arrange
        HealthReport unhealthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Unhealthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(unhealthyReport);

        // Act
        ObjectResult? act = await _healthCheckController.GetHealthCheckReportAsync() as ObjectResult;

        // Assert
        Assert.That(act, Is.Not.Null);
        Assert.That(act?.Value, Is.EqualTo(unhealthyReport));
    }
}
