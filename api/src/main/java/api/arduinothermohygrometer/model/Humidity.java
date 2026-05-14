package api.arduinothermohygrometer.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "humidities")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Humidity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "air_humidity", nullable = false, precision = 5, scale = 2)
    private Double airHumidity;

    public Humidity(LocalDateTime registeredAt, Double airHumidity) {
        this.registeredAt = registeredAt;
        this.airHumidity = airHumidity;
    }
}
