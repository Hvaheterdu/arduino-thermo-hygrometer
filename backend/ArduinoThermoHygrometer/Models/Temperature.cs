using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ArduinoThermoHygrometer.Models;

public class Temperature
{
    [Key]
    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; init; }

    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid TemperatureGuid { get; init; } = Guid.NewGuid();

    [Required]
    public DateTimeOffset Date { get; init; } = DateTimeOffset.Now.Date;

    [Required]
    public TimeSpan Time { get; init; } = DateTimeOffset.Now.TimeOfDay;

    [Required]
    [StringLength(10)]
    public string Temp { get; set; } = string.Empty;

    [Required]
    [StringLength(10)]
    public string AirHumidity { get; set; } = string.Empty;

    public Temperature(string temp, string airHumidity)
    {
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
