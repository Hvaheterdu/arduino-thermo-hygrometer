﻿using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;

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

    public Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<TemperatureDto?> GetTemperatureDtoByDateAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<IEnumerable<TemperatureDto?>> GetTemperatureDtosByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate)
    {
        throw new NotImplementedException();
    }

    public Task<TemperatureDto?> GetTemperatureDtoByTimestampAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<IEnumerable<TemperatureDto?>> GetTemperatureDtosByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp)
    {
        throw new NotImplementedException();
    }

    public Task<TemperatureDto?> AddTemperatureDtoAsync(TemperatureDto temperatureDto)
    {
        throw new NotImplementedException();
    }

    public TemperatureDto? RemoveTemperatureDto(TemperatureDto temperatureDto)
    {
        throw new NotImplementedException();
    }
}
