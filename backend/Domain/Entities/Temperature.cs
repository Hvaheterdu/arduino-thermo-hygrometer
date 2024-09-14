using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

#pragma warning disable CA1819

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Temperature
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int TemperatureId { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; init; }

    [Required]
    public decimal Temp { get; set; }

    [Required]
    public decimal AirHumidity { get; set; }

    [Timestamp]
    public byte[]? Version { get; set; }

    public Temperature(decimal temp, decimal airHumidity)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        Temp = temp;
        AirHumidity = airHumidity;
    }
}
