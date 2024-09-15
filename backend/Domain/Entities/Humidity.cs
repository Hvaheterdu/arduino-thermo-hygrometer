using System.ComponentModel.DataAnnotations;

#pragma warning disable CA1819

namespace ArduinoThermoHygrometer.Domain.Entities;
public class Humidity
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; init; } = DateTimeOffset.Now;

    [Required]
    public decimal AirHumidity { get; set; }

    [Timestamp]
    public byte[]? Version { get; set; }

    public Humidity(DateTimeOffset registeredAt, decimal airHumidity)
    {
        Id = Guid.NewGuid();
        RegisteredAt = registeredAt;
        AirHumidity = airHumidity;
    }
}
