package com.portifolio.portifolio_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjetoRequestDTO(
    Long usuarioId, 
    @NotBlank(message = "O título é obrigatório.")
    String titulo,

    @NotBlank(message = "A descrição é obrigatória.")
    String descricao,

    @NotBlank(message = "As tecnologias são obrigatórias.")
    String tecnologias,

    String urlRepositorio,
    String urlDeploy,
    String urlImagem
) {}