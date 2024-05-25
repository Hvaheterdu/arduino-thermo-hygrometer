using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermometer.API.Migrations;

/// <inheritdoc />
public partial class AddColumnNamedHumidity : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.AddColumn<string>(
            name: "humidity",
            table: "Temperatures",
            type: "nvarchar(10)",
            nullable: false);
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropColumn(
            name: "Humidity",
            table: "Temperatures");
    }
}
