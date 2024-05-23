namespace ArduinoThermometer.API.Models;

public class Temperature
{
    public int Id { get; private set; }

    public Guid TemperatureGuid { get; private set; } = Guid.NewGuid();

    public DateTimeOffset Date { get; private set; } = DateTimeOffset.Now.Date;

    public DateTimeOffset Time { get; private set; } = DateTimeOffset.Now;

    public string Value { get; set; } = string.Empty;

    public Temperature(string value)
    {
        Value = value;
    }
}
