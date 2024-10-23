using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

#pragma warning disable CA1062

namespace ArduinoThermoHygrometer.Core.Mappers;

public static class BatteryMapper
{

    /// <summary>
    /// Maps a BatteryDto object to a Battery object.
    /// </summary>
    /// <param name="batteryDto">The BatteryDto object to map.</param>
    /// <returns>The mapped Battery object.</returns>
    public static Battery GetBatteryFromBatteryDto(BatteryDto batteryDto)
    {
        Battery battery = new(batteryDto.RegisteredAt, batteryDto.BatteryStatus)
        {
            Id = batteryDto.Id,
            RegisteredAt = DateTimeOffset.Now,
            BatteryStatus = batteryDto.BatteryStatus
        };

        return battery;
    }

    /// <summary>
    /// Maps a Battery object to a BatteryDto object.
    /// </summary>
    /// <param name="battery">The Battery object to map.</param>
    /// <returns>The mapped BatteryDto object.</returns>
    public static BatteryDto GetBatteryDtoFromBattery(Battery battery) => new()
    {
        Id = battery.Id,
        RegisteredAt = DateTimeOffset.Now,
        BatteryStatus = battery.BatteryStatus
    };
}
