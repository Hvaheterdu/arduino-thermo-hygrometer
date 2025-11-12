using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Test.Helpers;

internal static class BatteryTestData
{
    internal static Battery GetBatteryById(Guid id) => new(100)
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now
    };

    internal static BatteryDto GetBatteryDtoById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = 100
    };

    internal static Battery GetBatteryByTimestamp(DateTimeOffset timestamp) => new(95)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp
    };

    internal static BatteryDto GetBatteryDtoByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        BatteryStatus = 95
    };

    internal static IEnumerable<Battery> GetBatteryByDate(DateTimeOffset dateTimeOffset) => new List<Battery>()
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

    internal static IEnumerable<BatteryDto> GetBatteryDtoByDate(DateTimeOffset dateTimeOffset) => new List<BatteryDto>()
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

    internal static BatteryDto CreateValidBatteryDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = 85
    };

    internal static BatteryDto CreateInvalidBatteryDto() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = -10
    };
}
