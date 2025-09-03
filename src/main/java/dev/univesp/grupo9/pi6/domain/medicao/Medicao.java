package dev.univesp.grupo9.pi6.domain.medicao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Table(name = "medicao")
@Setter
@Getter
@NoArgsConstructor
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private Long sensorId;

    // MySQL: POINT com SRID 4326 (WGS84) - Hibernate 6+
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point local;

    @Column(nullable = false)
    private Double noiseDb;
}
