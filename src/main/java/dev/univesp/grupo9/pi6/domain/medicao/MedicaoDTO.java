package dev.univesp.grupo9.pi6.domain.medicao;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MedicaoDTO {

    @NotNull(message = "O sensorId é obrigatório")
    private Long sensorId;

    @NotNull(message = "A latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude mínima é -90.0")
    @DecimalMax(value = "90.0", message = "Latitude máxima é 90.0")
    private BigDecimal latitude;

    @NotNull(message = "A longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude mínima é -180.0")
    @DecimalMax(value = "180.0", message = "Longitude máxima é 180.0")
    private BigDecimal longitude;

    @NotNull(message = "O nível de ruído é obrigatório")
    @Positive(message = "O nível de ruído deve ser positivo")
    private Double noiseDb;
}