namespace ArduinoThermoHygrometer.Web.DTOs;

public record BatteryDto
{
    public DateTimeOffset CreatedAt { get; set; } = default!;

    public int BatteryStatus { get; set; } = default!;
}
