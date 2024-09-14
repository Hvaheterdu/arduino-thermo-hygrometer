namespace ArduinoThermoHygrometer.Web.DTOs;

public record TemperatureDto
{
    public DateTimeOffset RegisteredAt { get; set; } = default!;

    public decimal Temp { get; set; } = default!;

    public decimal AirHumidity { get; set; } = default!;
}
