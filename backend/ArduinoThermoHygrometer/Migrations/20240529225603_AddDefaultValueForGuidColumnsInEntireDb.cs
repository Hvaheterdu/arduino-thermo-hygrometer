using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class AddDefaultValueForGuidColumnsInEntireDb : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<Guid>(
            name: "temperature_guid",
            table: "Temperatures",
            defaultValue: Guid.NewGuid());

        migrationBuilder.AlterColumn<Guid>(
            name: "battery_guid",
            table: "Batteries",
            defaultValue: Guid.NewGuid());
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<Guid>(
            name: "temperature_guid",
            table: "Temperatures",
            defaultValue: false);

        migrationBuilder.AlterColumn<Guid>(
            name: "battery_guid",
            table: "Batteries",
            defaultValue: false);
    }
}
