using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Mappers;

public static class TemperatureMapper
{
    /// <summary>
    /// Converts a TemperatureDto object to a Temperature object.
    /// </summary>
    /// <param name="temperatureDto">The TemperatureDto object to convert.</param>
    /// <returns>The converted Temperature object.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="temperatureDto"/> is null.</exception>
    public static Temperature GetTemperatureFromTemperatureDto(TemperatureDto temperatureDto)
    {
        ArgumentNullException.ThrowIfNull(temperatureDto, nameof(temperatureDto));

        Temperature temperature = new(temperatureDto.RegisteredAt, temperatureDto.Temp, temperatureDto.AirHumidity)
        {
            RegisteredAt = temperatureDto.RegisteredAt,
            Temp = temperatureDto.Temp,
            AirHumidity = temperatureDto.AirHumidity
        };

        return temperature;
    }

    /// <summary>
    /// Converts a Temperature object to a TemperatureDto object.
    /// </summary>
    /// <param name="temperature">The Temperature object to convert.</param>
    /// <returns>The converted TemperatureDto object.</returns>
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="temperature"/> is null.</exception>
    public static TemperatureDto GetTemperatureDtoFromTemperature(Temperature temperature)
    {
        ArgumentNullException.ThrowIfNull(temperature, nameof(temperature));

        return new TemperatureDto
        {
            RegisteredAt = temperature.RegisteredAt,
            Temp = temperature.Temp,
            AirHumidity = temperature.AirHumidity
        };
    }
}
