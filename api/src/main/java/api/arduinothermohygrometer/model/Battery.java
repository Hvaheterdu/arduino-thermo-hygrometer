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
@Table(name = "batteries")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Battery {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "battery_status", nullable = false)
    private int batteryStatus;

    public Battery(final LocalDateTime registeredAt,
                   final int batteryStatus) {
        this.registeredAt = registeredAt;
        this.batteryStatus = batteryStatus;
    }
}
