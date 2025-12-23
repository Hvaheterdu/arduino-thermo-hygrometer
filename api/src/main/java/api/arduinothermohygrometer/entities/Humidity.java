package api.arduinothermohygrometer.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "air_humidity", nullable = false, precision = 5, scale = 2)
    private BigDecimal airHumidity;

    @Builder
    public Humidity(BigDecimal airHumidity) {
        this.id = UUID.randomUUID();
        this.registeredAt = LocalDateTime.now();
        this.airHumidity = airHumidity;
    }
}
