using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Core.Mappers;
using ArduinoThermoHygrometer.Core.Utilities;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;

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

    /// <summary>
    /// Retrieves a HumidityDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="HumidityDto"/> object if found; otherwise, null.</returns>
    public async Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogRetrievingDtoById(_logger, nameof(HumidityDto));

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

        LoggingExtensions.LogRetrievedDtoById(_logger, nameof(HumidityDto));

        return humidityDto;
    }

    /// <summary>
    /// Retrieves a HumidityDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="HumidityDto"/> object if found; otherwise, null.</returns>
    public async Task<HumidityDto?> GetHumidityDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogRetrievingDtoByTimestamp(_logger, nameof(HumidityDto));

        Humidity? humidity = await _humidityRepository.GetHumidityByTimestampAsync(timestamp);

        if (humidity == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Humidity));
            return null;
        }

        HumidityDto humidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        LoggingExtensions.LogRetrievedDtoByTimestamp(_logger, nameof(HumidityDto));

        return humidityDto;
    }

    /// <summary>
    /// Retrieves a list of all HumidityDto objects of a date asynchronously.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="HumidityDto"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<HumidityDto>?> GetHumidityDtosByDateAsync(DateTimeOffset dateTimeOffset)
    {
        LoggingExtensions.LogRetrievingDtoByDate(_logger, $"{nameof(HumidityDto)}s");

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
            humidityDtos.Add(HumidityMapper.GetHumidityDtoFromHumidity(humidity));
        }

        LoggingExtensions.LogRetrievedDtoByDate(_logger, $"{nameof(HumidityDto)}s");

        return humidityDtos;
    }

    /// <summary>
    /// Creates a HumidityDto object asynchronously.
    /// </summary>
    /// <param name="humidityDto">The <see cref="HumidityDto"/> object to create.</param>
    /// <returns>Returns the <see cref="HumidityDto"/> object if created; otherwise, null.</returns>
    public async Task<HumidityDto> CreateHumidityDtoAsync(HumidityDto humidityDto)
    {
        LoggingExtensions.LogCreating(_logger, nameof(HumidityDto));

        Humidity? humidity = HumidityMapper.GetHumidityFromHumidityDto(humidityDto);

        await _humidityRepository.CreateHumidityAsync(humidity);
        await _humidityRepository.SaveChangesAsync();

        LoggingExtensions.LogCreated(_logger, nameof(HumidityDto));

        return humidityDto;
    }

    /// <summary>
    /// Deletes a HumidityDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="HumidityDto"/> object if deleted; otherwise, null.</returns>
    public async Task<HumidityDto?> DeleteHumidityDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogDeletingById(_logger, nameof(HumidityDto));

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

        LoggingExtensions.LogDeletedById(_logger, nameof(HumidityDto));

        return humidityDto;
    }

    /// <summary>
    /// Deletes a HumidityDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="HumidityDto"/> object if deleted; otherwise, null.</returns>
    public async Task<HumidityDto?> DeleteHumidityDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogDeletingByTimestamp(_logger, nameof(HumidityDto));

        Humidity? humidity = await _humidityRepository.DeleteHumidityByTimestampAsync(timestamp);

        if (humidity == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Humidity));
            return null;
        }

        HumidityDto humidityDto = HumidityMapper.GetHumidityDtoFromHumidity(humidity);

        await _humidityRepository.SaveChangesAsync();

        LoggingExtensions.LogDeletedByTimestamp(_logger, nameof(HumidityDto));

        return humidityDto;
    }
}
