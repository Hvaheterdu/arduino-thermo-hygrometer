using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;
using System.Text.Json.Serialization;

namespace ArduinoThermoHygrometer.Domain.DTOs;

public record BatteryDto
{
    [JsonIgnore]
    public Guid Id { get; set; }

    public DateTimeOffset RegisteredAt { get; set; }

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(0, 100)]
    [NotNull]
    public int BatteryStatus { get; set; }
}
