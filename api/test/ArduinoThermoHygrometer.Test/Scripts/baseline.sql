IF OBJECT_ID(N'[__EFMigrationsHistory]') IS NULL
BEGIN
    CREATE TABLE [__EFMigrationsHistory] (
        [MigrationId] nvarchar(150) NOT NULL,
        [ProductVersion] nvarchar(32) NOT NULL,
        CONSTRAINT [PK___EFMigrationsHistory] PRIMARY KEY ([MigrationId])
    );
END;
GO

BEGIN TRANSACTION;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20240915224505_InitialCreate'
)
BEGIN
    CREATE TABLE [Batteries] (
        [Id] uniqueidentifier NOT NULL DEFAULT (NEWID()),
        [RegisteredAt] datetimeoffset NOT NULL DEFAULT (SYSDATETIMEOFFSET()),
        [BatteryStatus] int NOT NULL,
        [Version] rowversion NULL,
        CONSTRAINT [PK_Batteries] PRIMARY KEY NONCLUSTERED ([Id]),
        CONSTRAINT [CK_BatteryStatus_GreaterThanOrEqualToZero] CHECK (BatteryStatus >= 0),
        CONSTRAINT [CK_BatteryStatus_LessThanOrEqualToOneHundred] CHECK (BatteryStatus <= 100)
    );
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20240915224505_InitialCreate'
)
BEGIN
    CREATE TABLE [Humidities] (
        [Id] uniqueidentifier NOT NULL DEFAULT (NEWID()),
        [RegisteredAt] datetimeoffset NOT NULL DEFAULT (SYSDATETIMEOFFSET()),
        [AirHumidity] decimal(4,2) NOT NULL,
        [Version] rowversion NULL,
        CONSTRAINT [PK_Humidities] PRIMARY KEY NONCLUSTERED ([Id]),
        CONSTRAINT [CK_AirHumidity_GreaterThanOrEqualToTwenty] CHECK (AirHumidity >= 20),
        CONSTRAINT [CK_AirHumidity_LessThanOrEqualToNinety] CHECK (AirHumidity <= 90)
    );
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20240915224505_InitialCreate'
)
BEGIN
    CREATE TABLE [Temperatures] (
        [Id] uniqueidentifier NOT NULL DEFAULT (NEWID()),
        [RegisteredAt] datetimeoffset NOT NULL DEFAULT (SYSDATETIMEOFFSET()),
        [Temp] decimal(5,2) NOT NULL,
        [Version] rowversion NULL,
        CONSTRAINT [PK_Temperatures] PRIMARY KEY NONCLUSTERED ([Id]),
        CONSTRAINT [CK_Temp_GreaterThanOrEqualToNegativeFiftyFive] CHECK (Temp >= -55),
        CONSTRAINT [CK_Temp_LessThanOrEqualToOneHundredAndTwentyFive] CHECK (Temp <= 125)
    );
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20240915224505_InitialCreate'
)
BEGIN
    CREATE UNIQUE CLUSTERED INDEX [IX_Batteries_RegisteredAt] ON [Batteries] ([RegisteredAt]);
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20240915224505_InitialCreate'
)
BEGIN
    CREATE UNIQUE CLUSTERED INDEX [IX_Humidities_RegisteredAt] ON [Humidities] ([RegisteredAt]);
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20240915224505_InitialCreate'
)
BEGIN
    CREATE UNIQUE CLUSTERED INDEX [IX_Temperatures_RegisteredAt] ON [Temperatures] ([RegisteredAt]);
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20240915224505_InitialCreate'
)
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20240915224505_InitialCreate', N'8.0.20');
END;
GO

COMMIT;
GO

BEGIN TRANSACTION;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023155218_RegisteredAtIndexInDescendingOrder'
)
BEGIN
    DROP INDEX [IX_Temperatures_RegisteredAt] ON [Temperatures];
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023155218_RegisteredAtIndexInDescendingOrder'
)
BEGIN
    DROP INDEX [IX_Humidities_RegisteredAt] ON [Humidities];
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023155218_RegisteredAtIndexInDescendingOrder'
)
BEGIN
    DROP INDEX [IX_Batteries_RegisteredAt] ON [Batteries];
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023155218_RegisteredAtIndexInDescendingOrder'
)
BEGIN
    CREATE UNIQUE CLUSTERED INDEX [IX_Temperatures_RegisteredAt] ON [Temperatures] ([RegisteredAt] DESC);
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023155218_RegisteredAtIndexInDescendingOrder'
)
BEGIN
    CREATE UNIQUE CLUSTERED INDEX [IX_Humidities_RegisteredAt] ON [Humidities] ([RegisteredAt] DESC);
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023155218_RegisteredAtIndexInDescendingOrder'
)
BEGIN
    CREATE UNIQUE CLUSTERED INDEX [IX_Batteries_RegisteredAt] ON [Batteries] ([RegisteredAt] DESC);
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023155218_RegisteredAtIndexInDescendingOrder'
)
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20241023155218_RegisteredAtIndexInDescendingOrder', N'8.0.20');
END;
GO

COMMIT;
GO

BEGIN TRANSACTION;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023215428_VersionRemoval'
)
BEGIN
    DECLARE @var0 sysname;
    SELECT @var0 = [d].[name]
    FROM [sys].[default_constraints] [d]
    INNER JOIN [sys].[columns] [c] ON [d].[parent_column_id] = [c].[column_id] AND [d].[parent_object_id] = [c].[object_id]
    WHERE ([d].[parent_object_id] = OBJECT_ID(N'[Temperatures]') AND [c].[name] = N'Version');
    IF @var0 IS NOT NULL EXEC(N'ALTER TABLE [Temperatures] DROP CONSTRAINT [' + @var0 + '];');
    ALTER TABLE [Temperatures] DROP COLUMN [Version];
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023215428_VersionRemoval'
)
BEGIN
    DECLARE @var1 sysname;
    SELECT @var1 = [d].[name]
    FROM [sys].[default_constraints] [d]
    INNER JOIN [sys].[columns] [c] ON [d].[parent_column_id] = [c].[column_id] AND [d].[parent_object_id] = [c].[object_id]
    WHERE ([d].[parent_object_id] = OBJECT_ID(N'[Humidities]') AND [c].[name] = N'Version');
    IF @var1 IS NOT NULL EXEC(N'ALTER TABLE [Humidities] DROP CONSTRAINT [' + @var1 + '];');
    ALTER TABLE [Humidities] DROP COLUMN [Version];
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023215428_VersionRemoval'
)
BEGIN
    DECLARE @var2 sysname;
    SELECT @var2 = [d].[name]
    FROM [sys].[default_constraints] [d]
    INNER JOIN [sys].[columns] [c] ON [d].[parent_column_id] = [c].[column_id] AND [d].[parent_object_id] = [c].[object_id]
    WHERE ([d].[parent_object_id] = OBJECT_ID(N'[Batteries]') AND [c].[name] = N'Version');
    IF @var2 IS NOT NULL EXEC(N'ALTER TABLE [Batteries] DROP CONSTRAINT [' + @var2 + '];');
    ALTER TABLE [Batteries] DROP COLUMN [Version];
END;
GO

IF NOT EXISTS (
    SELECT * FROM [__EFMigrationsHistory]
    WHERE [MigrationId] = N'20241023215428_VersionRemoval'
)
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20241023215428_VersionRemoval', N'8.0.20');
END;
GO

COMMIT;
GO

