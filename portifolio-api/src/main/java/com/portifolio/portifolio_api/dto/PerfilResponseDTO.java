package com.portifolio.portifolio_api.dto;

import com.portifolio.portifolio_api.model.Perfil;

public record PerfilResponseDTO(
    Long id,
    Long usuarioId,
    String nome,
    String cargo,
    String bio,
    String email,
    String githubUrl,
    String linkedinUrl,
    String fotoUrl
) {
    public PerfilResponseDTO(Perfil perfil) {
        this(
            perfil.getId(),
            perfil.getUsuario() != null ? perfil.getUsuario().getId() : null,
            perfil.getNome(),
            perfil.getCargo(),
            perfil.getBio(),
            perfil.getEmail(),
            perfil.getGithubUrl(),
            perfil.getLinkedinUrl(),
            perfil.getFotoUrl()
        );
    }
}