package api.arduinothermohygrometer.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotEmpty
    @NotNull
    @Pattern(regexp = "[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}")
    private UUID id;

    @NotEmpty
    @NotNull
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @NotEmpty
    @NotNull
    @Min(0)
    @Max(100)
    @Pattern(regexp = "^\\d+\\.\\d+$")
    @Column(name = "battery_status", nullable = false)
    private int batteryStatus;

    @Builder
    public Battery(int batteryStatus) {
        this.id = UUID.randomUUID();
        this.registeredAt = LocalDateTime.now();
        this.batteryStatus = batteryStatus;
    }
}
