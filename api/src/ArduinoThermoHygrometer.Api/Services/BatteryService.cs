using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Mappers;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Api.Utilities;
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
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, null.</returns>
    public async Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogRetrievingDtoById(_logger, nameof(BatteryDto));

        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Battery? battery = await _batteryRepository.GetBatteryByIdAsync(id);

        if (battery == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Battery));
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        LoggingExtensions.LogRetrievedDtoById(_logger, nameof(BatteryDto));

        return batteryDto;
    }

    /// <summary>
    /// Retrieves a BatteryDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, null.</returns>
    public async Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogRetrievingDtoByTimestamp(_logger, nameof(BatteryDto));

        Battery? battery = await _batteryRepository.GetBatteryByTimestampAsync(timestamp);

        if (battery == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Battery));
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        LoggingExtensions.LogRetrievedDtoByTimestamp(_logger, nameof(BatteryDto));

        return batteryDto;
    }

    /// <summary>
    /// Retrieves a list of all BatteryDto objects of a date asynchronously.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="BatteryDto"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<BatteryDto>?> GetBatteryDtosByDateAsync(DateTimeOffset dateTimeOffset)
    {
        LoggingExtensions.LogRetrievingDtoByDate(_logger, $"{nameof(BatteryDto)}s");

        IEnumerable<Battery> batteries = await _batteryRepository.GetBatteriesByDateAsync(dateTimeOffset);

        string? capitaliseBatteries = StringUtilities.CapitaliseFirstLetter(nameof(batteries));
        if (batteries.IsNullOrEmpty())
        {
            LoggingExtensions.LogIsNullOrEmpty(_logger, capitaliseBatteries, dateTimeOffset.Date.ToShortDateString());
            return null;
        }

        List<BatteryDto> batteryDtos = new();
        foreach (Battery battery in batteries)
        {
            batteryDtos.Add(BatteryMapper.GetBatteryDtoFromBattery(battery));
        }

        LoggingExtensions.LogRetrievedDtoByDate(_logger, $"{nameof(BatteryDto)}s");

        return batteryDtos;
    }

    /// <summary>
    /// Creates a BatteryDto object asynchronously.
    /// </summary>
    /// <param name="batteryDto">The <see cref="BatteryDto"/> object to create.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if created; otherwise, null.</returns>
    public async Task<BatteryDto> CreateBatteryDtoAsync(BatteryDto batteryDto)
    {
        LoggingExtensions.LogCreating(_logger, nameof(BatteryDto));

        Battery? battery = BatteryMapper.GetBatteryFromBatteryDto(batteryDto);

        await _batteryRepository.CreateBatteryAsync(battery);
        await _batteryRepository.SaveChangesAsync();

        LoggingExtensions.LogCreated(_logger, nameof(BatteryDto));

        return batteryDto;
    }

    /// <summary>
    /// Deletes a BatteryDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if deleted; otherwise, null.</returns>
    public async Task<BatteryDto?> DeleteBatteryDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogDeletingById(_logger, nameof(BatteryDto));

        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Battery? battery = await _batteryRepository.DeleteBatteryByIdAsync(id);

        if (battery == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Battery));
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        await _batteryRepository.SaveChangesAsync();

        LoggingExtensions.LogDeletedById(_logger, nameof(BatteryDto));

        return batteryDto;
    }

    /// <summary>
    /// Deletes a BatteryDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if deleted; otherwise, null.</returns>
    public async Task<BatteryDto?> DeleteBatteryDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogDeletingByTimestamp(_logger, nameof(BatteryDto));

        Battery? battery = await _batteryRepository.DeleteBatteryByTimestampAsync(timestamp);

        if (battery == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Battery));
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        await _batteryRepository.SaveChangesAsync();

        LoggingExtensions.LogDeletedByTimestamp(_logger, nameof(BatteryDto));

        return batteryDto;
    }
}
