using ArduinoThermoHygrometer.Web.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Validators;

public class TemperatureDtoValidator : AbstractValidator<TemperatureDto>
{
    public TemperatureDtoValidator()
    {
        RuleFor(t => t.Temp)
            .GreaterThanOrEqualTo(-55)
            .LessThanOrEqualTo(125)
            .NotEmpty()
            .NotNull();
    }
}