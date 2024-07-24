namespace ArduinoThermoHygrometer.Web.DTOs;

public record BatteryDto
{
    public DateTimeOffset CreatedAt { get; set; } = default!;

    public string BatteryStatus { get; set; } = default!;
}
