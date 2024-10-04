using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

#pragma warning disable CA1062

namespace ArduinoThermoHygrometer.Api.Mappers;

public static class TemperatureMapper
{
    /// <summary>
    /// Converts a TemperatureDto object to a Temperature object.
    /// </summary>
    /// <param name="temperatureDto">The TemperatureDto object to convert.</param>
    /// <returns>The converted Temperature object.</returns>
    public static Temperature GetTemperatureFromTemperatureDto(TemperatureDto temperatureDto)
    {
        Temperature temperature = new(temperatureDto.RegisteredAt, temperatureDto.Temp)
        {
            Id = temperatureDto.Id,
            RegisteredAt = temperatureDto.RegisteredAt,
            Temp = temperatureDto.Temp,
        };

        return temperature;
    }

    /// <summary>
    /// Converts a Temperature object to a TemperatureDto object.
    /// </summary>
    /// <param name="temperature">The Temperature object to convert.</param>
    /// <returns>The converted TemperatureDto object.</returns>
    public static TemperatureDto GetTemperatureDtoFromTemperature(Temperature temperature)
    {
        return new TemperatureDto
        {
            Id = temperature.Id,
            RegisteredAt = temperature.RegisteredAt,
            Temp = temperature.Temp,
        };
    }
}
