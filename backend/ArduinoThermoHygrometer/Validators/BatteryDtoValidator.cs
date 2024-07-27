using ArduinoThermoHygrometer.Web.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Validators;

public class BatteryDtoValidator : AbstractValidator<BatteryDto>
{
    public BatteryDtoValidator()
    {
        RuleFor(b => b.BatteryStatus)
            .MaximumLength(10)
            .Matches("^[0-9]{1,3}$")
            .NotEmpty()
            .NotNull();
    }
}
