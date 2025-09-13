package dev.univesp.grupo9.pi6.service;

import dev.univesp.grupo9.pi6.domain.sample.NoiseSample;
import dev.univesp.grupo9.pi6.domain.sample.NoiseSampleRepository;
import dev.univesp.grupo9.pi6.domain.sample.NoiseSampleRequestDTO;
import dev.univesp.grupo9.pi6.domain.sample.NoiseSampleResponseDTO;
import dev.univesp.grupo9.pi6.domain.sensor.Sensor;
import dev.univesp.grupo9.pi6.domain.sensor.SensorRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class NoiseService {

    @Autowired
    private NoiseSampleRepository noiseSampleRepository;
    @Autowired
    private SensorRepository sensorRepository;

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
        return new NoiseSampleResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public NoiseSampleResponseDTO getSampleByPublicId(UUID publicId) {
        var noiseSample = noiseSampleRepository.findByPublicId(publicId);
        return noiseSample.map(NoiseSampleResponseDTO::new).orElse(null);
    }
}
