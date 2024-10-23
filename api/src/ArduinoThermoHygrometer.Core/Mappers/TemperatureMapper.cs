using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

#pragma warning disable CA1062

namespace ArduinoThermoHygrometer.Core.Mappers;

public static class TemperatureMapper
{
    /// <summary>
    /// Maps a TemperatureDto object to a Temperature object.
    /// </summary>
    /// <param name="temperatureDto">The TemperatureDto object to map.</param>
    /// <returns>The mapped Temperature object.</returns>
    public static Temperature GetTemperatureFromTemperatureDto(TemperatureDto temperatureDto)
    {
        Temperature temperature = new(temperatureDto.RegisteredAt, temperatureDto.Temp)
        {
            Id = temperatureDto.Id,
            RegisteredAt = DateTimeOffset.Now,
            Temp = temperatureDto.Temp,
        };

        return temperature;
    }

    /// <summary>
    /// Maps a Temperature object to a TemperatureDto object.
    /// </summary>
    /// <param name="temperature">The Temperature object to map.</param>
    /// <returns>The mapped TemperatureDto object.</returns>
    public static TemperatureDto GetTemperatureDtoFromTemperature(Temperature temperature) => new()
    {
        Id = temperature.Id,
        RegisteredAt = DateTimeOffset.Now,
        Temp = temperature.Temp,
    };
}
