using ArduinoThermometer.API.DTOs;
using ArduinoThermometer.API.Models;

namespace ArduinoThermometer.API.Mapper;

public static class BatteryMapper
{
    /// <summary>
    /// Battery entity to dto mapper.
    /// </summary>
    /// <param name="batteryDto">Dto object.</param>
    /// <returns></returns>
    public static Battery GetBatteryFromBatteryDto(BatteryDto batteryDto)
    {
        Battery battery = new(batteryDto.Date, batteryDto.Time, batteryDto.BatteryStatus)
        {
            Date = batteryDto.Date,
            Time = batteryDto.Time,
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
            Date = battery.Date,
            Time = battery.Time,
            BatteryStatus = battery.BatteryStatus
        };
    }
}