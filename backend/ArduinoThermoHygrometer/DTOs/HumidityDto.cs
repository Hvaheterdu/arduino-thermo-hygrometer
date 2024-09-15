namespace ArduinoThermoHygrometer.Web.DTOs;

public class HumidityDto
{
    public DateTimeOffset RegisteredAt { get; set; } = default!;

    public decimal AirHumidity { get; set; } = default!;
}
