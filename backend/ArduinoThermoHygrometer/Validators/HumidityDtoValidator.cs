using ArduinoThermoHygrometer.Api.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Api.Validators;

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
