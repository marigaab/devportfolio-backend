package com.portifolio.portifolio_api.controller;

import com.portifolio.portifolio_api.dto.MensagemRequestDTO;
import com.portifolio.portifolio_api.dto.MensagemResponseDTO;
import com.portifolio.portifolio_api.model.Mensagem;
import com.portifolio.portifolio_api.repository.MensagemRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensagens")
@CrossOrigin(origins = "*")
public class MensagemController {

    @Autowired
    private MensagemRepository mensagemRepository;

    // Visitante envia uma mensagem (POST)
    @PostMapping
    public ResponseEntity<MensagemResponseDTO> enviarMensagem(@RequestBody @Valid MensagemRequestDTO dto) {
        Mensagem mensagem = new Mensagem();
        BeanUtils.copyProperties(dto, mensagem);

        Mensagem salva = mensagemRepository.save(mensagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MensagemResponseDTO(salva));
    }

    // Dono do portfólio visualiza as mensagens recebidas (GET)
    @GetMapping
    public ResponseEntity<List<MensagemResponseDTO>> listarMensagens() {
        List<MensagemResponseDTO> lista = mensagemRepository.findAll()
                .stream()
                .map(MensagemResponseDTO::new)
                .toList();

        return ResponseEntity.ok(lista);
    }
}