using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ArduinoThermoHygrometer.Entities;

public class Battery
{
    [Key]
    [Required]
    public int Id { get; init; }

    [Required]
    public int TemperatureId { get; init; }

    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid BatteryGuid { get; init; } = Guid.NewGuid();

    [Required]
    public DateTimeOffset Date { get; init; } = DateTimeOffset.Now.Date;

    [Required]
    public TimeSpan Time { get; init; } = DateTimeOffset.Now.TimeOfDay;

    [Required]
    [StringLength(10)]
    public string BatteryStatus { get; set; }

    public Battery(string batteryStatus)
    {
        BatteryStatus = batteryStatus;
    }
}
