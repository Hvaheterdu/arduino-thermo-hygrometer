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
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="batteryDto"/> is null.</exception>
    public static Battery GetBatteryFromBatteryDto(BatteryDto batteryDto)
    {
        ArgumentNullException.ThrowIfNull(batteryDto, nameof(batteryDto));

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
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="battery"/> is null.</exception>
    public static BatteryDto GetBatteryDtoFromBattery(Battery battery)
    {
        ArgumentNullException.ThrowIfNull(battery, nameof(battery));

        return new BatteryDto
        {
            Id = battery.Id,
            RegisteredAt = battery.RegisteredAt,
            BatteryStatus = battery.BatteryStatus
        };
    }
}
