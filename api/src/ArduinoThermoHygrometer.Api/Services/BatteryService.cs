using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Mappers;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using Microsoft.IdentityModel.Tokens;

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

    /// <summary>
    /// Retrieves a BatteryDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="registeredAt">The <see cref="DateTimeOffset"/> of the battery to retrieve.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, null.</returns>
    public async Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset registeredAt)
    {
        LoggingExtensions.LogRetrievingBatteryDtoByTimestamp(_logger);

        Battery? battery = await _batteryRepository.GetBatteryByTimestampAsync(registeredAt);

        if (battery == null)
        {
            LoggingExtensions.LogBatteryIsNull(_logger);
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        LoggingExtensions.LogRetrievedBatteryDtoByTimestamp(_logger);

        return batteryDto;
    }

    public async Task<IEnumerable<BatteryDto>?> GetBatteryDtosByDateAsync(DateTimeOffset dateTimeOffset)
    {
        LoggingExtensions.LogRetrievingBatteryDtoByDate(_logger);

        IEnumerable<Battery> batteries = await _batteryRepository.GetBatteriesByDateAsync(dateTimeOffset);

        if (batteries.IsNullOrEmpty())
        {
            LoggingExtensions.LogBatteryIsNull(_logger);
            return null;
        }

        List<BatteryDto> batteryDtos = new();
        foreach (Battery battery in batteries)
        {
            batteryDtos.Add(BatteryMapper.GetBatteryDtoFromBattery(battery));
        }

        LoggingExtensions.LogRetrievedBatteryDtoByDate(_logger);

        return batteryDtos;
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
