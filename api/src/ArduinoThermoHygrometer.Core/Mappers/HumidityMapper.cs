using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

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
        ArgumentNullException.ThrowIfNull(humidityDto, nameof(humidityDto));

        Humidity humidity = new(humidityDto.AirHumidity)
        {
            AirHumidity = humidityDto.AirHumidity
        };

        return humidity;
    }

    /// <summary>
    /// Maps a Humidity object to a HumidityDto object.
    /// </summary>
    /// <param name="humidity">The Humidity object to map.</param>
    /// <returns>The mapped HumidityDto object.</returns>
    public static HumidityDto GetHumidityDtoFromHumidity(Humidity humidity)
    {
        ArgumentNullException.ThrowIfNull(humidity, nameof(humidity));

        return new HumidityDto()
        {
            Id = humidity.Id,
            RegisteredAt = humidity.RegisteredAt,
            AirHumidity = humidity.AirHumidity
        };
    }
}
