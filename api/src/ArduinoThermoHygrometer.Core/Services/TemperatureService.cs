using System.Globalization;
using ArduinoThermoHygrometer.Core.Logging;
using ArduinoThermoHygrometer.Core.Mappers;
using ArduinoThermoHygrometer.Core.Repositories.Contracts;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using ArduinoThermoHygrometer.Domain.Entities;
using Microsoft.Extensions.Logging;

namespace ArduinoThermoHygrometer.Core.Services;

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
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="TemperatureDto"/> object if found; otherwise, null.</returns>
    public async Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id)
    {
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

        LoggingExtensions.LogDtoObjectToReturn(
            _logger, nameof(BatteryDto), temperatureDto.Id, temperatureDto.RegisteredAt.ToString(CultureInfo.InvariantCulture)
        );

        return temperatureDto;
    }

    /// <summary>
    /// Retrieves a TemperatureDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns a <see cref="TemperatureDto"/> object if found; otherwise, null.</returns>
    public async Task<TemperatureDto?> GetTemperatureDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        Temperature? temperature = await _temperatureRepository.GetTemperatureByTimestampAsync(timestamp);

        if (temperature == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Temperature));
            return null;
        }

        TemperatureDto temperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        LoggingExtensions.LogDtoObjectToReturn(
            _logger, nameof(BatteryDto), temperatureDto.Id, temperatureDto.RegisteredAt.ToString(CultureInfo.InvariantCulture)
        );

        return temperatureDto;
    }

    /// <summary>
    /// Retrieves a list of all TemperatureDto objects of a date asynchronously.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="TemperatureDto"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<TemperatureDto>?> GetTemperatureDtosByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Temperature> temperatures = await _temperatureRepository.GetTemperatureByDateAsync(dateTimeOffset);

        if (temperatures == null || !temperatures.Any())
        {
            LoggingExtensions.LogIsNullOrEmpty(_logger, nameof(Humidity), dateTimeOffset.Date.ToShortDateString());
            return null;
        }

        List<TemperatureDto> temperatureDtos = new();
        foreach (Temperature temperature in temperatures)
        {
            TemperatureDto temperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);
            temperatureDtos.Add(temperatureDto);

            LoggingExtensions.LogDtoObjectToReturn(
                _logger, nameof(BatteryDto), temperatureDto.Id, temperatureDto.RegisteredAt.Date.ToShortDateString()
            );
        }

        return temperatureDtos;
    }

    /// <summary>
    /// Creates a TemperatureDto object asynchronously.
    /// </summary>
    /// <param name="temperatureDto">The <see cref="TemperatureDto"/> object to create.</param>
    /// <returns>Returns the <see cref="TemperatureDto"/> object if created; otherwise, null.</returns>
    public async Task<TemperatureDto> CreateTemperatureDtoAsync(TemperatureDto temperatureDto)
    {
        Temperature? temperature = TemperatureMapper.GetTemperatureFromTemperatureDto(temperatureDto);

        await _temperatureRepository.CreateTemperatureAsync(temperature);
        await _temperatureRepository.SaveChangesAsync();

        TemperatureDto createdTemperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        LoggingExtensions.LogDtoObjectToCreate(
            _logger, nameof(BatteryDto), createdTemperatureDto.Id, createdTemperatureDto.RegisteredAt.ToString(CultureInfo.InvariantCulture)
        );

        return createdTemperatureDto;
    }

    /// <summary>
    /// Deletes a TemperatureDto object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="TemperatureDto"/> object if deleted; otherwise, null.</returns>
    public async Task<TemperatureDto?> DeleteTemperatureDtoByIdAsync(Guid id)
    {
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

        LoggingExtensions.LogDtoObjectToDelete(
            _logger, nameof(BatteryDto), temperatureDto.Id, temperatureDto.RegisteredAt.ToString(CultureInfo.InvariantCulture)
        );

        return temperatureDto;
    }

    /// <summary>
    /// Deletes a TemperatureDto object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="TemperatureDto"/> object if deleted; otherwise, null.</returns>
    public async Task<TemperatureDto?> DeleteTemperatureDtoByTimestampAsync(DateTimeOffset timestamp)
    {
        Temperature? temperature = await _temperatureRepository.DeleteTemperatureByTimestampAsync(timestamp);

        if (temperature == null)
        {
            LoggingExtensions.LogIsNull(_logger, nameof(Temperature));
            return null;
        }

        TemperatureDto temperatureDto = TemperatureMapper.GetTemperatureDtoFromTemperature(temperature);

        await _temperatureRepository.SaveChangesAsync();

        LoggingExtensions.LogDtoObjectToDelete(
            _logger, nameof(BatteryDto), temperatureDto.Id, temperatureDto.RegisteredAt.ToString(CultureInfo.InvariantCulture)
        );

        return temperatureDto;
    }
}
