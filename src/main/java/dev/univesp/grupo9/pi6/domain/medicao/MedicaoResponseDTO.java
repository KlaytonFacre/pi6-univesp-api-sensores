package dev.univesp.grupo9.pi6.domain.medicao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MedicaoResponseDTO(
        Long id,
        LocalDateTime timestamp,
        Long sensorId,
        BigDecimal latitude,
        BigDecimal longitude,
        Double noiseDb
) {
}
