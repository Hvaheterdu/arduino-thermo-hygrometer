using System.ComponentModel.DataAnnotations;

namespace ArduinoThermometer.API.DTOs;

public record BatteryDto
{
    [Required]
    public DateTimeOffset Date { get; set; } = default!;

    [Required]
    public TimeSpan Time { get; set; } = default!;

    [Required]
    [StringLength(10)]
    public string BatteryStatus { get; set; } = default!;
}
