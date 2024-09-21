using ArduinoThermoHygrometer.Api.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Api.Validators;

public class BatteryDtoValidator : AbstractValidator<BatteryDto>
{
    public BatteryDtoValidator()
    {
        RuleFor(b => b.BatteryStatus)
            .GreaterThanOrEqualTo(0)
            .LessThanOrEqualTo(100)
            .NotEmpty()
            .NotNull();
    }
}
