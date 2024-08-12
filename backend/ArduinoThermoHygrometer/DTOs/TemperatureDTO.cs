namespace ArduinoThermoHygrometer.Web.DTOs;

public record TemperatureDto
{
    public DateTimeOffset CreatedAt { get; set; } = default!;

    public decimal Temp { get; set; } = default!;

    public decimal AirHumidity { get; set; } = default!;
}
