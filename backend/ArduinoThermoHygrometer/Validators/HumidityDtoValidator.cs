using ArduinoThermoHygrometer.Web.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Validators;

public class HumidityDtoValidator : AbstractValidator<HumidityDto>
{
    public HumidityDtoValidator()
    {
        RuleFor(t => t.AirHumidity)
            .GreaterThanOrEqualTo(20)
            .LessThanOrEqualTo(90)
            .NotEmpty()
            .NotNull();
    }
}
