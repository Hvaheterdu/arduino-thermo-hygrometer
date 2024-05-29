using ArduinoThermoHygrometer.DTOs;
using FluentValidation;

namespace ArduinoThermoHygrometer.Validators;

public class TemperatureDtoValidator : AbstractValidator<TemperatureDto>
{
    public TemperatureDtoValidator()
    {
        RuleFor(t => t.Temp).NotEmpty().NotNull();
        RuleFor(t => t.AirHumidity).NotEmpty().NotNull();
    }
}