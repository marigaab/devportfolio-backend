package com.portifolio.portifolio_api.controller;

import com.portifolio.portifolio_api.dto.PerfilRequestDTO;
import com.portifolio.portifolio_api.dto.PerfilResponseDTO;
import com.portifolio.portifolio_api.model.Perfil;
import com.portifolio.portifolio_api.model.Usuario;
import com.portifolio.portifolio_api.repository.PerfilRepository;
import com.portifolio.portifolio_api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfil")
@CrossOrigin(origins = "*")
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<PerfilResponseDTO>> listarTodos() {
        List<PerfilResponseDTO> lista = perfilRepository.findAll()
                .stream()
                .map(PerfilResponseDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> buscarPorId(@PathVariable Long id) {
        return perfilRepository.findById(id)
                .map(p -> ResponseEntity.ok(new PerfilResponseDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🚀 1. CADASTRAR (POST): Cria SEMPRE um novo Usuário + Perfil
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid PerfilRequestDTO dto) {
        try {
            Usuario novoUsuario = criarNovoUsuario(dto.email());
            
            Perfil perfil = new Perfil();
            perfil.setUsuario(novoUsuario);
            mapearDtoParaPerfil(dto, perfil);

            Perfil perfilSalvo = perfilRepository.save(perfil);
            return ResponseEntity.status(HttpStatus.CREATED).body(new PerfilResponseDTO(perfilSalvo));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar novo perfil: " + e.getMessage());
        }
    }

    // ✏️ 2. ATUALIZAR (PUT): Atualiza um perfil EXISTENTE pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid PerfilRequestDTO dto) {
        try {
            Perfil perfil = perfilRepository.findById(id)
                    .orElseGet(() -> {
                        // Tenta localizar por usuarioId se não achar por id do perfil diretamente
                        return perfilRepository.findAll().stream()
                                .filter(p -> p.getUsuario() != null && p.getUsuario().getId().equals(id))
                                .findFirst()
                                .orElse(null);
                    });

            if (perfil == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil não encontrado para atualização.");
            }

            mapearDtoParaPerfil(dto, perfil);

            Perfil perfilAtualizado = perfilRepository.save(perfil);
            return ResponseEntity.ok(new PerfilResponseDTO(perfilAtualizado));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar perfil: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPerfil(@PathVariable Long id) {
        return perfilRepository.findById(id).map(perfil -> {
            Usuario usuario = perfil.getUsuario();
            if (usuario != null) {
                usuarioRepository.delete(usuario);
            } else {
                perfilRepository.delete(perfil);
            }
            return ResponseEntity.ok().build();
        }).orElseGet(() -> {
            return usuarioRepository.findById(id).map(usuario -> {
                usuarioRepository.delete(usuario);
                return ResponseEntity.ok().build();
            }).orElse(ResponseEntity.notFound().build());
        });
    }

    // --- MÉTODOS AUXILIARES DE SUPORTE ---

    private void mapearDtoParaPerfil(PerfilRequestDTO dto, Perfil perfil) {
        perfil.setNome(dto.nome());
        perfil.setCargo(dto.cargo());
        perfil.setBio(dto.bio());
        perfil.setEmail(dto.email());
        perfil.setGithubUrl(dto.githubUrl());
        perfil.setLinkedinUrl(dto.linkedinUrl());
        perfil.setFotoUrl(dto.fotoUrl());
    }

    private Usuario criarNovoUsuario(String email) {
        Usuario novoUser = new Usuario();
        novoUser.setEmail(email != null && !email.isBlank() ? email : "dev" + System.currentTimeMillis() + "@email.com");
        novoUser.setSenha("123456");
        return usuarioRepository.save(novoUser);
    }
}