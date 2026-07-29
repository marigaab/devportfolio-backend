package com.portifolio.portifolio_api.dto;
import com.portifolio.portifolio_api.model.Usuario;

public record UsuarioResponseDTO(
    Long id,
    String email
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getEmail());
    }
}