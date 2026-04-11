package api.arduinothermohygrometer.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "batteries")
@Getter
@Setter
public class Battery {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "battery_status", nullable = false)
    private int batteryStatus;

    public Battery(LocalDateTime registeredAt, int batteryStatus) {
        this.registeredAt = registeredAt;
        this.batteryStatus = batteryStatus;
    }

    protected Battery() {
    }
}
