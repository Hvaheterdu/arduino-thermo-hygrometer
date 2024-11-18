using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Humidity
{
    [Key]
    [Required]
    public Guid Id { get; init; }

    [Required]
    public DateTimeOffset RegisteredAt { get; init; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(20, 90)]
    [NotNull]
    public decimal AirHumidity { get; set; }

    public Humidity(decimal airHumidity)
    {
        Id = Guid.NewGuid();
        RegisteredAt = DateTimeOffset.Now;
        AirHumidity = airHumidity;
    }
}
