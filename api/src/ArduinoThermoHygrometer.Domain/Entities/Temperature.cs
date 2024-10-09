using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Temperature
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; set; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(-55, 125)]
    [NotNull]
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
