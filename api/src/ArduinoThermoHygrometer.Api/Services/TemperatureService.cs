using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Mappers;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Api.Utilities;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using Microsoft.IdentityModel.Tokens;

namespace ArduinoThermoHygrometer.Api.Services;

public class TemperatureService : ITemperatureService
{
    private readonly ITemperatureRepository _temperatureRepository;
    private readonly ILogger<TemperatureService> _logger;

    public TemperatureService(ITemperatureRepository temperatureRepository, ILogger<TemperatureService> logger)
    {
        _temperatureRepository = temperatureRepository;
        _logger = logger;
    }

    /// <summary>
    /// Retrieves a TemperatureDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the temperature to retrieve.</param>
    /// <returns>Returns a <see cref="TemperatureDto"/> object if found; otherwise, null.</returns>
    public async Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogRetrievingDtoById(_logger, nameof(TemperatureDto));

        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Temperature? temperature = await _temperatureRepository.GetTemperatureByIdAsync(id);

        if (temperature == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Temperature));
            return null;
        }

        TemperatureDto temperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        LoggingExtensions.LogRetrievingDtoById(_logger, nameof(TemperatureDto));

        return temperatureDto;
    }

    /// <summary>
    /// Retrieves a TemperatureDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the temperature to retrieve.</param>
    /// <returns>Returns a <see cref="TemperatureDto"/> object if found; otherwise, null.</returns>
    public async Task<TemperatureDto?> GetTemperatureDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogRetrievingDtoByTimestamp(_logger, nameof(TemperatureDto));

        Temperature? temperature = await _temperatureRepository.GetTemperatureByTimestampAsync(timestamp);

        if (temperature == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Temperature));
            return null;
        }

        TemperatureDto temperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        LoggingExtensions.LogRetrievedDtoByTimestamp(_logger, nameof(TemperatureDto));

        return temperatureDto;
    }

    /// <summary>
    /// Retrieves a list of all TemperatureDto objects of a date asynchronously.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the temperatures to retrieve.</param>
    /// <returns>Returns a list of <see cref="TemperatureDto"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<TemperatureDto>?> GetTemperatureDtosByDateAsync(DateTimeOffset dateTimeOffset)
    {
        LoggingExtensions.LogRetrievingDtoByDate(_logger, $"{nameof(TemperatureDto)}s");

        IEnumerable<Temperature> temperatures = await _temperatureRepository.GetTemperaturesByDateAsync(dateTimeOffset);

        string? capitaliseTemperatures = StringUtilities.CapitaliseFirstLetter(nameof(temperatures));
        if (temperatures.IsNullOrEmpty())
        {
            LoggingExtensions.LogIsNullOrEmpty(_logger, capitaliseTemperatures, dateTimeOffset.Date.ToShortDateString());
            return null;
        }

        List<TemperatureDto> temperatureDtos = new();
        foreach (Temperature temperature in temperatures)
        {
            temperatureDtos.Add(TemperatureMapper.GetTemperatureDtoFromTemperature(temperature));
        }

        LoggingExtensions.LogRetrievedDtoByDate(_logger, $"{nameof(TemperatureDto)}s");

        return temperatureDtos;
    }

    /// <summary>
    /// Creates a TemperatureDto object asynchronously.
    /// </summary>
    /// <param name="temperatureDto">The <see cref="TemperatureDto"/> object to add.</param>
    /// <returns>Returns the <see cref="TemperatureDto"/> object if created; otherwise, null.</returns>
    public async Task<TemperatureDto> CreateTemperatureDtoAsync(TemperatureDto temperatureDto)
    {
        LoggingExtensions.LogCreating(_logger, nameof(TemperatureDto));

        Temperature? temperature = TemperatureMapper.GetTemperatureFromTemperatureDto(temperatureDto);

        await _temperatureRepository.CreateTemperatureAsync(temperature);
        await _temperatureRepository.SaveChangesAsync();

        LoggingExtensions.LogCreated(_logger, nameof(TemperatureDto));

        return temperatureDto;
    }

    /// <summary>
    /// Deletes a TemperatureDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="TemperatureDto"/> object if deleted; otherwise, null.</returns>
    public async Task<TemperatureDto?> DeleteTemperatureDtoByIdAsync(Guid id)
    {
        LoggingExtensions.LogDeletingById(_logger, nameof(TemperatureDto));

        if (id == Guid.Empty)
        {
            LoggingExtensions.LogInvalidId(_logger, id);
            return null;
        }

        Temperature? temperature = await _temperatureRepository.DeleteTemperatureByIdAsync(id);

        if (temperature == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Temperature));
            return null;
        }

        TemperatureDto temperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        await _temperatureRepository.SaveChangesAsync();

        LoggingExtensions.LogDeletedById(_logger, nameof(TemperatureDto));

        return temperatureDto;
    }

    /// <summary>
    /// Deletes a TemperatureDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="TemperatureDto"/> object if deleted; otherwise, null.</returns>
    public async Task<TemperatureDto?> DeleteTemperatureDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        LoggingExtensions.LogDeletingByTimestamp(_logger, nameof(TemperatureDto));

        Temperature? temperature = await _temperatureRepository.DeleteTemperatureByTimestampAsync(timestamp);

        if (temperature == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Temperature));
            return null;
        }

        TemperatureDto temperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        await _temperatureRepository.SaveChangesAsync();

        LoggingExtensions.LogDeletedByTimestamp(_logger, nameof(TemperatureDto));

        return temperatureDto;
    }
}
