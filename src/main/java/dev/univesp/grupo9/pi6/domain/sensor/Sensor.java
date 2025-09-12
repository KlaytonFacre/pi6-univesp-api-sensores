package dev.univesp.grupo9.pi6.domain.sensor;

import dev.univesp.grupo9.pi6.domain.AbstractBaseEntity;
import dev.univesp.grupo9.pi6.domain.owner.Owner;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "sensors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_owner_code", columnNames = {"owner_id", "code"})
        }
)
@Getter @Setter @NoArgsConstructor
public class Sensor extends AbstractBaseEntity {

    @NotBlank
    @Column(nullable = false, length = 20)
    private String name;

    // Codigo simples para melhor identificacao pratica do sensor
    @NotBlank
    @Column(nullable = false, length = 10)
    private String code;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(length = 60)
    private String serialNumber;

    private Instant lastSeenAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private SensorStatus status = SensorStatus.ACTIVE;
}
