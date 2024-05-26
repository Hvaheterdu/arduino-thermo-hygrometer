using ArduinoThermometer.API.DTOs;
using ArduinoThermometer.API.Models;

namespace ArduinoThermometer.API.Mapper;

public static class TemperatureMapper
{
    /// <summary>
    /// Temperature entity to dto mapper.
    /// </summary>
    /// <param name="temperatureDto">Dto object.</param>
    /// <returns></returns>
    public static Temperature GetTemperatureFromTemperatureDto(TemperatureDto temperatureDto)
    {
        Temperature temperature = new(temperatureDto.Date, temperatureDto.Time, temperatureDto.Temp, temperatureDto.Humidity)
        {
            Date = temperatureDto.Date,
            Time = temperatureDto.Time,
            Temp = temperatureDto.Temp,
            Humidity = temperatureDto.Humidity
        };

        return temperature;
    }

    /// <summary>
    /// Temperature dto to entity mapper.
    /// </summary>
    /// <param name="temperature">Entity object.</param>
    /// <returns></returns>
    public static TemperatureDto GetTemperatureDtoFromTemperature(Temperature temperature)
    {
        return new TemperatureDto
        {
            Date = temperature.Date,
            Time = temperature.Time,
            Temp = temperature.Temp,
            Humidity = temperature.Humidity
        };
    }
}
