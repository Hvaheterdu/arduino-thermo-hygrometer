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
    /// <exception cref="ArgumentNullException">Thrown if <paramref name="modelBuilder"/> is null.</exception>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        ArgumentNullException.ThrowIfNull(modelBuilder, nameof(modelBuilder));

        modelBuilder.Entity<Battery>(entity =>
        {
            entity.ToTable("Batteries", buildAction =>
            {
                buildAction.HasCheckConstraint(
                    name: "CK_BatteryStatus_GreaterThanOrEqualToZero",
                    sql: $"{nameof(Battery.BatteryStatus)} >= 0");

                buildAction.HasCheckConstraint(
                    name: "CK_BatteryStatus_LessThanOrEqualToOneHundred",
                    sql: $"{nameof(Battery.BatteryStatus)} <= 100");
            });

            entity.HasKey(b => b.Id)
                .IsClustered(clustered: false);

            entity.Property(b => b.Id)
                .HasColumnType("uniqueidentifier")
                .HasDefaultValueSql("NEWID()")
                .IsRequired();

            entity.Property(b => b.RegisteredAt)
                .HasColumnType("datetimeoffset")
                .HasDefaultValueSql("SYSDATETIMEOFFSET()")
                .IsRequired();

            entity.HasIndex(b => b.RegisteredAt)
                .IsClustered()
                .IsUnique();

            entity.Property(b => b.BatteryStatus)
                .HasColumnType("int")
                .IsRequired();

            entity.Property(b => b.Version)
                .HasColumnType("rowversion")
                .IsRowVersion();
        });

        modelBuilder.Entity<Temperature>(entity =>
        {
            entity.ToTable("Temperatures", buildAction =>
            {
                buildAction.HasCheckConstraint(
                    name: "CK_AirHumidity_GreaterThanOrEqualToTwenty",
                    sql: $"{nameof(Temperature.AirHumidity)} >= 20");

                buildAction.HasCheckConstraint(
                    name: "CK_AirHumidity_LessThanOrEqualToNinety",
                    sql: $"{nameof(Temperature.AirHumidity)} <= 90");

                buildAction.HasCheckConstraint(
                    name: "CK_Temp_GreaterThanOrEqualToNegativeFiftyFive",
                    sql: $"{nameof(Temperature.Temp)} >= -55");

                buildAction.HasCheckConstraint(
                    name: "CK_Temp_LessThanOrEqualToOneHundredAndTwentyFive",
                    sql: $"{nameof(Temperature.Temp)} <= 125");
            });

            entity.HasKey(t => t.Id)
                .IsClustered(clustered: false);

            entity.Property(t => t.Id)
                .HasColumnType("uniqueidentifier")
                .HasDefaultValueSql("NEWID()")
                .IsRequired();

            entity.Property(t => t.RegisteredAt)
                .HasColumnType("datetimeoffset")
                .HasDefaultValueSql("SYSDATETIMEOFFSET()")
                .IsRequired();

            entity.HasIndex(t => t.RegisteredAt)
                .IsClustered()
                .IsUnique();

            entity.Property(t => t.Temp)
                .HasColumnType("decimal")
                .HasPrecision(5, 2)
                .IsRequired();

            entity.Property(t => t.AirHumidity)
                .HasColumnType("decimal")
                .HasPrecision(4, 2)
                .IsRequired();

            entity.Property(t => t.Version)
                .HasColumnType("rowversion")
                .IsRowVersion();
        });

        base.OnModelCreating(modelBuilder);
    }
}
