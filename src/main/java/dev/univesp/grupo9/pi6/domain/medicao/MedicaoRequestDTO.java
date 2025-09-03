package dev.univesp.grupo9.pi6.domain.medicao;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MedicaoRequestDTO(
        @NotNull(message = "O sensorId é obrigatório")
        Long sensorId,

        @NotNull(message = "A latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "Latitude mínima é -90.0")
        @DecimalMax(value = "90.0", message = "Latitude máxima é 90.0")
        BigDecimal latitude,

        @NotNull(message = "A longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "Longitude mínima é -180.0")
        @DecimalMax(value = "180.0", message = "Longitude máxima é 180.0")
        BigDecimal longitude,

        @NotNull(message = "O nível de ruído é obrigatório")
        @Positive(message = "O nível de ruído deve ser positivo")
        Double noiseDb
) {
}