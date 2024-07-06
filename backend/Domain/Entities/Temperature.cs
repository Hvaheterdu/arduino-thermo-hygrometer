using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Temperature
{
    [Key]
    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; init; }

    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid TemperatureGuid { get; init; }

    [Required]
    public DateOnly Date { get; init; }

    [Required]
    public TimeOnly Time { get; init; }

    [Required]
    [StringLength(10)]
    public string Temp { get; set; } = string.Empty;

    [Required]
    [StringLength(10)]
    public string AirHumidity { get; set; } = string.Empty;

    public Temperature(string temp, string airHumidity)
    {
        TemperatureGuid = Guid.NewGuid();
        Date = DateOnly.FromDateTime(DateTimeOffset.Now.Date);
        Time = TimeOnly.FromTimeSpan(DateTimeOffset.Now.TimeOfDay);
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
