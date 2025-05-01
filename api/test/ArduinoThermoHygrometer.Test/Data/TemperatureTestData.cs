using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Test.Data;

public static class TemperatureTestData
{
    public static TemperatureDto GetTemperatureDtoTestObjectById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        Temp = 120.25M
    };

    public static TemperatureDto GetTemperatureDtoTestObjectByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        Temp = 115.35M
    };

    public static IEnumerable<TemperatureDto> GetTemperatureDtoTestObjectByDate(DateTimeOffset dateTimeOffset) => new List<TemperatureDto>()
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

    public static TemperatureDto CreateValidTemperatureDtoTestObject() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        Temp = 100.52M
    };

    public static TemperatureDto CreateInvalidTemperatureDtoTestObject() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        Temp = -50.24M
    };
}
