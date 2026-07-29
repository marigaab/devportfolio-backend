package com.portifolio.portifolio_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PerfilRequestDTO(
    Long usuarioId, // 👈 ADICIONADO AQUI!

    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotBlank(message = "O cargo é obrigatório.")
    String cargo,

    String bio,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    String email,

    String githubUrl,
    String linkedinUrl,
    String fotoUrl
) {}