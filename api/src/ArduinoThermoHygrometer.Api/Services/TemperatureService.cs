using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services;

public class TemperatureService : ITemperatureService
{
    private readonly ITemperatureRepository _temperatureRepository;
    private readonly ILogger<TemperatureService> _logger;

    public TemperatureService(ITemperatureRepository temperatureRepository, ILogger<TemperatureService> logger)
    {
        _temperatureRepository = temperatureRepository;
        _logger = logger;
    }

    public Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id) => throw new NotImplementedException();

    public Task<TemperatureDto?> GetTemperatureDtoByTimestampAsync(DateTimeOffset registeredAt) => throw new NotImplementedException();

    public Task<IEnumerable<TemperatureDto>?> GetTemperatureDtosByDateAsync(DateTimeOffset dateTimeOffset) => throw new NotImplementedException();

    public Task<TemperatureDto> CreateTemperatureDtoAsync(TemperatureDto temperatureDto) => throw new NotImplementedException();

    public Task<TemperatureDto?> DeleteTemperatureDtoByIdAsync(Guid id) => throw new NotImplementedException();

    public Task<TemperatureDto?> DeleteTemperatureDtoByTimestampAsync(DateTimeOffset timestamp) => throw new NotImplementedException();
}
