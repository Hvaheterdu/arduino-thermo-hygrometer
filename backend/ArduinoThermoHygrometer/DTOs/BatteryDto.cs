using System.ComponentModel.DataAnnotations;

namespace ArduinoThermoHygrometer.Web.DTOs;

public record BatteryDto
{
    [Required]
    [StringLength(10)]
    public string BatteryStatus { get; set; } = default!;
}
