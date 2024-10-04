using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

#pragma warning disable CA1062

namespace ArduinoThermoHygrometer.Api.Mappers;

public static class HumidityMapper
{
    /// <summary>
    /// Converts a HumidityDto object to a Humidity object.
    /// </summary>
    /// <param name="humidityDto">The HumidityDto object to convert.</param>
    /// <returns>The converted Humidity object.</returns>
    public static Humidity GetHumidityFromHumidityDto(HumidityDto humidityDto)
    {
        Humidity humidity = new(humidityDto.RegisteredAt, humidityDto.AirHumidity)
        {
            Id = humidityDto.Id,
            RegisteredAt = humidityDto.RegisteredAt,
            AirHumidity = humidityDto.AirHumidity
        };

        return humidity;
    }

    /// <summary>
    /// Converts a Humidity object to a HumidityDto object.
    /// </summary>
    /// <param name="humidity">The Humidity object to convert.</param>
    /// <returns>The converted HumidityDto object.</returns>
    public static HumidityDto GetHumidityDtoFromHumidity(Humidity humidity)
    {
        return new HumidityDto
        {
            Id = humidity.Id,
            RegisteredAt = humidity.RegisteredAt,
            AirHumidity = humidity.AirHumidity
        };
    }
}
