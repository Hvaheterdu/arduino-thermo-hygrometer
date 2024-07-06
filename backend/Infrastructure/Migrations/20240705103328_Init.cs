﻿using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Infrastructure.Migrations;

/// <inheritdoc />
public partial class Init : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.CreateTable(name: "Temperatures", columns: table => new
        {
            id = table.Column<int>(type: "int", nullable: false)
                .Annotation("SqlServer:Identity", "1, 1"),
            temperature_guid = table.Column<Guid>(type: "uniqueidentifier", nullable: false, defaultValueSql: "NEWID()"),
            date = table.Column<DateOnly>(type: "date", nullable: false, defaultValueSql: "CAST(SYSDATETIME() AS DATE)"),
            time = table.Column<TimeOnly>(type: "time", nullable: false, defaultValueSql: "CAST(SYSDATETIME() AS TIME)"),
            temp = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false),
            air_humidity = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false)
        },
        constraints: table => table.PrimaryKey("PK_Temperatures", x => x.id));

        migrationBuilder.CreateTable(name: "Batteries", columns: table => new
        {
            id = table.Column<int>(type: "int", nullable: false)
                .Annotation("SqlServer:Identity", "1, 1"),
            temperature_id = table.Column<int>(type: "int", nullable: false),
            battery_guid = table.Column<Guid>(type: "uniqueidentifier", nullable: false, defaultValueSql: "NEWID()"),
            date = table.Column<DateOnly>(type: "date", nullable: false, defaultValueSql: "CAST(SYSDATETIME() AS DATE)"),
            time = table.Column<TimeOnly>(type: "time", nullable: false, defaultValueSql: "CAST(SYSDATETIME() AS TIME)"),
            battery_status = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false)
        },
        constraints: table =>
        {
            table.PrimaryKey("PK_Batteries", x => x.id);
            table.ForeignKey(
                name: "FK_Batteries_Temperatures_temperature_id",
                column: x => x.temperature_id,
                principalTable: "Temperatures",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        });

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
        migrationBuilder.DropTable(name: "Batteries");

        migrationBuilder.DropTable(name: "Temperatures");
    }
}
