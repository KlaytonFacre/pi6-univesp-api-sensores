package dev.univesp.grupo9.pi6.service;

import dev.univesp.grupo9.pi6.domain.medicao.Medicao;
import dev.univesp.grupo9.pi6.domain.medicao.MedicaoRepository;
import dev.univesp.grupo9.pi6.domain.medicao.MedicaoRequestDTO;
import dev.univesp.grupo9.pi6.domain.medicao.MedicaoResponseDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MedicaoService {

    @Autowired
    private MedicaoRepository medicaoRepository;

    // GeometryFactory para criar os Points (SRID 4326 -> WGS84)
    private final GeometryFactory geometryFactory =
            new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public MedicaoResponseDTO salvarMedicao(MedicaoRequestDTO dto) {
        // Converter BigDecimal -> double
        double lat = dto.latitude().doubleValue();
        double lon = dto.longitude().doubleValue();

        // Criar Point (ordem correta: x = lon, y = lat)
        Point local = geometryFactory.createPoint(new Coordinate(lon, lat));
        local.setSRID(4326);

        // Montar entidade
        Medicao medicao = new Medicao();
        medicao.setSensorId(dto.sensorId());
        medicao.setLocal(local);
        medicao.setNoiseDb(dto.noiseDb());

        // Persistir
        Medicao salvo = medicaoRepository.save(medicao);

        return new MedicaoResponseDTO(
                salvo.getId(),
                salvo.getTimestamp(),
                salvo.getSensorId(),
                BigDecimal.valueOf(salvo.getLocal().getY()), // latitude
                BigDecimal.valueOf(salvo.getLocal().getX()), // longitude
                salvo.getNoiseDb()
        );
    }
}

