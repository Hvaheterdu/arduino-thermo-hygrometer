namespace ArduinoThermoHygrometer.Api.DTOs;

public class HumidityDto
{
    public Guid Id { get; set; } = default!;

    public DateTimeOffset RegisteredAt { get; set; } = default!;

    public decimal AirHumidity { get; set; } = default!;
}
