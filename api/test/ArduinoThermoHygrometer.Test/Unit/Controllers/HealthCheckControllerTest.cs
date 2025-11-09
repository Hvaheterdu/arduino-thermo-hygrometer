using ArduinoThermoHygrometer.Api.Controllers;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;
using NSubstitute;
using NUnit.Framework;

namespace ArduinoThermoHygrometer.Test.Unit.Controllers;

[TestFixture]
[FixtureLifeCycle(LifeCycle.InstancePerTestCase)]
public class HealthCheckControllerTest
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
        HealthReport healthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Healthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(healthyReport);

        IActionResult result = await _healthCheckController.GetHealthCheckReportAsync();

        Assert.Multiple(() =>
        {
            Assert.That(result, Is.InstanceOf<OkObjectResult>());
            Assert.That(((OkObjectResult)result).Value, Is.EqualTo(healthyReport));
        });
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsDegraded()
    {
        HealthReport degradedReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Degraded, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(degradedReport);

        IActionResult result = await _healthCheckController.GetHealthCheckReportAsync();

        Assert.Multiple(() =>
        {
            Assert.That(result, Is.InstanceOf<ObjectResult>());
            Assert.That(((ObjectResult)result).Value, Is.EqualTo(degradedReport));
            Assert.That(((ObjectResult)result).StatusCode, Is.EqualTo(StatusCodes.Status500InternalServerError));
        });
    }

    [Test]
    public async Task GetHealthCheckReportAsync_Should_Return500InternalServerError_When_HealthReportStatusIsUnhealthy()
    {
        HealthReport unhealthyReport = new(new Dictionary<string, HealthReportEntry>(), HealthStatus.Unhealthy, TimeSpan.Zero);
        _healthCheckService.GetHealthCheckReportAsync().Returns(unhealthyReport);

        IActionResult result = await _healthCheckController.GetHealthCheckReportAsync();

        Assert.Multiple(() =>
        {
            Assert.That(result, Is.InstanceOf<ObjectResult>());
            Assert.That(((ObjectResult)result).Value, Is.EqualTo(unhealthyReport));
            Assert.That(((ObjectResult)result).StatusCode, Is.EqualTo(StatusCodes.Status500InternalServerError));
        });
    }
}
