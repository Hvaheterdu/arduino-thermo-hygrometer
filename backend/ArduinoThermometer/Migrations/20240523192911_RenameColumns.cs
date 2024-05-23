using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermometer.API.Migrations;

/// <inheritdoc />
public partial class RenameColumns : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.RenameColumn(
            name: "TemperatureValue",
            table: "Temperatures",
            newName: "value");

        migrationBuilder.RenameColumn(
            name: "TemperatureReadingTime",
            table: "Temperatures",
            newName: "time");

        migrationBuilder.RenameColumn(
            name: "TemperatureReadingDate",
            table: "Temperatures",
            newName: "date");

        migrationBuilder.RenameColumn(
            name: "TemperatureId",
            table: "Temperatures",
            newName: "id");

        migrationBuilder.RenameColumn(
            name: "TemperatureGUID",
            table: "Temperatures",
            newName: "temperature_guid");
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.RenameColumn(
            name: "Value",
            table: "Temperatures",
            newName: "TemperatureValue");

        migrationBuilder.RenameColumn(
            name: "Time",
            table: "Temperatures",
            newName: "TemperatureReadingTime");

        migrationBuilder.RenameColumn(
            name: "Date",
            table: "Temperatures",
            newName: "TemperatureReadingDate");

        migrationBuilder.RenameColumn(
            name: "Id",
            table: "Temperatures",
            newName: "TemperatureId");

        migrationBuilder.RenameColumn(
            name: "temperature_guid",
            table: "Temperatures",
            newName: "TemperatureGUID");
    }
}
