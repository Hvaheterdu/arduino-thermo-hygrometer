using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Test.Data;

public static class HumidityTestData
{
    public static HumidityDto GetHumidityDtoTestObjectById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 95.50M
    };

    public static HumidityDto GetHumidityDtoTestObjectByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        AirHumidity = 90.42M
    };

    public static IEnumerable<HumidityDto> GetHumidityDtoTestObjectByDate(DateTimeOffset dateTimeOffset) => new List<HumidityDto>()
    {
        new()
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
            AirHumidity = 85.22M
        },
        new()
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
            AirHumidity = 84.24M
        }
    };

    public static HumidityDto CreateValidHumidityDtoTestObject() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 80.53M
    };

    public static HumidityDto CreateInvalidHumidityDtoTestObject() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 10.53M
    };
}
