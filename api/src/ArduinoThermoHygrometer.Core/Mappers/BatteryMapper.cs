using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

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
        ArgumentNullException.ThrowIfNull(batteryDto, nameof(batteryDto));

        Battery battery = new(batteryDto.BatteryStatus)
        {
            BatteryStatus = batteryDto.BatteryStatus
        };

        return battery;
    }

    /// <summary>
    /// Maps a Battery object to a BatteryDto object.
    /// </summary>
    /// <param name="battery">The Battery object to map.</param>
    /// <returns>The mapped BatteryDto object.</returns>
    public static BatteryDto GetBatteryDtoFromBattery(Battery battery)
    {
        ArgumentNullException.ThrowIfNull(battery, nameof(battery));

        return new BatteryDto()
        {
            Id = battery.Id,
            RegisteredAt = battery.RegisteredAt,
            BatteryStatus = battery.BatteryStatus
        };
    }
}
