package api.arduinothermohygrometer.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "humidities")
@Getter
@Setter
public class Humidity {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "air_humidity", nullable = false, precision = 5, scale = 2)
    private Double airHumidity;

    @Builder
    public Humidity(LocalDateTime registeredAt, Double airHumidity) {
        this.registeredAt = registeredAt;
        this.airHumidity = airHumidity;
    }

    protected Humidity() {
    }
}
