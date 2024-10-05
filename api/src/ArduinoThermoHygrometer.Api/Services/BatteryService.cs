using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Mappers;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

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

    /// <summary>
    /// Retrieves a BatteryDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the battery to retrieve.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, null.</returns>
    public async Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogRetrievingBatteryDtoById(_logger);

        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Battery? battery = await _batteryRepository.GetBatteryByIdAsync(id);

        if (battery == null)
        {
            LoggingExtensions.LogBatteryIsNull(_logger);
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        LoggingExtensions.LogRetrievedBatteryDtoById(_logger);

        return batteryDto;
    }

    public Task<BatteryDto?> GetBatteryDtoByDateAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<IEnumerable<BatteryDto?>> GetBatteryDtosByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate)
    {
        throw new NotImplementedException();
    }

    public Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<IEnumerable<BatteryDto?>> GetBatteryDtosByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp)
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
}
