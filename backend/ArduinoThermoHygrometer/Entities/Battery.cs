using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ArduinoThermoHygrometer.Entities;

public class Battery
{
    [Key]
    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; init; }

    [Required]
    public int TemperatureId { get; init; }

    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid BatteryGuid { get; init; }

    [Required]
    public DateOnly Date { get; init; }

    [Required]
    public TimeOnly Time { get; init; }

    [Required]
    [StringLength(10)]
    public string BatteryStatus { get; set; } = string.Empty;

    public Battery(string batteryStatus)
    {
        BatteryGuid = Guid.NewGuid();
        Date = DateOnly.FromDateTime(DateTimeOffset.Now.Date);
        Time = TimeOnly.FromTimeSpan(DateTimeOffset.Now.TimeOfDay);
        BatteryStatus = batteryStatus;
    }
}
