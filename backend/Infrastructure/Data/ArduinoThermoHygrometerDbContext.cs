using ArduinoThermoHygrometer.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Infrastructure.Data;

public class ArduinoThermoHygrometerDbContext : DbContext
{
    public DbSet<Battery> Batteries { get; set; } = null!;
    public DbSet<Temperature> Temperatures { get; set; } = null!;

    public ArduinoThermoHygrometerDbContext(DbContextOptions<ArduinoThermoHygrometerDbContext> options) : base(options)
    {
    }

    /// <summary>
    /// Configures the model for the database context.
    /// </summary>
    /// <param name="modelBuilder">The builder being used to construct the model for this context.</param>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Battery>(entity =>
        {
            entity.ToTable("Batteries");

            entity.HasKey(b => b.Id);

            entity.Property(b => b.BatteryGuid)
                .HasColumnType("uniqueidentifier")
                .HasDefaultValueSql("NEWID()")
                .IsRequired();

            entity.HasIndex(b => b.BatteryGuid)
                .IsUnique();

            entity.Property(b => b.CreatedAt)
                .HasColumnType("datetimeoffset")
                .HasDefaultValueSql("SYSDATETIMEOFFSET()")
                .IsRequired();

            entity.Property(b => b.BatteryStatus)
                .HasColumnType("string")
                .HasMaxLength(10)
                .IsRequired();
        });

        modelBuilder.Entity<Temperature>(entity =>
        {
            entity.ToTable("Temperatures");

            entity.HasKey(t => t.Id);

            entity.Property(t => t.TemperatureGuid)
                .HasColumnType("uniqueidentifier")
                .HasDefaultValueSql("NEWID()")
                .IsRequired();

            entity.HasIndex(t => t.TemperatureGuid)
                .IsUnique();

            entity.Property(t => t.CreatedAt)
                .HasColumnType("datetimeoffset")
                .HasDefaultValueSql("SYSDATETIMEOFFSET()")
                .IsRequired();

            entity.Property(t => t.Temp)
                .HasColumnType("string")
                .HasMaxLength(10)
                .IsRequired();

            entity.Property(t => t.AirHumidity)
                .HasColumnType("string")
                .HasMaxLength(10)
                .IsRequired();
        });

        base.OnModelCreating(modelBuilder);
    }
}
