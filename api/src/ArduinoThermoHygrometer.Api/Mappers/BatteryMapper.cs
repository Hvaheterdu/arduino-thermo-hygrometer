using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

#pragma warning disable CA1062

namespace ArduinoThermoHygrometer.Api.Mappers;

public static class BatteryMapper
{

    /// <summary>
    /// Converts a BatteryDto object to a Battery object.
    /// </summary>
    /// <param name="batteryDto">The BatteryDto object to convert.</param>
    /// <returns>The converted Battery object.</returns>
    public static Battery GetBatteryFromBatteryDto(BatteryDto batteryDto)
    {
        Battery battery = new(batteryDto.RegisteredAt, batteryDto.BatteryStatus)
        {
            Id = batteryDto.Id,
            RegisteredAt = batteryDto.RegisteredAt,
            BatteryStatus = batteryDto.BatteryStatus
        };

        return battery;
    }

    /// <summary>
    /// Converts a Battery object to a BatteryDto object.
    /// </summary>
    /// <param name="battery">The Battery object to convert.</param>
    /// <returns>The converted BatteryDto object.</returns>
    public static BatteryDto GetBatteryDtoFromBattery(Battery battery)
    {
        return new BatteryDto
        {
            Id = battery.Id,
            RegisteredAt = battery.RegisteredAt,
            BatteryStatus = battery.BatteryStatus
        };
    }
}
