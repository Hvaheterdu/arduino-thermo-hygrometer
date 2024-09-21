using System.ComponentModel.DataAnnotations;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; set; } = DateTimeOffset.Now;

    [Required]
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
