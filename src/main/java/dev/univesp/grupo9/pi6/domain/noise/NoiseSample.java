package dev.univesp.grupo9.pi6.domain.noise;

import dev.univesp.grupo9.pi6.domain.AbstractBaseEntity;
import dev.univesp.grupo9.pi6.domain.sensor.Sensor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "samples",
        indexes = {
                @Index(name = "ix_noise_sensor_time", columnList = "sensor_id,captured_at")
        }
)
@Getter @Setter @NoArgsConstructor
public class NoiseSample extends AbstractBaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "captured_at", nullable = false)
    private Instant capturedAt;

    @NotNull
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point local;

    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal laeq;

    @Column(precision = 8, scale = 2)
    private BigDecimal lmax;

    @Column(precision = 8, scale = 2)
    private BigDecimal lmin;

    @Column(nullable = false)
    private int windowSeconds;
}