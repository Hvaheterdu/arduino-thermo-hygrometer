using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Temperature
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; init; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(-55, 125)]
    [NotNull]
    public decimal Temp { get; set; }

    public Temperature(decimal temp)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        Temp = temp;
    }
}
