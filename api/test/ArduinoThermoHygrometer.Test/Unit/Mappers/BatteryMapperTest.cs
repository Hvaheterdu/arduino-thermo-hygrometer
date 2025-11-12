using ArduinoThermoHygrometer.Core.Mappers;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Test.Helpers;
using Xunit;

namespace ArduinoThermoHygrometer.Test.Unit.Mappers;

public class BatteryMapperTest
{
    [Fact]
    public void GetBatteryFromBatteryDto_Should_MapPropertiesCorrectly()
    {
        Guid id = Guid.NewGuid();
        BatteryDto batteryDto = BatteryTestData.GetBatteryDtoById(id);

        Battery result = BatteryMapper.GetBatteryFromBatteryDto(batteryDto);

        Assert.NotNull(result);
        Assert.Equal(batteryDto.Id, result.Id);
        Assert.Equal(batteryDto.RegisteredAt, result.RegisteredAt);
        Assert.Equal(batteryDto.BatteryStatus, result.BatteryStatus);
    }

    [Fact]
    public void GetBatteryFromBatteryDto_Should_ThrowArgumentNullException_When_InputIsNull()
    {
        BatteryDto? batteryDto = null;

        ArgumentNullException exception = Assert.Throws<ArgumentNullException>(() =>
        {
            BatteryMapper.GetBatteryFromBatteryDto(batteryDto!);
        });

        Assert.Equal("batteryDto", exception.ParamName);
    }

    [Fact]
    public void GetBatteryDtoFromBattery_Should_MapPropertiesCorrectly()
    {
        Guid id = Guid.NewGuid();
        Battery battery = BatteryTestData.GetBatteryById(id);

        BatteryDto result = BatteryMapper.GetBatteryDtoFromBattery(battery);

        Assert.NotNull(result);
        Assert.Equal(battery.Id, result.Id);
        Assert.Equal(battery.RegisteredAt, result.RegisteredAt);
        Assert.Equal(battery.BatteryStatus, result.BatteryStatus);
    }

    [Fact]
    public void GetBatteryDtoFromBattery_Should_ThrowArgumentNullException_When_InputIsNull()
    {
        Battery? battery = null;

        ArgumentNullException exception = Assert.Throws<ArgumentNullException>(() =>
        {
            BatteryMapper.GetBatteryDtoFromBattery(battery!);
        });

        Assert.Equal("battery", exception.ParamName);
    }
}
