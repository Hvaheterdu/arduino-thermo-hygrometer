using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Test.Data;

public static class BatteryTestData
{
    public static BatteryDto GetBatteryDtoTestObjectById(Guid id) => new()
    {
        Id = id,
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = 100
    };

    public static BatteryDto GetBatteryDtoTestObjectByTimestamp(DateTimeOffset timestamp) => new()
    {
        Id = Guid.NewGuid(),
        RegisteredAt = timestamp,
        BatteryStatus = 95
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
}
