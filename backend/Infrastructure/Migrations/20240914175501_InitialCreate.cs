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
                Id = table.Column<Guid>(type: "uniqueidentifier", nullable: false, defaultValueSql: "NEWID()"),
                BatteryId = table.Column<int>(type: "int", nullable: false)
                    .Annotation("SqlServer:Identity", "1, 1"),
                RegisteredAt = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false, defaultValueSql: "SYSDATETIMEOFFSET()"),
                BatteryStatus = table.Column<int>(type: "int", nullable: false),
                Version = table.Column<byte[]>(type: "rowversion", rowVersion: true, nullable: true)
            },
            constraints: table =>
            {
                table.PrimaryKey("PK_Batteries", x => x.Id)
                    .Annotation("SqlServer:Clustered", false);
                table.CheckConstraint("CK_BatteryStatus_GreaterThanOrEqualToZero", "BatteryStatus >= 0");
                table.CheckConstraint("CK_BatteryStatus_LessThanOrEqualToOneHundred", "BatteryStatus <= 100");
            });

        migrationBuilder.CreateTable(
            name: "Temperatures",
            columns: table => new
            {
                Id = table.Column<Guid>(type: "uniqueidentifier", nullable: false, defaultValueSql: "NEWID()"),
                TemperatureId = table.Column<int>(type: "int", nullable: false)
                    .Annotation("SqlServer:Identity", "1, 1"),
                RegisteredAt = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false, defaultValueSql: "SYSDATETIMEOFFSET()"),
                Temp = table.Column<decimal>(type: "decimal(5,2)", precision: 5, scale: 2, nullable: false),
                AirHumidity = table.Column<decimal>(type: "decimal(4,2)", precision: 4, scale: 2, nullable: false),
                Version = table.Column<byte[]>(type: "rowversion", rowVersion: true, nullable: true)
            },
            constraints: table =>
            {
                table.PrimaryKey("PK_Temperatures", x => x.Id)
                    .Annotation("SqlServer:Clustered", false);
                table.CheckConstraint("CK_AirHumidity_GreaterThanOrEqualToTwenty", "AirHumidity >= 20");
                table.CheckConstraint("CK_AirHumidity_LessThanOrEqualToNinety", "AirHumidity <= 90");
                table.CheckConstraint("CK_Temp_GreaterThanOrEqualToNegativeFiftyFive", "Temp >= -55");
                table.CheckConstraint("CK_Temp_LessThanOrEqualToOneHundredAndTwentyFive", "Temp <= 125");
            });

        migrationBuilder.CreateIndex(
            name: "IX_Batteries_BatteryId",
            table: "Batteries",
            column: "BatteryId",
            unique: true)
                .Annotation("SqlServer:Clustered", true);

        migrationBuilder.CreateIndex(
            name: "IX_Batteries_RegisteredAt",
            table: "Batteries",
            column: "RegisteredAt",
            unique: true);

        migrationBuilder.CreateIndex(
            name: "IX_Temperatures_RegisteredAt",
            table: "Temperatures",
            column: "RegisteredAt",
            unique: true);

        migrationBuilder.CreateIndex(
            name: "IX_Temperatures_TemperatureId",
            table: "Temperatures",
            column: "TemperatureId",
            unique: true)
                .Annotation("SqlServer:Clustered", true);
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
