using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

#pragma warning disable CA1819

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; init; }

    [Required]
    public Guid BatteryGuid { get; init; }

    [Required]
    public DateTimeOffset CreatedAt { get; init; }

    [Required]
    public int BatteryStatus { get; set; }

    [Timestamp]
    public byte[]? Version { get; set; }

    public Battery(int batteryStatus)
    {
        BatteryGuid = Guid.NewGuid();
        CreatedAt = DateTimeOffset.Now;
        BatteryStatus = batteryStatus;
    }
}
