namespace ArduinoThermometer.API.Models;

public class Temperature
{
    public int TemperatureId { get; private set; }

    public Guid TemperatureGUID { get; private set; } = Guid.NewGuid();

    public DateTimeOffset TemperatureReadingDate { get; private set; } = DateTimeOffset.Now.Date;

    public DateTimeOffset TemperatureReadingTime { get; private set; } = DateTimeOffset.Now;

    public string TemperatureValue { get; set; } = string.Empty;

    public Temperature(string temperatureValue)
    {
        TemperatureValue = temperatureValue;
    }
}
