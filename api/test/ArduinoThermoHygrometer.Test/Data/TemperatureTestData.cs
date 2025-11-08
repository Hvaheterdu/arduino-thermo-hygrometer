using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Test.Data;

public static class TemperatureTestData
{
    public static Temperature GetTemperatureById(Guid id) => new(120.25M)
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
    };

    public static TemperatureDto GetTemperatureDtoById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        Temp = 120.25M
    };

    public static Temperature GetTemperatureByTimestamp(DateTimeOffset timestamp) => new(115.35M)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
    };

    public static TemperatureDto GetTemperatureDtoByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        Temp = 115.35M
    };

    public static IEnumerable<Temperature> GetTemperatureByDate(DateTimeOffset dateTimeOffset) => new List<Temperature>()
    {
        new(110.35M)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
        },
        new(109.52M)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
        }
    };

    public static IEnumerable<TemperatureDto> GetTemperatureDtoByDate(DateTimeOffset dateTimeOffset) => new List<TemperatureDto>()
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

    public static TemperatureDto CreateValidTemperatureDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        Temp = 100.52M
    };

    public static TemperatureDto CreateInvalidTemperatureDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        Temp = -50.24M
    };
}
