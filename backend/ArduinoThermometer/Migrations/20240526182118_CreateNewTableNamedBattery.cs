using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermometer.API.Migrations;

/// <inheritdoc />
public partial class CreateNewTableNamedBattery : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.CreateTable(name: "Batteries", columns: table => new
        {
            id = table.Column<int>(type: "int", nullable: false)
                    .Annotation("SqlServer:Identity", "1, 1"),
            temperature_id = table.Column<int>(type: "int", nullable: false),
            battery_guid = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
            date = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false),
            time = table.Column<TimeSpan>(type: "time", nullable: false),
            battery_status = table.Column<string>(type: "nvarchar(10)", nullable: false)
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
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropTable(name: "Batteries");
    }
}
