﻿// <auto-generated />
using System;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

#nullable disable

namespace ArduinoThermoHygrometer.Infrastructure.Migrations
{
    [DbContext(typeof(ArduinoThermoHygrometerDbContext))]
    partial class ArduinoThermoHygrometerDbContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "8.0.6")
                .HasAnnotation("Relational:MaxIdentifierLength", 128);

            SqlServerModelBuilderExtensions.UseIdentityColumns(modelBuilder);

            modelBuilder.Entity("ArduinoThermoHygrometer.Domain.Entities.Battery", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<Guid>("BatteryGuid")
                        .HasColumnType("uniqueidentifier");

                    b.Property<string>("BatteryStatus")
                        .IsRequired()
                        .HasMaxLength(10)
                        .HasColumnType("nvarchar(10)");

                    b.Property<DateOnly>("CreatedDate")
                        .HasColumnType("date");

                    b.Property<TimeOnly>("CreatedTime")
                        .HasColumnType("time");

                    b.Property<int>("TemperatureId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.ToTable("Batteries");
                });

            modelBuilder.Entity("ArduinoThermoHygrometer.Domain.Entities.Temperature", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("AirHumidity")
                        .IsRequired()
                        .HasMaxLength(10)
                        .HasColumnType("nvarchar(10)");

                    b.Property<DateOnly>("CreatedDate")
                        .HasColumnType("date");

                    b.Property<TimeOnly>("CreatedTime")
                        .HasColumnType("time");

                    b.Property<string>("Temp")
                        .IsRequired()
                        .HasMaxLength(10)
                        .HasColumnType("nvarchar(10)");

                    b.Property<Guid>("TemperatureGuid")
                        .HasColumnType("uniqueidentifier");

                    b.HasKey("Id");

                    b.ToTable("Temperatures");
                });
#pragma warning restore 612, 618
        }
    }
}
