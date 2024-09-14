namespace ArduinoThermoHygrometer.Web.DTOs;

public record BatteryDto
{
    public DateTimeOffset RegisteredAt { get; set; } = default!;

    public int BatteryStatus { get; set; } = default!;
}
