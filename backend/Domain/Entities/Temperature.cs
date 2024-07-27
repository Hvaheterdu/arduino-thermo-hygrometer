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
    [RegularExpression("^[0-9]{1,3}([.][0-9]{1,4})?$")]
    public string Temp { get; set; } = string.Empty;

    [Required]
    [StringLength(10)]
    [RegularExpression("^[0-9]{1,3}$")]
    public string AirHumidity { get; set; } = string.Empty;

    [Timestamp]
    public byte[]? Version { get; set; }

    public Temperature(string temp, string airHumidity)
    {
        TemperatureGuid = Guid.NewGuid();
        CreatedAt = DateTimeOffset.Now;
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
