﻿using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ArduinoThermoHygrometer.Domain.Entities;

public class Battery
{
    [Key]
    [Required]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; init; }

    [Required]
    public Guid BatteryGuid { get; init; }

    [Required]
    public DateTimeOffset CreatedAt { get; init; }

    [Required]
    [StringLength(10)]
    public string BatteryStatus { get; set; } = string.Empty;

    public Battery(string batteryStatus)
    {
        BatteryGuid = Guid.NewGuid();
        CreatedAt = DateTimeOffset.Now;
        BatteryStatus = batteryStatus;
    }
}