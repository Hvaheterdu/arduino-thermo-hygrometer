using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Test.Data;

public static class BatteryTestData
{
    public static Battery GetBatteryById(Guid id) => new(100)
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now
    };

    public static BatteryDto GetBatteryDtoById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = 100
    };

    public static Battery GetBatteryByTimestamp(DateTimeOffset timestamp) => new(95)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp
    };

    public static BatteryDto GetBatteryDtoByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        BatteryStatus = 95
    };

    public static IEnumerable<Battery> GetBatteryByDate(DateTimeOffset dateTimeOffset) => new List<Battery>()
    {
        new(90)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset
        },
        new(89)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset
        }
    };

    public static IEnumerable<BatteryDto> GetBatteryDtoByDate(DateTimeOffset dateTimeOffset) => new List<BatteryDto>()
    {
        new()
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
            BatteryStatus = 90
        },
        new()
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
            BatteryStatus = 89
        }
    };

    public static BatteryDto CreateValidBatteryDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = 85
    };

    public static BatteryDto CreateInvalidBatteryDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = -10
    };
}
