using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

#pragma warning disable CA1819

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int BatteryId { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; init; }

    [Required]
    public int BatteryStatus { get; set; }

    [Timestamp]
    public byte[]? Version { get; set; }

    public Battery(int batteryStatus)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        BatteryStatus = batteryStatus;
    }
}
