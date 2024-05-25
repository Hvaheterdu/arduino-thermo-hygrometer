using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermometer.API.Migrations;

/// <inheritdoc />
public partial class ChangeDataTypeForTimeColumnFromDateTimeOffsetToTime : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<DateTimeOffset>(
            name: "time",
            table: "Temperatures",
            type: "time",
            nullable: false,
            oldClrType: typeof(DateTimeOffset),
            oldType: "datetimeoffset");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AlterColumn<TimeSpan>(
            name: "time",
            table: "Temperatures",
            type: "datetimeoffset",
            nullable: false,
            oldClrType: typeof(TimeSpan),
            oldType: "time");
    }
}
