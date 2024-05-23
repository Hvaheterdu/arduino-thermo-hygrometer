﻿namespace ArduinoThermometer.Models.Models;

public class Temperature
{
    public int TemperatureId { get; private set; }

    public Guid TemperatureGUID { get; private set; } = Guid.NewGuid();

    public DateTimeOffset TemperatureReadingDate { get; private set; } = DateTimeOffset.Now.Date;

    public DateTimeOffset TemperatureReadingTime { get; private set; } = DateTimeOffset.Now;

    public decimal TemperatureReading { get; set; } = decimal.Zero;

    public Temperature(decimal temperatureReading)
    {
        TemperatureReading = temperatureReading;
    }
}