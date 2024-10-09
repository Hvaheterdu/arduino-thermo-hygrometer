using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services;

public class HumidityService : IHumidityService
{
    private readonly IHumidityRepository _humidityRepository;
    private readonly ILogger<HumidityService> _logger;

    public HumidityService(IHumidityRepository humidityRepository, ILogger<HumidityService> logger)
    {
        _humidityRepository = humidityRepository;
        _logger = logger;
    }

    public Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id) => throw new NotImplementedException();

    public Task<HumidityDto?> GetHumidityDtoByTimestampAsync(DateTimeOffset registeredAt) => throw new NotImplementedException();

    public Task<IEnumerable<HumidityDto>?> GetHumidityDtosByDateAsync(DateTimeOffset dateTimeOffset) => throw new NotImplementedException();

    public Task<HumidityDto> CreateHumidityDtoAsync(HumidityDto humidityDto) => throw new NotImplementedException();

    public Task<HumidityDto?> DeleteHumidityDtoByIdAsync(Guid id) => throw new NotImplementedException();

    public Task<HumidityDto?> DeleteHumidityDtoByTimestampAsync(DateTimeOffset timestamp) => throw new NotImplementedException();
}
