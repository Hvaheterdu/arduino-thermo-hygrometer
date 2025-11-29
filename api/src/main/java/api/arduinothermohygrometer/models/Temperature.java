package api.arduinothermohygrometer.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "temperatures")
@Getter
@Setter
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotEmpty
    @NotNull
    @Pattern(regexp = "[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}")
    private UUID id;

    @NotEmpty
    @NotNull
    private LocalDateTime registeredAt;

    @NotEmpty
    @NotNull
    @DecimalMin("-55.00")
    @DecimalMax("125.00")
    @Pattern(regexp = "^[+-]?\\d+\\.\\d+$")
    private BigDecimal temp;

    @Builder
    public Temperature(BigDecimal temp) {
        this.id = UUID.randomUUID();
        this.registeredAt = LocalDateTime.now();
        this.temp = temp;
    }
}
