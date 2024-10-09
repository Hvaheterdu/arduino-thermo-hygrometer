using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; set; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(0, 100)]
    [NotNull]
    public int BatteryStatus { get; set; }

    [Timestamp]
    public byte[]? Version { get; set; }

    public Battery(DateTimeOffset registeredAt, int batteryStatus)
    {
        Id = Guid.NewGuid();
        RegisteredAt = registeredAt;
        BatteryStatus = batteryStatus;
    }
}
