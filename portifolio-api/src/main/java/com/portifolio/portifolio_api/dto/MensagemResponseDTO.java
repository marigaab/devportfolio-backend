package com.portifolio.portifolio_api.dto;

import com.portifolio.portifolio_api.model.Mensagem;
import java.time.LocalDateTime;

public record MensagemResponseDTO(
    Long id,
    String nome,
    String email,
    String conteudo,
    LocalDateTime dataEnvio
) {
    public MensagemResponseDTO(Mensagem mensagem) {
        this(
            mensagem.getId(),
            mensagem.getNome(),
            mensagem.getEmail(),
            mensagem.getConteudo(),
            mensagem.getDataEnvio()
        );
    }
}