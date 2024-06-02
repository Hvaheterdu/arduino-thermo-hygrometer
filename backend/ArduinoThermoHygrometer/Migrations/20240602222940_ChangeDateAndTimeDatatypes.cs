using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Migrations;

/// <inheritdoc />
public partial class ChangeDateAndTimeDatatypes : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateOnly>(
            name: "date",
            table: "Temperatures",
            type: "date",
            nullable: false,
            oldClrType: typeof(DateTimeOffset),
            oldType: "datetimeoffset");

        migrationBuilder.AlterColumn<DateOnly>(
            name: "date",
            table: "Batteries",
            type: "date",
            nullable: false,
            oldClrType: typeof(DateTimeOffset),
            oldType: "datetimeoffset");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Temperatures",
            type: "datetimeoffset",
            nullable: false,
            oldClrType: typeof(DateOnly),
            oldType: "date");

        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "date",
            table: "Batteries",
            type: "datetimeoffset",
            nullable: false,
            oldClrType: typeof(DateOnly),
            oldType: "date");
    }
}
