using ArduinoThermoHygrometer.Api.DTOs;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;

namespace ArduinoThermoHygrometer.Api.Services;

public class BatteryService : IBatteryService
{
    private readonly IBatteryRepository _batteryRepository;
    private readonly ILogger<BatteryService> _logger;

    public BatteryService(IBatteryRepository batteryRepository, ILogger<BatteryService> logger)
    {
        _batteryRepository = batteryRepository;
        _logger = logger;
    }

    public Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<BatteryDto?> GetBatteryDtoByDateAndTimeAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<IEnumerable<BatteryDto?>> GetAllBatteryDtosWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp)
    {
        throw new NotImplementedException();
    }

    public Task<BatteryDto?> AddBatteryDtoAsync(BatteryDto batteryDto)
    {
        throw new NotImplementedException();
    }

    public BatteryDto? RemoveBatteryDto(BatteryDto batteryDto)
    {
        throw new NotImplementedException();
    }

    public Task SaveChangesAsync()
    {
        throw new NotImplementedException();
    }
}
