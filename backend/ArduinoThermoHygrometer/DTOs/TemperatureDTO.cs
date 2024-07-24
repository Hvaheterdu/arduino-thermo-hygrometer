namespace ArduinoThermoHygrometer.Web.DTOs;

public record TemperatureDto
{
    public DateTimeOffset CreatedAt { get; set; } = default!;

    public string Temp { get; set; } = default!;

    public string AirHumidity { get; set; } = default!;
}
