package com.portifolio.portifolio_api.dto;

import com.portifolio.portifolio_api.model.Projeto;

public record ProjetoResponseDTO(
    Long id,
    String titulo,
    String descricao,
    String tecnologias,
    String urlRepositorio,
    String urlDeploy,
    String urlImagem
) {
    // Construtor auxiliar para mapear facilmente uma Entidade Projeto para DTO
    public ProjetoResponseDTO(Projeto projeto) {
        this(
            projeto.getId(),
            projeto.getTitulo(),
            projeto.getDescricao(),
            projeto.getTecnologias(),
            projeto.getUrlRepositorio(),
            projeto.getUrlDeploy(),
            projeto.getUrlImagem()
        );
    }
}