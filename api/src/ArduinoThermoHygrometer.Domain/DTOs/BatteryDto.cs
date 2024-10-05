using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;
using System.Text.Json.Serialization;

namespace ArduinoThermoHygrometer.Domain.DTOs;

public record BatteryDto
{
    [JsonIgnore]
    public Guid Id { get; set; } = default!;

    public DateTimeOffset RegisteredAt { get; set; } = default!;

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(0, 100)]
    [NotNull]
    public int BatteryStatus { get; set; } = default!;
}
