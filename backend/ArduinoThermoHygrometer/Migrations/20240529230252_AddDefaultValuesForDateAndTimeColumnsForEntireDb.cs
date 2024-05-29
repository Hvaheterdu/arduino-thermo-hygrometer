using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class AddDefaultValuesForDateAndTimeColumnsForEntireDb : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            defaultValue: DateTimeOffset.Now.Date);

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Temperatures",
            defaultValue: DateTimeOffset.Now.TimeOfDay);

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            defaultValue: DateTimeOffset.Now.Date);

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Batteries",
            defaultValue: DateTimeOffset.Now.TimeOfDay);
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            defaultValue: false);

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Temperatures",
            defaultValue: false);

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            defaultValue: false);

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Batteries",
            defaultValue: false);
    }
}
