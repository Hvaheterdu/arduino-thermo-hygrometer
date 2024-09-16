namespace ArduinoThermoHygrometer.Web.DTOs;

public record TemperatureDto
{
    public Guid Id { get; set; } = default!;

    public DateTimeOffset RegisteredAt { get; set; } = default!;

    public decimal Temp { get; set; } = default!;
}
