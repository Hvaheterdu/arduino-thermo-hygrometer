using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class RefactorDateAndTimeValues : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)");

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Temperatures",
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)");

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)");

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Batteries",
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            defaultValueSql: "SYSDATETIMEOFFSET()");

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Temperatures",
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)");

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            defaultValueSql: "SYSDATETIMEOFFSET()");

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Batteries",
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)");
    }
}
