using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace ArduinoThermoHygrometer.Web.Repositories;

public class HumidityRepository : IHumidityRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public HumidityRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public Task<IEnumerable<Humidity?>> GetAllHumiditiesAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Humidity?> GetHumidityByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity?> GetHumidityByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity?> AddHumidityAsync(HumidityDto humidityDto)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity?> UpdateHumidityAsync(HumidityDto humidityDto)
    {
        throw new NotImplementedException();
    }

    public Humidity? RemoveHumidity(HumidityDto humidityDto)
    {
        throw new NotImplementedException();
    }

    public Task SaveChangesAsync()
    {
        throw new NotImplementedException();
    }
}
