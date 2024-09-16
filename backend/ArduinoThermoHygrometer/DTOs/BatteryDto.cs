namespace ArduinoThermoHygrometer.Web.DTOs;

public record BatteryDto
{
    public Guid Id { get; set; } = default!;

    public DateTimeOffset RegisteredAt { get; set; } = default!;

    public int BatteryStatus { get; set; } = default!;
}
