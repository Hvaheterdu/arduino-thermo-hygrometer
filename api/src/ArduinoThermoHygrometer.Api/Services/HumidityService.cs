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

    public Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<HumidityDto?> GetHumidityDtoByTimestampAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<IEnumerable<HumidityDto?>> GetAllBatteryDtosWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp)
    {
        throw new NotImplementedException();
    }

    public Task<HumidityDto?> AddHumidityDtoAsync(HumidityDto humidityDto)
    {
        throw new NotImplementedException();
    }

    public HumidityDto? RemoveHumidityDto(HumidityDto humidityDto)
    {
        throw new NotImplementedException();
    }
}
