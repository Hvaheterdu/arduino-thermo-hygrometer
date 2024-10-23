using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Infrastructure.Migrations;

/// <inheritdoc />
public partial class VersionRemoval : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropColumn(
            name: "Version",
            table: "Temperatures");

        migrationBuilder.DropColumn(
            name: "Version",
            table: "Humidities");

        migrationBuilder.DropColumn(
            name: "Version",
            table: "Batteries");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AddColumn<byte[]>(
            name: "Version",
            table: "Temperatures",
            type: "rowversion",
            rowVersion: true,
            nullable: true);

        migrationBuilder.AddColumn<byte[]>(
            name: "Version",
            table: "Humidities",
            type: "rowversion",
            rowVersion: true,
            nullable: true);

        migrationBuilder.AddColumn<byte[]>(
            name: "Version",
            table: "Batteries",
            type: "rowversion",
            rowVersion: true,
            nullable: true);
    }
}
