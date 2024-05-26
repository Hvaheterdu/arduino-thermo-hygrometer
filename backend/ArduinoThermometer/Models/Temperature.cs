using System.ComponentModel.DataAnnotations;

namespace ArduinoThermometer.API.Models;

public class Temperature
{
    [Required]
    public int Id { get; private set; }

    [Required]
    public Guid TemperatureGuid { get; private set; } = Guid.NewGuid();

    [Required]
    public DateTimeOffset Date { get; set; } = DateTimeOffset.Now.Date;

    [Required]
    public TimeSpan Time { get; set; } = DateTimeOffset.Now.TimeOfDay;

    [Required]
    [StringLength(10)]
    public string Temp { get; set; } = string.Empty;

    [Required]
    [StringLength(10)]
    public string Humidity { get; set; } = string.Empty;

    public Temperature(DateTimeOffset date, TimeSpan time, string temp, string humidity)
    {
        Date = date;
        Time = time;
        Temp = temp;
        Humidity = humidity;
    }
}
