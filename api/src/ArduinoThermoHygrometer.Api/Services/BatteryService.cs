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
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the battery to retrieve.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, null.</returns>
    public async Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogRetrievingBatteryDtoByTimestamp(_logger);

        Battery? battery = await _batteryRepository.GetBatteryByTimestampAsync(timestamp);

        if (battery == null)
        {
            LoggingExtensions.LogBatteryIsNull(_logger);
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        LoggingExtensions.LogRetrievedBatteryDtoByTimestamp(_logger);

        return batteryDto;
    }

    /// <summary>
    /// Retrieves a list of all BatteryDto objects of a date asynchronously.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the batteries to retrieve.</param>
    /// <returns>Returns a list of <see cref="BatteryDto"/> objects if non-empty list; otherwise, null.</returns>
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

    /// <summary>
    /// Creates a BatteryDto object asynchronously.
    /// </summary>
    /// <param name="batteryDto">The <see cref="BatteryDto"/> object to add.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if created; otherwise, null.</returns>
    public async Task<BatteryDto> CreateBatteryDtoAsync(BatteryDto batteryDto)
    {
        LoggingExtensions.LogCreatingBattery(_logger);

        Battery? battery = BatteryMapper.GetBatteryFromBatteryDto(batteryDto);

        await _batteryRepository.CreateBatteryAsync(battery);
        await _batteryRepository.SaveChangesAsync();

        LoggingExtensions.LogCreatedBattery(_logger);

        return batteryDto;
    }

    /// <summary>
    /// Deletes a BatteryDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if deleted; otherwise, null.</returns>
    public async Task<BatteryDto?> DeleteBatteryDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogDeletingBattery(_logger);

        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Battery? battery = await _batteryRepository.DeleteBatteryByIdAsync(id);

        if (battery == null)
        {
            LoggingExtensions.LogBatteryIsNull(_logger);
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        await _batteryRepository.SaveChangesAsync();

        LoggingExtensions.LogDeletedBattery(_logger);

        return batteryDto;
    }

    /// <summary>
    /// Deletes a BatteryDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if deleted; otherwise, null.</returns>
    public async Task<BatteryDto?> DeleteBatteryDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogDeletingBattery(_logger);

        Battery? battery = await _batteryRepository.DeleteBatteryByTimestampAsync(timestamp);

        if (battery == null)
        {
            LoggingExtensions.LogBatteryIsNull(_logger);
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        await _batteryRepository.SaveChangesAsync();

        LoggingExtensions.LogDeletedBattery(_logger);

        return batteryDto;
    }
}
