using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ArduinoThermoHygrometer.Infrastructure.Migrations;

/// <inheritdoc />
public partial class RegisteredAtIndexInDescendingOrder : Migration
{
    /// <inheritdoc />
    protected override void Up(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropIndex(
            name: "IX_Temperatures_RegisteredAt",
            table: "Temperatures");

        migrationBuilder.DropIndex(
            name: "IX_Humidities_RegisteredAt",
            table: "Humidities");

        migrationBuilder.DropIndex(
            name: "IX_Batteries_RegisteredAt",
            table: "Batteries");

        migrationBuilder.CreateIndex(
            name: "IX_Temperatures_RegisteredAt",
            table: "Temperatures",
            column: "RegisteredAt",
            unique: true,
            descending: Array.Empty<bool>())
                .Annotation("SqlServer:Clustered", true);

        migrationBuilder.CreateIndex(
            name: "IX_Humidities_RegisteredAt",
            table: "Humidities",
            column: "RegisteredAt",
            unique: true,
            descending: Array.Empty<bool>())
                .Annotation("SqlServer:Clustered", true);

        migrationBuilder.CreateIndex(
            name: "IX_Batteries_RegisteredAt",
            table: "Batteries",
            column: "RegisteredAt",
            unique: true,
            descending: Array.Empty<bool>())
                .Annotation("SqlServer:Clustered", true);
    }

    /// <inheritdoc />
    protected override void Down(MigrationBuilder migrationBuilder)
    {
        migrationBuilder.DropIndex(
            name: "IX_Temperatures_RegisteredAt",
            table: "Temperatures");

        migrationBuilder.DropIndex(
            name: "IX_Humidities_RegisteredAt",
            table: "Humidities");

        migrationBuilder.DropIndex(
            name: "IX_Batteries_RegisteredAt",
            table: "Batteries");

        migrationBuilder.CreateIndex(
            name: "IX_Temperatures_RegisteredAt",
            table: "Temperatures",
            column: "RegisteredAt",
            unique: true)
                .Annotation("SqlServer:Clustered", true);

        migrationBuilder.CreateIndex(
            name: "IX_Humidities_RegisteredAt",
            table: "Humidities",
            column: "RegisteredAt",
            unique: true)
                .Annotation("SqlServer:Clustered", true);

        migrationBuilder.CreateIndex(
            name: "IX_Batteries_RegisteredAt",
            table: "Batteries",
            column: "RegisteredAt",
            unique: true)
                .Annotation("SqlServer:Clustered", true);
    }
}
