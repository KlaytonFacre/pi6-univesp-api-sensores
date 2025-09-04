package dev.univesp.grupo9.pi6.domain.noise;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NoiseSampleResponseDTO(
        Long id,
        LocalDateTime createdAt,
        Long sensorId,
        BigDecimal latitude,
        BigDecimal longitude,
        Double noiseDb
) {
}
