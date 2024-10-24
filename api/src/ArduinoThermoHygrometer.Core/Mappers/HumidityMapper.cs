using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

#pragma warning disable CA1062

namespace ArduinoThermoHygrometer.Core.Mappers;

public static class HumidityMapper
{
    /// <summary>
    /// Maps a HumidityDto object to a Humidity object.
    /// </summary>
    /// <param name="humidityDto">The HumidityDto object to map.</param>
    /// <returns>The mapped Humidity object.</returns>
    public static Humidity GetHumidityFromHumidityDto(HumidityDto humidityDto)
    {
        Humidity humidity = new(humidityDto.RegisteredAt, humidityDto.AirHumidity)
        {
            Id = humidityDto.Id,
            RegisteredAt = DateTimeOffset.Now,
            AirHumidity = humidityDto.AirHumidity
        };

        return humidity;
    }

    /// <summary>
    /// Maps a Humidity object to a HumidityDto object.
    /// </summary>
    /// <param name="humidity">The Humidity object to map.</param>
    /// <returns>The mapped HumidityDto object.</returns>
    public static HumidityDto GetHumidityDtoFromHumidity(Humidity humidity) => new()
    {
        Id = humidity.Id,
        RegisteredAt = DateTimeOffset.Now,
        AirHumidity = humidity.AirHumidity
    };
}
