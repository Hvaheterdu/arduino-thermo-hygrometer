using ArduinoThermoHygrometer.Entities;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Data;

public class ArduinoThermoHygrometerDbContext : DbContext
{
    public DbSet<Temperature> Temperatures { get; set; } = null!;
    public DbSet<Battery> Batteries { get; set; } = null!;

    public ArduinoThermoHygrometerDbContext(DbContextOptions<ArduinoThermoHygrometerDbContext> options) : base(options)
    {
    }

    /// <summary>
    /// Construct database table from model with Id as key.
    /// </summary>
    /// <param name="modelBuilder"></param>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Temperature>().HasKey(t => t.Id);
        modelBuilder.Entity<Battery>().HasKey(b => b.Id);
    }
}
