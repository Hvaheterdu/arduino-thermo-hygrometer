using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [Required]
    [RegularExpression(@"^[\dA-Fa-f]{8}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{12}$")]
    /// <summary>
    /// Battery id.
    /// </summary>
    /// <example>00000000-0000-0000-0000-000000000000</example>
    public Guid Id { get; init; }

    [Required]
    /// <summary>
    /// Time when battery was registered.
    /// </summary>
    /// <example>2024-12-12T20:22:42.6659487+01:00</example>
    public DateTimeOffset RegisteredAt { get; init; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(0, 100)]
    [NotNull]
    /// <summary>
    /// Collected battery status as percentage of 100.
    /// </summary>
    /// <example>98</example>
    public int BatteryStatus { get; set; }

    public Battery(int batteryStatus)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        BatteryStatus = batteryStatus;
    }
}
