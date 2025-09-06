using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Test.Data;

public static class HumidityTestData
{
    public static HumidityDto GetHumidityDtoById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 95.50M
    };

    public static HumidityDto GetHumidityDtoByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        AirHumidity = 90.42M
    };

    public static IEnumerable<HumidityDto> GetHumidityDtoByDate(DateTimeOffset dateTimeOffset) => new List<HumidityDto>()
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

    public static HumidityDto CreateValidHumidityDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 80.53M
    };

    public static HumidityDto CreateInvalidHumidityDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 10.53M
    };
}
