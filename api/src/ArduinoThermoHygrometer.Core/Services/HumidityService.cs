using ArduinoThermoHygrometer.Core.Logging;
using ArduinoThermoHygrometer.Core.Mappers;
using ArduinoThermoHygrometer.Core.Repositories.Contracts;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Core.Utilities;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using Microsoft.Extensions.Logging;

namespace ArduinoThermoHygrometer.Core.Services;

public class HumidityService : IHumidityService
{
    private readonly IHumidityRepository _humidityRepository;
    private readonly ILogger<HumidityService> _logger;

    public HumidityService(IHumidityRepository humidityRepository, ILogger<HumidityService> logger)
    {
        _humidityRepository = humidityRepository;
        _logger = logger;
    }

    /// <summary>
    /// Retrieves a HumidityDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="HumidityDto"/> object if found; otherwise, null.</returns>
    public async Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id)
    {
        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Humidity? humidity = await _humidityRepository.GetHumidityByIdAsync(id);

        if (humidity == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Humidity));
            return null;
        }

        HumidityDto humidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        LoggingExtensions.LogDtoObjectToReturn(
            _logger, nameof(BatteryDto), humidityDto.Id, humidityDto.RegisteredAt.Date.ToShortDateString()
        );

        return humidityDto;
    }

    /// <summary>
    /// Retrieves a HumidityDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="HumidityDto"/> object if found; otherwise, null.</returns>
    public async Task<HumidityDto?> GetHumidityDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        Humidity? humidity = await _humidityRepository.GetHumidityByTimestampAsync(timestamp);

        if (humidity == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Humidity));
            return null;
        }

        HumidityDto humidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        LoggingExtensions.LogDtoObjectToReturn(
            _logger, nameof(BatteryDto), humidityDto.Id, humidityDto.RegisteredAt.Date.ToShortDateString()
        );

        return humidityDto;
    }

    /// <summary>
    /// Retrieves a list of all HumidityDto objects of a date asynchronously.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="HumidityDto"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<HumidityDto>?> GetHumidityDtosByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Humidity> humidities = await _humidityRepository.GetHumiditiesByDateAsync(dateTimeOffset);

        string? capitaliseHumidities = StringUtilities.CapitaliseFirstLetter(nameof(humidities));
        if (humidities == null || !humidities.Any())
        {
            LoggingExtensions.LogIsNullOrEmpty(_logger, capitaliseHumidities, dateTimeOffset.Date.ToShortDateString());
            return null;
        }

        List<HumidityDto> humidityDtos = new();
        foreach (Humidity humidity in humidities)
        {
            HumidityDto humidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);
            humidityDtos.Add(humidityDto);

            LoggingExtensions.LogDtoObjectToReturn(
                _logger, nameof(BatteryDto), humidityDto.Id, humidityDto.RegisteredAt.Date.ToShortDateString()
            );
        }

        return humidityDtos;
    }

    /// <summary>
    /// Creates a HumidityDto object asynchronously.
    /// </summary>
    /// <param name="humidityDto">The <see cref="HumidityDto"/> object to create.</param>
    /// <returns>Returns the <see cref="HumidityDto"/> object if created; otherwise, null.</returns>
    public async Task<HumidityDto> CreateHumidityDtoAsync(HumidityDto humidityDto)
    {
        Humidity? humidity = HumidityMapper.GetHumidityFromHumidityDto(humidityDto);

        await _humidityRepository.CreateHumidityAsync(humidity);
        await _humidityRepository.SaveChangesAsync();

        HumidityDto createdHumidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        LoggingExtensions.LogDtoObjectToCreate(
            _logger, nameof(BatteryDto), createdHumidityDto.Id, createdHumidityDto.RegisteredAt.Date.ToShortDateString()
        );

        return createdHumidityDto;
    }

    /// <summary>
    /// Deletes a HumidityDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="HumidityDto"/> object if deleted; otherwise, null.</returns>
    public async Task<HumidityDto?> DeleteHumidityDtoByIdAsync(Guid id)
    {
        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Humidity? humidity = await _humidityRepository.DeleteHumidityByIdAsync(id);

        if (humidity == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Humidity));
            return null;
        }

        HumidityDto humidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        await _humidityRepository.SaveChangesAsync();

        LoggingExtensions.LogDtoObjectToDelete(
            _logger, nameof(BatteryDto), humidityDto.Id, humidityDto.RegisteredAt.Date.ToShortDateString()
        );

        return humidityDto;
    }

    /// <summary>
    /// Deletes a HumidityDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="HumidityDto"/> object if deleted; otherwise, null.</returns>
    public async Task<HumidityDto?> DeleteHumidityDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        Humidity? humidity = await _humidityRepository.DeleteHumidityByTimestampAsync(timestamp);

        if (humidity == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Humidity));
            return null;
        }

        HumidityDto humidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        await _humidityRepository.SaveChangesAsync();

        LoggingExtensions.LogDtoObjectToDelete(
            _logger, nameof(BatteryDto), humidityDto.Id, humidityDto.RegisteredAt.Date.ToShortDateString()
        );

        return humidityDto;
    }
}
