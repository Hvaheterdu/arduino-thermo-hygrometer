using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

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
        ArgumentNullException.ThrowIfNull(temperatureDto, nameof(temperatureDto));

        Temperature temperature = new(temperatureDto.Temp)
        {
            Id = temperatureDto.Id,
            RegisteredAt = temperatureDto.RegisteredAt,
            Temp = temperatureDto.Temp,
        };

        return temperature;
    }

    /// <summary>
    /// Maps a Temperature object to a TemperatureDto object.
    /// </summary>
    /// <param name="temperature">The Temperature object to map.</param>
    /// <returns>The mapped TemperatureDto object.</returns>
    public static TemperatureDto GetTemperatureDtoFromTemperature(Temperature temperature)
    {
        ArgumentNullException.ThrowIfNull(temperature, nameof(temperature));

        return new TemperatureDto()
        {
            Id = temperature.Id,
            RegisteredAt = temperature.RegisteredAt,
            Temp = temperature.Temp
        };
    }
}
