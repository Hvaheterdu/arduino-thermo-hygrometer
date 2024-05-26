using ArduinoThermometer.API.DTOs;
using FluentValidation;

namespace ArduinoThermometer.API.Validators;

public class BatteryDtoValidator : AbstractValidator<BatteryDto>
{
    public BatteryDtoValidator()
    {
        RuleFor(b => b.Date).NotEmpty().NotNull().LessThanOrEqualTo(DateTimeOffset.Now.Date);
        RuleFor(b => b.Time).NotEmpty().NotNull().LessThanOrEqualTo(DateTimeOffset.Now.TimeOfDay);
        RuleFor(b => b.BatteryStatus).NotEmpty().NotNull();
    }
}
