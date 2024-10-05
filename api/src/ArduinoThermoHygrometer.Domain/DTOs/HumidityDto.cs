using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;
using System.Text.Json.Serialization;

namespace ArduinoThermoHygrometer.Domain.DTOs;

public class HumidityDto
{
    [JsonIgnore]
    public Guid Id { get; set; } = default!;

    public DateTimeOffset RegisteredAt { get; set; } = default!;

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(20, 90)]
    [NotNull]
    public decimal AirHumidity { get; set; } = default!;
}
