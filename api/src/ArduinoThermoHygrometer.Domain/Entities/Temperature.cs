using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Temperature
{
    [Key]
    [Required]
    [RegularExpression(@"^[\dA-Fa-f]{8}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{12}$")]
    /// <summary>
    /// Temperature id.
    /// </summary>
    /// <example>00000000-0000-0000-0000-000000000000</example>
    public Guid Id { get; init; }

    [Required]
    /// <summary>
    /// Registration time.
    /// </summary>
    /// <example>2024-12-12T20:22:42.6659487+01:00</example>
    public DateTimeOffset RegisteredAt { get; init; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(-55, 125)]
    [NotNull]
    /// <summary
    /// Measured outside temperature.
    /// </summary>
    /// <example>-35.74</example>
    public decimal Temp { get; set; }

    public Temperature(decimal temp)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        Temp = temp;
    }
}
