using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Infrastructure.Migrations;

/// <inheritdoc />
public partial class InitialCreate : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.CreateTable(
            name: "Batteries",
            columns: table => new
            {
                Id = table.Column<int>(type: "int", nullable: false)
                    .Annotation("SqlServer:Identity", "1, 1"),
                BatteryGuid = table.Column<Guid>(type: "uniqueidentifier", nullable: false, defaultValueSql: "NEWID()"),
                CreatedAt = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false, defaultValueSql: "SYSDATETIMEOFFSET()"),
                BatteryStatus = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false)
            },
            constraints: table => table.PrimaryKey("PK_Batteries", x => x.Id));

        migrationBuilder.CreateTable(
            name: "Temperatures",
            columns: table => new
            {
                Id = table.Column<int>(type: "int", nullable: false)
                    .Annotation("SqlServer:Identity", "1, 1"),
                TemperatureGuid = table.Column<Guid>(type: "uniqueidentifier", nullable: false, defaultValueSql: "NEWID()"),
                CreatedAt = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false, defaultValueSql: "SYSDATETIMEOFFSET()"),
                Temp = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false),
                AirHumidity = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false)
            },
            constraints: table => table.PrimaryKey("PK_Temperatures", x => x.Id));

        migrationBuilder.CreateIndex(
            name: "IX_Batteries_BatteryGuid",
            table: "Batteries",
            column: "BatteryGuid",
            unique: true);

        migrationBuilder.CreateIndex(
            name: "IX_Temperatures_TemperatureGuid",
            table: "Temperatures",
            column: "TemperatureGuid",
            unique: true);
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropTable(name: "Batteries");

        migrationBuilder.DropTable(name: "Temperatures");
    }
}
