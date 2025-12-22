CREATE TABLE IF NOT EXISTS batteries
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    registered_at TIMESTAMP NOT NULL DEFAULT now(),
    battery_status INTEGER NOT NULL,
    CHECK (battery_status BETWEEN 0 AND 100)
);
CREATE INDEX IDX_batteries_registered_at ON batteries(registered_at);

CREATE TABLE IF NOT EXISTS humidities
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    registered_at TIMESTAMP NOT NULL DEFAULT now(),
    air_humidity NUMERIC(5, 2) NOT NULL,
    CHECK (air_humidity BETWEEN 20 AND 90)
);
CREATE INDEX IF NOT EXISTS IDX_humidities_registered_at ON humidities(registered_at);

CREATE TABLE IF NOT EXISTS temperatures
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    registered_at TIMESTAMP NOT NULL DEFAULT now(),
    temp NUMERIC(6, 2) NOT NULL,
    CHECK (temp BETWEEN -55 AND 125)
);
CREATE INDEX IF NOT EXISTS IDX_temperatures_registered_at ON temperatures(registered_at);