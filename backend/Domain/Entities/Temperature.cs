using System.ComponentModel.DataAnnotations;

#pragma warning disable CA1819

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Temperature
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; init; } = DateTimeOffset.Now;

    [Required]
    public decimal Temp { get; set; }

    [Required]
    public decimal AirHumidity { get; set; }

    [Timestamp]
    public byte[]? Version { get; set; }

    public Temperature(DateTimeOffset registeredAt, decimal temp, decimal airHumidity)
    {
        Id = Guid.NewGuid();
        RegisteredAt = registeredAt;
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
