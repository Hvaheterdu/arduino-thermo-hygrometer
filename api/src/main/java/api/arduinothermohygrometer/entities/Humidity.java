package api.arduinothermohygrometer.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotEmpty
    @NotNull
    @Pattern(regexp = "[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}")
    private UUID id;

    @NotEmpty
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "no_NO")
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @NotEmpty
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    @DecimalMin("20.00")
    @DecimalMax("90.00")
    @Pattern(regexp = "^\\d+\\.\\d+$")
    @Column(name = "air_humidity", nullable = false, precision = 5, scale = 2)
    private BigDecimal airHumidity;

    @Builder
    public Humidity(BigDecimal airHumidity) {
        this.id = UUID.randomUUID();
        this.registeredAt = LocalDateTime.now();
        this.airHumidity = airHumidity;
    }
}
