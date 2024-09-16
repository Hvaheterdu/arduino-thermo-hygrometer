using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Mappers;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Web.Repositories;

public class BatteryRepository : IBatteryRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public BatteryRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task<IEnumerable<Battery?>> GetAllBatteriesAsync()
    {
        IEnumerable<Battery?> batteries = await _dbContext.Batteries.ToListAsync();

        return batteries;
    }

    public async Task<Battery?> GetBatteryByIdAsync(Guid id)
    {
        Battery? battery = await _dbContext.Batteries.FindAsync(id);

        return battery;
    }

    public async Task<Battery?> GetBatteryByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        Battery? battery = await _dbContext.Batteries.FindAsync(dateTimeOffset);

        return battery;
    }

    public async Task<Battery?> AddBatteryAsync(BatteryDto batteryDto)
    {
        Battery? battery = BatteryMapper.GetBatteryFromBatteryDto(batteryDto);
        await _dbContext.AddAsync<Battery>(battery);

        return battery;
    }

    public async Task<Battery?> UpdateBatteryAsync(BatteryDto batteryDto)
    {
        Battery? battery = await _dbContext.Batteries.FindAsync(batteryDto.Id);
        battery = BatteryMapper.GetBatteryFromBatteryDto(batteryDto);

        return battery;
    }

    public Battery RemoveBattery(BatteryDto batteryDto)
    {
        Battery? battery = BatteryMapper.GetBatteryFromBatteryDto(batteryDto);
        _dbContext.Batteries.Remove(battery);

        return battery;
    }

    public async Task SaveChangesAsync()
    {
        await _dbContext.SaveChangesAsync();
    }
}
