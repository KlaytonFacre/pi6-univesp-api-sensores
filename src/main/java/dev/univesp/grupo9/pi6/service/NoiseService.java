package dev.univesp.grupo9.pi6.service;

import dev.univesp.grupo9.pi6.domain.sensor.Sensor;
import dev.univesp.grupo9.pi6.domain.sensor.SensorRepository;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSample;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleRepository;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleRequestDTO;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoiseService {

    private final NoiseSampleRepository noiseSampleRepository;
    private final SensorRepository sensorRepository;

    // SRID 4326 (WGS84)
    private static final int SRID = 4326;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);

    @Transactional
    public NoiseSampleResponseDTO saveSample(NoiseSampleRequestDTO dto) {
        // 1) Resolver o sensor
        Sensor sensor = sensorRepository.findById(dto.sensorId())
                .orElseThrow(() -> new IllegalArgumentException("Sensor não encontrado: id=" + dto.sensorId()));

        // 2) Criar o Point (x = lon, y = lat)
        Point local = geometryFactory.createPoint(new Coordinate(
                dto.longitude().doubleValue(),
                dto.latitude().doubleValue()
        ));
        local.setSRID(SRID);

        // 3) Timestamp da medição (se não informado, usa agora)
        Instant capturedAt = dto.capturedAt() != null ? dto.capturedAt() : Instant.now();

        // 4) Janela de medição (default 1s se não vier)
        int windowSeconds = dto.windowSeconds() != null ? dto.windowSeconds() : 1;

        // 5) Montar entidade
        NoiseSample entity = new NoiseSample();
        entity.setSensor(sensor);
        entity.setCapturedAt(capturedAt);
        entity.setLocal(local);
        entity.setLaeq(dto.laeq());
        entity.setLmax(dto.lmax());
        entity.setLmin(dto.lmin());
        entity.setWindowSeconds(windowSeconds);

        // 6) Persistir
        NoiseSample saved = noiseSampleRepository.save(entity);

        // 7) DTO de resposta
        return new NoiseSampleResponseDTO(
                saved.getPublicId(),
                saved.getSensor().getPublicId(),
                saved.getCapturedAt(),
                saved.getLaeq(),
                saved.getLmax(),
                saved.getLmin(),
                saved.getWindowSeconds(),
                // latitude = Y; longitude = X
                BigDecimal.valueOf(saved.getLocal().getY()),
                BigDecimal.valueOf(saved.getLocal().getX())
        );
    }

    public NoiseSampleResponseDTO getSampleByPublicId(UUID publicId) {
        var noiseSample = noiseSampleRepository.findByPublicId(publicId);

        return noiseSample.map(NoiseSampleResponseDTO::new).orElse(null);
    }
}
