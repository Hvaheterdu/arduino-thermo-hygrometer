using Microsoft.EntityFrameworkCore;

namespace ArduinoThermometer.Database;
public class ArduinoThermometerDbContext : DbContext
{
    public ArduinoThermometerDbContext(DbContextOptions<ArduinoThermometerDbContext> options) : base(options)
    {
    }
}
