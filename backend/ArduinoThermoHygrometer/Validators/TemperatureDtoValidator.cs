using ArduinoThermoHygrometer.Api.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Api.Validators;

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
