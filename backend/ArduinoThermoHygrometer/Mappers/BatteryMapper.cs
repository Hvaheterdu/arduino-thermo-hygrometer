using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Mappers;

public static class BatteryMapper
{

    /// <summary>
    /// Converts a BatteryDto object to a Battery object.
    /// </summary>
    /// <param name="batteryDto">The BatteryDto object to convert.</param>
    /// <returns>The converted Battery object.</returns>
    public static Battery GetBatteryFromBatteryDto(BatteryDto batteryDto)
    {
        Battery battery = new(batteryDto.BatteryStatus)
        {
            CreatedAt = batteryDto.CreatedAt,
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
            CreatedAt = battery.CreatedAt,
            BatteryStatus = battery.BatteryStatus
        };
    }
}
