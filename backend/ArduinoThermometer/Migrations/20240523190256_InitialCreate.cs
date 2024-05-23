using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermometer.API.Migrations;

/// <inheritdoc />
public partial class InitialCreate : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.CreateTable(name: "Temperatures", columns: table => new
        {
            TemperatureId = table.Column<int>(type: "int", nullable: false)
                    .Annotation("SqlServer:Identity", "1, 1"),
            TemperatureGUID = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
            TemperatureReadingDate = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false),
            TemperatureReadingTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false),
            TemperatureValue = table.Column<string>(type: "nvarchar(10)", nullable: false)
        },

        constraints: table => table.PrimaryKey("PK_Temperatures", x => x.TemperatureId));
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropTable(name: "Temperatures");
    }
}
