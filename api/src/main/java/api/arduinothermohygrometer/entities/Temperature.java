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
@Table(name = "temperatures")
@Getter
@Setter
public class Temperature {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "temp", nullable = false, precision = 6, scale = 2)
    private Double temp;

    @Builder
    public Temperature(LocalDateTime registeredAt, Double temp) {
        this.registeredAt = registeredAt;
        this.temp = temp;
    }

    protected Temperature() {
    }
}
