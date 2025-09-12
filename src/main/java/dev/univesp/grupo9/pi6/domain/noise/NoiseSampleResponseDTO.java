package dev.univesp.grupo9.pi6.domain.noise;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record NoiseSampleResponseDTO(
        UUID samplePublicId,
        UUID sensorPublicId,
        Instant capturedAt,
        BigDecimal laeq,
        BigDecimal lmax,
        BigDecimal lmin,
        int windowSeconds,
        BigDecimal latitude,
        BigDecimal longitude) {
    public NoiseSampleResponseDTO(NoiseSample noise) {
        this(noise.getPublicId(),
                noise.getSensor().getPublicId(),
                noise.getCapturedAt(),
                noise.getLaeq(),
                noise.getLmax(),
                noise.getLmin(),
                noise.getWindowSeconds(),
                noise.getLocal() != null ? BigDecimal.valueOf(noise.getLocal().getY()) : null,
                noise.getLocal() != null ? BigDecimal.valueOf(noise.getLocal().getX()) : null);
    }

}
