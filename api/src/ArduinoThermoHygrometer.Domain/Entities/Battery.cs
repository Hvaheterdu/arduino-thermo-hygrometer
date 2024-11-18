using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; init; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(0, 100)]
    [NotNull]
    public int BatteryStatus { get; set; }

    public Battery(int batteryStatus)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        BatteryStatus = batteryStatus;
    }
}
