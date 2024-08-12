using ArduinoThermoHygrometer.Web.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Validators;

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
