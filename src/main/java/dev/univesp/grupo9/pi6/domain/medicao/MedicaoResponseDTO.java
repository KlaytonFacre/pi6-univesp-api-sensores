package dev.univesp.grupo9.pi6.domain.medicao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MedicaoResponseDTO {

    private Long id;
    private LocalDateTime timestamp;
    private Long sensorId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double noiseDb;
}
