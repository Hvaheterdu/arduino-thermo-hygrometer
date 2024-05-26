using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermometer.API.Migrations;

/// <inheritdoc />
public partial class ChangeColumnNameFromValueToTemperature : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.RenameColumn(
            name: "value",
            table: "Temperatures",
            newName: "temperature",
            schema: "dbo");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.RenameColumn(
            name: "temperature",
            table: "Temperatures",
            newName: "value",
            schema: "dbo");
    }
}
