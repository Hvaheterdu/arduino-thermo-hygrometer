﻿using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;
using System.Text.Json.Serialization;

namespace ArduinoThermoHygrometer.Domain.DTOs;

public record TemperatureDto
{
    [JsonIgnore]
    public Guid Id { get; set; } = default!;

    public DateTimeOffset RegisteredAt { get; set; } = default!;

    [Required]
    [RegularExpression(@"^(?!\s*$).*")]
    [Range(-55, 125)]
    [NotNull]
    public decimal Temp { get; set; } = default!;
}
