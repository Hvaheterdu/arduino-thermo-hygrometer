using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; init; }

    [Required]
    public int TemperatureId { get; init; }

    [Required]
    public Guid BatteryGuid { get; init; }

    [Required]
    public DateOnly CreatedDate { get; init; }

    [Required]
    public TimeOnly CreatedTime { get; init; }

    [Required]
    [StringLength(10)]
    public string BatteryStatus { get; set; } = string.Empty;

    public Battery(string batteryStatus)
    {
        BatteryGuid = Guid.NewGuid();
        CreatedDate = DateOnly.FromDateTime(DateTimeOffset.Now.Date);
        CreatedTime = TimeOnly.FromTimeSpan(DateTimeOffset.Now.TimeOfDay);
        BatteryStatus = batteryStatus;
    }
}
