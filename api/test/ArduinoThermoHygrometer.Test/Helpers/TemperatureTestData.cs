using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Test.Helpers;

internal static class TemperatureTestData
{
    internal static Temperature GetTemperatureById(Guid id) => new(120.25M)
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now
    };

    internal static TemperatureDto GetTemperatureDtoById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        Temp = 120.25M
    };

    internal static Temperature GetTemperatureByTimestamp(DateTimeOffset timestamp) => new(115.35M)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp
    };

    internal static TemperatureDto GetTemperatureDtoByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        Temp = 115.35M
    };

    internal static IEnumerable<Temperature> GetTemperatureByDate(DateTimeOffset dateTimeOffset) => new List<Temperature>()
    {
        new(110.35M)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset
        },
        new(109.52M)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset
        }
    };

    internal static IEnumerable<TemperatureDto> GetTemperatureDtoByDate(DateTimeOffset dateTimeOffset) => new List<TemperatureDto>()
    {
        new()
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
            Temp = 110.35M
        },
        new()
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
            Temp = 109.52M
        }
    };

    internal static TemperatureDto CreateValidTemperatureDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        Temp = 100.52M
    };

    internal static TemperatureDto CreateInvalidTemperatureDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        Temp = -50.24M
    };
}
