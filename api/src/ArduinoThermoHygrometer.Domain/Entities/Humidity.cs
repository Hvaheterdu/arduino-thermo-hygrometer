using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Humidity
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; set; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(20, 90)]
    [NotNull]
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
