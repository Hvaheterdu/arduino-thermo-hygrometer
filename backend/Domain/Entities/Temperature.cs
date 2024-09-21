using System.ComponentModel.DataAnnotations;

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

    [Timestamp]
    public byte[]? Version { get; set; }

    public Temperature(DateTimeOffset registeredAt, decimal temp)
    {
        Id = Guid.NewGuid();
        RegisteredAt = registeredAt;
        Temp = temp;
    }
}
