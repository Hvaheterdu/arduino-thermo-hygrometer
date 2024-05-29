using ArduinoThermoHygrometer.DTOs;
using ArduinoThermoHygrometer.Models;

namespace ArduinoThermoHygrometer.Mapper;

public static class BatteryMapper
{
    /// <summary>
    /// Battery entity to dto mapper.
    /// </summary>
    /// <param name="batteryDto">Dto object.</param>
    /// <returns></returns>
    public static Battery GetBatteryFromBatteryDto(BatteryDto batteryDto)
    {
        Battery battery = new(batteryDto.BatteryStatus)
        {
            BatteryStatus = batteryDto.BatteryStatus
        };

        return battery;
    }

    /// <summary>
    /// Battery dto to entity mapper.
    /// </summary>
    /// <param name="battery">Entity object.</param>
    /// <returns></returns>
    public static BatteryDto GetBatteryDtoFromBattery(Battery battery)
    {
        return new BatteryDto
        {
            BatteryStatus = battery.BatteryStatus
        };
    }
}