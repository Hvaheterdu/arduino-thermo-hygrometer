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
    public decimal Temp { get; set; }

    [Required]
    public decimal AirHumidity { get; set; }

    [Timestamp]
    public byte[]? Version { get; set; }

    public Temperature(decimal temp, decimal airHumidity)
    {
        TemperatureGuid = Guid.NewGuid();
        CreatedAt = DateTimeOffset.Now;
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
