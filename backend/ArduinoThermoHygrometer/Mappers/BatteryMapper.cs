using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Mappers;

public static class BatteryMapper
{
    /// <summary>
    /// Battery entity to dto mapper.
    /// </summary>
    /// <param name="batteryDto">Dto object.</param>
    /// <returns>Battery entity</returns>
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
    /// Battery dto to entity mapper.
    /// </summary>
    /// <param name="battery">Entity object.</param>
    /// <returns>BatteryDto object</returns>
    public static BatteryDto GetBatteryDtoFromBattery(Battery battery)
    {
        return new BatteryDto
        {
            CreatedAt = battery.CreatedAt,
            BatteryStatus = battery.BatteryStatus
        };
    }
}