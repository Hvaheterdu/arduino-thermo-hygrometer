using ArduinoThermometer.Data.Models;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermometer.Data;
public class ArduinoThermometerDbContext : DbContext
{
    public ArduinoThermometerDbContext(DbContextOptions<ArduinoThermometerDbContext> options) : base(options)
    {
    }

    public DbSet<Temperature> Temperatures { get; set; } = null!;

    /// <summary>
    /// Construct database table from model.
    /// </summary>
    /// <param name="modelBuilder"></param>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Temperature>().HasKey(t => t.TemperatureId);
    }
}
