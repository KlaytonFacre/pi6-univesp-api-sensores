package dev.univesp.grupo9.pi6.controller;

import dev.univesp.grupo9.pi6.domain.sample.NoiseSampleRequestDTO;
import dev.univesp.grupo9.pi6.domain.sample.NoiseSampleResponseDTO;
import dev.univesp.grupo9.pi6.service.NoiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/noise")
public class NoiseController {

    @Autowired
    private NoiseService noiseService;

    @PostMapping
    public ResponseEntity<NoiseSampleResponseDTO> createSample(@Valid @RequestBody NoiseSampleRequestDTO dto) {
        NoiseSampleResponseDTO saved = noiseService.saveSample(dto);
        URI location = URI.create("/noise/" + saved.samplePublicId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<NoiseSampleResponseDTO> getSample(@PathVariable UUID publicId) {
        NoiseSampleResponseDTO sample = noiseService.getSampleByPublicId(publicId);
        return ResponseEntity.ok(sample);
    }
}

