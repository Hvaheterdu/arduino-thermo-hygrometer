using ArduinoThermometer.API.Models;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermometer.API.Data;

public class ArduinoThermometerDbContext : DbContext
{
    public ArduinoThermometerDbContext(DbContextOptions<ArduinoThermometerDbContext> options) : base(options)
    {
    }

    public DbSet<Temperature> Temperatures { get; set; } = null!;

    /// <summary>
    /// Construct database table from model with Id as key.
    /// </summary>
    /// <param name="modelBuilder"></param>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Temperature>().HasKey(t => t.Id);
    }
}
