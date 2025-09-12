package dev.univesp.grupo9.pi6.domain.sample;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record NoiseSampleRequestDTO(
        @NotNull Long sensorId,
        @NotNull BigDecimal latitude,
        @NotNull BigDecimal longitude,

        // acústica
        @NotNull @DecimalMin("0.0") @DecimalMax("200.0") BigDecimal laeq,
        BigDecimal lmax,
        BigDecimal lmin,

        // janela e horário
        Integer windowSeconds,     // default 1 se null
        Instant capturedAt         // default now se null
) {}
