using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Core.Mappers;
using ArduinoThermoHygrometer.Core.Utilities;
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
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, null.</returns>
    public async Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id)
    {
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

        return batteryDto;
    }

    /// <summary>
    /// Retrieves a BatteryDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, null.</returns>
    public async Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        Battery? battery = await _batteryRepository.GetBatteryByTimestampAsync(timestamp);

        if (battery == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Battery));
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        return batteryDto;
    }

    /// <summary>
    /// Retrieves a list of all BatteryDto objects of a date asynchronously.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="BatteryDto"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<BatteryDto>?> GetBatteryDtosByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Battery> batteries = await _batteryRepository.GetBatteriesByDateAsync(dateTimeOffset);

        string? capitaliseBatteries = StringUtilities.CapitaliseFirstLetter(nameof(batteries));
        if (batteries == null || !batteries.Any())
        {
            LoggingExtensions.LogIsNullOrEmpty(_logger, capitaliseBatteries, dateTimeOffset.Date.ToShortDateString());
            return null;
        }

        List<BatteryDto> batteryDtos = new();
        foreach (Battery battery in batteries)
        {
            batteryDtos.Add(BatteryMapper.GetBatteryDtoFromBattery(battery));
        }

        return batteryDtos;
    }

    /// <summary>
    /// Creates a BatteryDto object asynchronously.
    /// </summary>
    /// <param name="batteryDto">The <see cref="BatteryDto"/> object to create.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if created; otherwise, null.</returns>
    public async Task<BatteryDto> CreateBatteryDtoAsync(BatteryDto batteryDto)
    {
        Battery? battery = BatteryMapper.GetBatteryFromBatteryDto(batteryDto);

        await _batteryRepository.CreateBatteryAsync(battery);
        await _batteryRepository.SaveChangesAsync();

        return batteryDto;
    }

    /// <summary>
    /// Deletes a BatteryDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if deleted; otherwise, null.</returns>
    public async Task<BatteryDto?> DeleteBatteryDtoByIdAsync(Guid id)
    {
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

        return batteryDto;
    }

    /// <summary>
    /// Deletes a BatteryDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="BatteryDto"/> object if deleted; otherwise, null.</returns>
    public async Task<BatteryDto?> DeleteBatteryDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        Battery? battery = await _batteryRepository.DeleteBatteryByTimestampAsync(timestamp);

        if (battery == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Battery));
            return null;
        }

        BatteryDto batteryDto = BatteryMapper.GetBatteryDtoFromBattery(battery);

        await _batteryRepository.SaveChangesAsync();

        return batteryDto;
    }
}
