using ArduinoThermoHygrometer.Web.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Validators;

public class TemperatureDtoValidator : AbstractValidator<TemperatureDto>
{
    public TemperatureDtoValidator()
    {
        RuleFor(t => t.Temp)
            .MaximumLength(10)
            .Matches("^[0-9]*$")
            .NotEmpty()
            .NotNull();

        RuleFor(t => t.AirHumidity)
            .MaximumLength(10)
            .Matches("^[0-9]*$")
            .NotEmpty()
            .NotNull();
    }
}