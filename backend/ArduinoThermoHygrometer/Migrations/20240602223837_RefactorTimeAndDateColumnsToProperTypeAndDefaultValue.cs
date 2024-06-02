using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class RefactorTimeAndDateColumnsToProperTypeAndDefaultValue : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateOnly>(
            name: "date",
            table: "Temperatures",
            type: "date",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)",
            oldClrType: typeof(DateTimeOffset),
            oldType: "datetimeoffset");

        migrationBuilder.AlterColumn<TimeOnly>(
            name: "time",
            table: "Temperatures",
            type: "time",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)",
            oldClrType: typeof(TimeSpan),
            oldType: "time");

        migrationBuilder.AlterColumn<DateOnly>(
            name: "date",
            table: "Batteries",
            type: "date",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)",
            oldClrType: typeof(DateTimeOffset),
            oldType: "datetimeoffset");

        migrationBuilder.AlterColumn<TimeOnly>(
            name: "time",
            table: "Batteries",
            type: "time",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)",
            oldClrType: typeof(TimeSpan),
            oldType: "time");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            type: "datetimeoffset",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)",
            oldClrType: typeof(DateOnly),
            oldType: "datetimeoffset");

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Temperatures",
            type: "time",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)",
            oldClrType: typeof(TimeOnly),
            oldType: "time");

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            type: "date",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS DATE)",
            oldClrType: typeof(DateOnly),
            oldType: "datetimeoffset");

        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Batteries",
            type: "time",
            nullable: false,
            defaultValueSql: "CAST(SYSDATETIME() AS TIME)",
            oldClrType: typeof(TimeOnly),
            oldType: "time");
    }
}
