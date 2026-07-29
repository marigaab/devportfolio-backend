package com.portifolio.portifolio_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MensagemRequestDTO(
    @NotBlank(message = "Seu nome é obrigatório.")
    String nome,

    @NotBlank(message = "Seu e-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    String email,

    @NotBlank(message = "Escreva uma mensagem.")
    String conteudo
) {}