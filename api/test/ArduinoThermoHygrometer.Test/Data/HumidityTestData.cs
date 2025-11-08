using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Test.Data;

public static class HumidityTestData
{
    public static Humidity GetHumidityById(Guid id) => new(95.50M)
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now
    };

    public static HumidityDto GetHumidityDtoById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 95.50M
    };

    public static Humidity GetHumidityByTimestamp(DateTimeOffset timestamp) => new(90.42M)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp
    };

    public static HumidityDto GetHumidityDtoByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        AirHumidity = 90.42M
    };

    public static IEnumerable<Humidity> GetHumidityByDate(DateTimeOffset dateTimeOffset) => new List<Humidity>()
    {
        new(85.22M)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset
        },
        new(84.24M)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset
        }
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
        AirHumidity = 83.98M
    };

    public static HumidityDto CreateInvalidHumidityDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = 100
    };
}
