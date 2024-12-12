using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Humidity
{
    [Key]
    [Required]
    [RegularExpression(@"^[\dA-Fa-f]{8}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{12}$")]
    /// <summary>
    /// Humidity id.
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
    [Range(20, 90)]
    [NotNull]
    /// <summary>
    /// Measured outside air humidity
    /// </summary>
    /// <example>78.24</example>
    public decimal AirHumidity { get; set; }

    public Humidity(decimal airHumidity)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        AirHumidity = airHumidity;
    }
}
