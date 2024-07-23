using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Temperature
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; init; }

    [Required]
    public Guid TemperatureGuid { get; init; }

    [Required]
    public DateTimeOffset CreatedAt { get; init; }

    [Required]
    [StringLength(10)]
    public string Temp { get; set; } = string.Empty;

    [Required]
    [StringLength(10)]
    public string AirHumidity { get; set; } = string.Empty;

    public Temperature(string temp, string airHumidity)
    {
        TemperatureGuid = Guid.NewGuid();
        CreatedAt = DateTimeOffset.Now;
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
