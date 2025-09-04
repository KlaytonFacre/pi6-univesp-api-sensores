package dev.univesp.grupo9.pi6.service;

import dev.univesp.grupo9.pi6.domain.noise.NoiseSample;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleRepository;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleRequestDTO;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleResponseDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class NoiseService {

    @Autowired
    private NoiseSampleRepository noiseSampleRepository;

    // GeometryFactory para criar os Points (SRID 4326 -> WGS84)
    private final GeometryFactory geometryFactory =
            new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public NoiseSampleResponseDTO saveSample(NoiseSampleRequestDTO dto) {
        // Converter BigDecimal -> double
        double lat = dto.latitude().doubleValue();
        double lon = dto.longitude().doubleValue();

        // Criar Point (ordem correta: x = lon, y = lat)
        Point local = geometryFactory.createPoint(new Coordinate(lon, lat));
        local.setSRID(4326);

        // Montar entidade
        NoiseSample noiseSample = new NoiseSample();
        noiseSample.setSensorId(dto.sensorId());
        noiseSample.setLocal(local);
        noiseSample.setNoiseDb(dto.noiseDb());

        // Persistir
        NoiseSample sample = noiseSampleRepository.save(noiseSample);

        return new NoiseSampleResponseDTO(
                sample.getId(),
                sample.getCreatedAt(),
                sample.getSensorId(),
                BigDecimal.valueOf(sample.getLocal().getY()), // latitude
                BigDecimal.valueOf(sample.getLocal().getX()), // longitude
                sample.getNoiseDb()
        );
    }
}

