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
                BatteryStatus = table.Column<int>(type: "int", nullable: false),
                Version = table.Column<byte[]>(type: "rowversion", rowVersion: true, nullable: true)
            },
            constraints: table =>
            {
                table.PrimaryKey("PK_Batteries", x => x.Id);
                table.CheckConstraint("CK_BatteryStatus_LessThanOrEqualToOneHundred", "BatteryStatus <= 100");
                table.CheckConstraint("CK_BatteryStatus_NotNegative", "BatteryStatus >= 0");
            });

        migrationBuilder.CreateTable(
            name: "Temperatures",
            columns: table => new
            {
                Id = table.Column<int>(type: "int", nullable: false)
                    .Annotation("SqlServer:Identity", "1, 1"),
                TemperatureGuid = table.Column<Guid>(type: "uniqueidentifier", nullable: false, defaultValueSql: "NEWID()"),
                CreatedAt = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false, defaultValueSql: "SYSDATETIMEOFFSET()"),
                Temp = table.Column<decimal>(type: "decimal(5,2)", precision: 5, scale: 2, nullable: false),
                AirHumidity = table.Column<decimal>(type: "decimal(4,2)", precision: 4, scale: 2, nullable: false),
                Version = table.Column<byte[]>(type: "rowversion", rowVersion: true, nullable: true)
            },
            constraints: table =>
            {
                table.PrimaryKey("PK_Temperatures", x => x.Id);
                table.CheckConstraint("CK_AirHumidity_GreaterThanOrEqualToTwenty", "AirHumidity >= 20");
                table.CheckConstraint("CK_AirHumidity_LessThanOrEqualToNinety", "AirHumidity <= 90");
                table.CheckConstraint("CK_Temp_GreaterThanOrEqualToNegativeFiftyFive", "Temp >= -55");
                table.CheckConstraint("CK_Temp_LessThanOrEqualToOneHundredAndTwentyFive", "Temp <= 125");
            });

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
        migrationBuilder.DropTable(
            name: "Batteries");

        migrationBuilder.DropTable(
            name: "Temperatures");
    }
}
