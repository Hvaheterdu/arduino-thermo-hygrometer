using ArduinoThermoHygrometer.Web.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Validators;

public class BatteryDtoValidator : AbstractValidator<BatteryDto>
{
    public BatteryDtoValidator()
    {
        RuleFor(b => b.BatteryStatus)
            .MaximumLength(10)
            .NotEmpty()
            .NotNull();
    }
}
