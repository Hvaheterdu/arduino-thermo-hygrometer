using ArduinoThermoHygrometer.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Infrastructure.Data;

public class ArduinoThermoHygrometerDbContext : DbContext
{
    public DbSet<Temperature> Temperatures { get; set; } = null!;
    public DbSet<Battery> Batteries { get; set; } = null!;

    public ArduinoThermoHygrometerDbContext(DbContextOptions<ArduinoThermoHygrometerDbContext> options) : base(options)
    {
    }

    /// <summary>
    /// Configures the model for the database context.
    /// </summary>
    /// <param name="modelBuilder">The model builder instance.</param>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Temperature>().HasKey(t => t.Id);
        modelBuilder.Entity<Temperature>()
            .HasIndex(t => t.TemperatureGuid)
            .IsUnique();

        modelBuilder.Entity<Battery>().HasKey(b => b.Id);
        modelBuilder.Entity<Battery>()
            .HasIndex(b => b.BatteryGuid)
            .IsUnique();
    }
}
