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
    /// Retrieves a <see cref="BatteryDto"/> object by its unique identifier (ID) asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the battery to retrieve. Must not be an empty or invalid GUID.</param>
    /// <returns>Returns a <see cref="BatteryDto"/> object if found; otherwise, <c>null</c>.</returns>
    /// <exception cref="ArgumentException">Thrown when the provided ID is not a valid <see cref="Guid"/>.</exception>
    public async Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogRetrievingBatteryDtoById(_logger);

        if (id == Guid.Empty)
        {
            LoggingExtensions.LogEmptyId(_logger, id);
            return null;
        }

        bool isIdValidGuid = Guid.TryParse(id.ToString(), out _);
        if (!isIdValidGuid)
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

    public Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset registeredAt)
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
}
