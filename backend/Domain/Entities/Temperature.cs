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
    public Guid TemperatureGuid { get; init; }

    [Required]
    public DateOnly CreatedDate { get; init; }

    [Required]
    public TimeOnly CreatedTime { get; init; }

    [Required]
    [StringLength(10)]
    public string Temp { get; set; } = string.Empty;

    [Required]
    [StringLength(10)]
    public string AirHumidity { get; set; } = string.Empty;

    public Temperature(string temp, string airHumidity)
    {
        TemperatureGuid = Guid.NewGuid();
        CreatedDate = DateOnly.FromDateTime(DateTimeOffset.Now.Date);
        CreatedTime = TimeOnly.FromTimeSpan(DateTimeOffset.Now.TimeOfDay);
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
