using System.ComponentModel.DataAnnotations;

namespace ArduinoThermometer.API.DTOs;

public record TemperatureDto
{
    [Required]
    public DateTimeOffset Date { get; set; } = default!;

    [Required]
    public TimeSpan Time { get; set; } = default!;

    [Required]
    [StringLength(10)]
    public string Temp { get; set; } = default!;

    [Required]
    [StringLength(10)]
    public string Humidity { get; set; } = default!;
}
