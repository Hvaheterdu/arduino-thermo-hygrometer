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
    /// Is utilised for seeding the database with data.
    /// </summary>
    /// <param name="modelBuilder">The builder being used to construct the model for this context.</param>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);
    }
}
