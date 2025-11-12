using ArduinoThermoHygrometer.Core.Mappers;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Test.Helpers;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Unit.Mappers;

[Collection("Humidity unit tests.")]
public class HumidityMapperTest
{
    [Fact]
    public void GetHumidityFromHumidityDto_Should_MapPropertiesCorrectly()
    {
        Guid id = Guid.NewGuid();
        HumidityDto humidityDto = HumidityTestData.GetHumidityDtoById(id);

        Humidity result = HumidityMapper.GetHumidityFromHumidityDto(humidityDto);

        Assert.NotNull(result);
        Assert.Equal(humidityDto.Id, result.Id);
        Assert.Equal(humidityDto.RegisteredAt, result.RegisteredAt);
        Assert.Equal(humidityDto.AirHumidity, result.AirHumidity);
    }

    [Fact]
    public void GetHumidityFromHumidityDto_Should_ThrowArgumentNullException_When_InputIsNull()
    {
        HumidityDto? humidityDto = null;

        ArgumentNullException exception = Assert.Throws<ArgumentNullException>(() =>
        {
            HumidityMapper.GetHumidityFromHumidityDto(humidityDto!);
        });

        Assert.Equal("humidityDto", exception.ParamName);
    }

    [Fact]
    public void GetHumidityDtoFromHumidity_Should_MapPropertiesCorrectly()
    {
        Guid id = Guid.NewGuid();
        Humidity humidity = HumidityTestData.GetHumidityById(id);

        HumidityDto result = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        Assert.NotNull(result);
        Assert.Equal(humidity.Id, result.Id);
        Assert.Equal(humidity.RegisteredAt, result.RegisteredAt);
        Assert.Equal(humidity.AirHumidity, result.AirHumidity);
    }

    [Fact]
    public void GetHumidityDtoFromHumidity_Should_ThrowArgumentNullException_When_InputIsNull()
    {
        Humidity? humidity = null;

        ArgumentNullException exception = Assert.Throws<ArgumentNullException>(() =>
        {
            HumidityMapper.GetHumidityDtoFromHumidity(humidity!);
        });

        Assert.Equal("humidity", exception.ParamName);
    }
}
