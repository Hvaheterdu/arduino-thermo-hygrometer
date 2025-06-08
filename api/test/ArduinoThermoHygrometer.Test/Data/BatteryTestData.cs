using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Test.Data;

public static class BatteryTestData
{
    public static BatteryDto GetBatteryDtoTestObjectById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = 100
    };

    public static Battery GetBatteryTestObjectById(Guid id) => new(100)
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
    };

    public static BatteryDto GetBatteryDtoTestObjectByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        BatteryStatus = 95
    };

    public static Battery GetBatteryTestObjectByTimestamp(DateTimeOffset timestamp) => new(95)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
    };

    public static IEnumerable<BatteryDto> GetBatteryDtoTestObjectByDate(DateTimeOffset dateTimeOffset) => new List<BatteryDto>()
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

    public static IEnumerable<Battery> GetBatteryTestObjectByDate(DateTimeOffset dateTimeOffset) => new List<Battery>()
    {
        new(90)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
        },
        new(89)
        {
            Id = Guid.NewGuid(),
            RegisteredAt = dateTimeOffset,
        }
    };

    public static BatteryDto CreateValidBatteryDtoTestObject() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = 85
    };

    public static Battery CreateValidBatteryTestObject() => new(85)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
    };

    public static BatteryDto CreateInvalidBatteryDtoTestObject() => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = -10
    };

    public static Battery CreateInvalidBatteryTestObject() => new(-10)
    {
        Id = Guid.NewGuid(),
        RegisteredAt = DateTimeOffset.Now,
    };
}
