package dev.univesp.grupo9.pi6.controller;

import dev.univesp.grupo9.pi6.domain.medicao.MedicaoRequestDTO;
import dev.univesp.grupo9.pi6.domain.medicao.MedicaoResponseDTO;
import dev.univesp.grupo9.pi6.service.MedicaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicoes")
@RequiredArgsConstructor
public class MedicaoController {

    @Autowired
    private final MedicaoService medicaoService;

    @PostMapping
    public ResponseEntity<MedicaoResponseDTO> criarMedicao(@Valid @RequestBody MedicaoRequestDTO dto) {
        var salvo = medicaoService.salvarMedicao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
