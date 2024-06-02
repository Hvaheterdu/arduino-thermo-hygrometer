using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class AddDefaultValueForDateBecauseItWasRemovedByAccident : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)");

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            defaultValue: false);

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            defaultValue: false);
    }
}
