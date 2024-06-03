using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class AddUniqueConstraintsForGuidColumns : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.CreateIndex(
            name: "IX_Temperatures_TemperatureGuid",
            table: "Temperatures",
            column: "temperature_guid",
            unique: true);

        migrationBuilder.CreateIndex(
            name: "IX_Batteries_BatteryGuid",
            table: "Batteries",
            column: "battery_guid",
            unique: true);
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropIndex(
            name: "IX_Temperatures_TemperatureGuid",
            table: "Temperatures");

        migrationBuilder.DropIndex(
            name: "IX_Batteries_BatteryGuid",
            table: "Batteries");
    }
}
