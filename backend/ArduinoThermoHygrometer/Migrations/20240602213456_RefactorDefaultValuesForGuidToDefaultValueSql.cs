using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class RefactorDefaultValuesForGuidToDefaultValueSql : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<Guid>(
            name: "temperature_guid",
            table: "Temperatures",
            defaultValueSql: "NEWID()");

        migrationBuilder.AlterColumn<Guid>(
            name: "battery_guid",
            table: "Batteries",
            defaultValueSql: "NEWID()");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
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
}
