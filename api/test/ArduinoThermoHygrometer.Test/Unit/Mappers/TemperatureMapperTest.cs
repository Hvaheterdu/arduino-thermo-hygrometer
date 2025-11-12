using System;
using ArduinoThermoHygrometer.Core.Mappers;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Test.Helpers;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Unit.Mappers;

[Collection("TemperatureMapper unit tests.")]
public class TemperatureMapperTest
{
    [Fact]
    public void GetTemperatureFromTemperatureDto_Should_MapPropertiesCorrectly()
    {
        Guid id = Guid.NewGuid();
        TemperatureDto temperatureDto = TemperatureTestData.GetTemperatureDtoById(id);

        Temperature result = TemperatureMapper.GetTemperatureFromTemperatureDto(temperatureDto);

        Assert.NotNull(result);
        Assert.Equal(temperatureDto.Id, result.Id);
        Assert.Equal(temperatureDto.RegisteredAt, result.RegisteredAt);
        Assert.Equal(temperatureDto.Temp, result.Temp);
    }

    [Fact]
    public void GetTemperatureFromTemperatureDto_Should_ThrowArgumentNullException_When_InputIsNull()
    {
        TemperatureDto? temperatureDto = null;

        ArgumentNullException exception = Assert.Throws<ArgumentNullException>(() =>
        {
            TemperatureMapper.GetTemperatureFromTemperatureDto(temperatureDto!);
        });

        Assert.Equal("temperatureDto", exception.ParamName);
    }

    [Fact]
    public void GetTemperatureDtoFromTemperature_Should_MapPropertiesCorrectly()
    {
        Guid id = Guid.NewGuid();
        Temperature temperature = TemperatureTestData.GetTemperatureById(id);

        TemperatureDto result = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        Assert.NotNull(result);
        Assert.Equal(temperature.Id, result.Id);
        Assert.Equal(temperature.RegisteredAt, result.RegisteredAt);
        Assert.Equal(temperature.Temp, result.Temp);
    }

    [Fact]
    public void GetTemperatureDtoFromTemperature_Should_ThrowArgumentNullException_When_InputIsNull()
    {
        Temperature? temperature = null;

        ArgumentNullException exception = Assert.Throws<ArgumentNullException>(() =>
        {
            TemperatureMapper.GetTemperatureDtoFromTemperature(temperature!);
        });

        Assert.Equal("temperature", exception.ParamName);
    }
}
