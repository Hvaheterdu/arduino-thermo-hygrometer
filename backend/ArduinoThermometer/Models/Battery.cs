using System.ComponentModel.DataAnnotations;

namespace ArduinoThermometer.API.Models;

public class Battery
{
    [Required]
    public int Id { get; private set; }

    [Required]
    public int TemperatureId { get; private set; }

    [Required]
    public Guid BatteryGuid { get; private set; } = Guid.NewGuid();

    [Required]
    public DateTimeOffset Date { get; set; } = DateTimeOffset.Now.Date;

    [Required]
    public TimeSpan Time { get; set; } = DateTimeOffset.Now.TimeOfDay;

    [Required]
    [StringLength(10)]
    public string BatteryStatus { get; set; }

    public Battery(DateTimeOffset date, TimeSpan time, string batteryStatus)
    {
        Date = date;
        Time = time;
        BatteryStatus = batteryStatus;
    }
}
