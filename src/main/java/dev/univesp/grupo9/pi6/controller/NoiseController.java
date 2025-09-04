package dev.univesp.grupo9.pi6.controller;

import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleRequestDTO;
import dev.univesp.grupo9.pi6.domain.noise.NoiseSampleResponseDTO;
import dev.univesp.grupo9.pi6.service.NoiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noise")
public class NoiseController {

    @Autowired
    private NoiseService noiseService;

    @PostMapping
    public ResponseEntity<NoiseSampleResponseDTO> createSampleOnDatabase(@Valid @RequestBody NoiseSampleRequestDTO dto) {
        var salvo = noiseService.saveSample(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
