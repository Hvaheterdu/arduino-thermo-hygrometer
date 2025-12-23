package api.arduinothermohygrometer.entities;

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
@Table(name = "batteries")
@Getter
@Setter
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "battery_status", nullable = false)
    private int batteryStatus;

    @Builder
    public Battery(int batteryStatus) {
        this.id = UUID.randomUUID();
        this.registeredAt = LocalDateTime.now();
        this.batteryStatus = batteryStatus;
    }
}
