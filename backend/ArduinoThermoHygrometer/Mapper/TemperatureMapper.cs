using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Mapper;

public static class TemperatureMapper
{
    /// <summary>
    /// Temperature entity to dto mapper.
    /// </summary>
    /// <param name="temperatureDto">Dto object.</param>
    /// <returns></returns>
    public static Temperature GetTemperatureFromTemperatureDto(TemperatureDto temperatureDto)
    {
        Temperature temperature = new(temperatureDto.Temp, temperatureDto.AirHumidity)
        {
            Temp = temperatureDto.Temp,
            AirHumidity = temperatureDto.AirHumidity
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
            Temp = temperature.Temp,
            AirHumidity = temperature.AirHumidity
        };
    }
}
