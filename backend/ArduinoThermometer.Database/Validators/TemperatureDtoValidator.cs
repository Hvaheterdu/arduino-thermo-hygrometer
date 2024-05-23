using ArduinoThermometer.Data.Models;
using FluentValidation;

namespace ArduinoThermometer.Data.Validators;

public class TemperatureDtoValidator : AbstractValidator<Temperature>
{
    public TemperatureDtoValidator()
    {
        RuleFor(t => t.TemperatureGUID).NotEmpty().NotNull();
        RuleFor(t => t.TemperatureReadingDate).NotEmpty().NotNull().LessThanOrEqualTo(DateTimeOffset.Now.Date);
        RuleFor(t => t.TemperatureReadingTime).NotEmpty().NotNull().LessThanOrEqualTo(DateTimeOffset.Now);
        RuleFor(t => t.TemperatureReading).NotEmpty().NotNull();
    }
}