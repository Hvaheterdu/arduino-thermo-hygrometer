using System.ComponentModel.DataAnnotations;

namespace ArduinoThermoHygrometer.Web.DTOs;

public record TemperatureDto
{
    [Required]
    public DateTimeOffset CreatedAt { get; set; } = default!;

    [Required]
    [StringLength(10)]
    public string Temp { get; set; } = default!;

    [Required]
    [StringLength(10)]
    public string AirHumidity { get; set; } = default!;
}
