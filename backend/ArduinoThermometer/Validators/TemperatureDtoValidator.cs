using ArduinoThermometer.API.Models;
using FluentValidation;

namespace ArduinoThermometer.API.Validators;

public class TemperatureDtoValidator : AbstractValidator<Temperature>
{
    public TemperatureDtoValidator()
    {
        RuleFor(t => t.TemperatureGUID).NotEmpty().NotNull();
        RuleFor(t => t.TemperatureReadingDate).NotEmpty().NotNull().LessThanOrEqualTo(DateTimeOffset.Now.Date);
        RuleFor(t => t.TemperatureReadingTime).NotEmpty().NotNull().LessThanOrEqualTo(DateTimeOffset.Now);
        RuleFor(t => t.TemperatureValue).NotEmpty().NotNull();
    }
}