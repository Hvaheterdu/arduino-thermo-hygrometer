using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Infrastructure.Migrations;

/// <inheritdoc />
public partial class RowVersioningForTables : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AddColumn<byte[]>(
            name: "Version",
            table: "Batteries",
            type: "rowversion",
            rowVersion: true,
            nullable: true);

        migrationBuilder.AddColumn<byte[]>(
            name: "Version",
            table: "Temperatures",
            type: "rowversion",
            rowVersion: true,
            nullable: true);
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropColumn(
            name: "Version",
            table: "Batteries");

        migrationBuilder.DropColumn(
            name: "Version",
            table: "Temperatures");
    }
}
