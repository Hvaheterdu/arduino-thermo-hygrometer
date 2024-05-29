using ArduinoThermoHygrometer.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Validators;

public class BatteryDtoValidator : AbstractValidator<BatteryDto>
{
    public BatteryDtoValidator()
    {
        RuleFor(b => b.BatteryStatus).NotEmpty().NotNull();
    }
}
