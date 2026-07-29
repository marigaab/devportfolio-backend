package com.portifolio.portifolio_api.controller;

import com.portifolio.portifolio_api.dto.ProjetoRequestDTO;
import com.portifolio.portifolio_api.dto.ProjetoResponseDTO;
import com.portifolio.portifolio_api.model.Projeto;
import com.portifolio.portifolio_api.model.Usuario;
import com.portifolio.portifolio_api.repository.ProjetoRepository;
import com.portifolio.portifolio_api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
@CrossOrigin(origins = "*")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired // 👈 Garantir que este Autowired está aqui para não dar NullPointerException
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<ProjetoResponseDTO>> listarTodos() {
        List<ProjetoResponseDTO> lista = projetoRepository.findAll()
                .stream()
                .map(ProjetoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid ProjetoRequestDTO dto) {
        try {
            Projeto projeto = new Projeto();
            BeanUtils.copyProperties(dto, projeto);

            // Pega o usuarioId enviado pelo formulário ou assume o ID 1
            Long idUsuario = (dto.usuarioId() != null) ? dto.usuarioId() : 1L;

            // Busca o usuário no banco ou cria caso ainda não exista
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseGet(() -> {
                        Usuario novoUser = new Usuario();
                        novoUser.setEmail("admin@email.com");
                        novoUser.setSenha("123456");
                        return usuarioRepository.save(novoUser);
                    });

            projeto.setUsuario(usuario);

            Projeto projetoSalvo = projetoRepository.save(projeto);
            return ResponseEntity.ok(new ProjetoResponseDTO(projetoSalvo));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar projeto: " + e.getMessage());
        }
    }
    @GetMapping("/usuario/{usuarioId}")
public ResponseEntity<List<ProjetoResponseDTO>> buscarPorUsuario(@PathVariable Long usuarioId) {
    List<ProjetoResponseDTO> lista = projetoRepository.findByUsuarioId(usuarioId)
            .stream()
            .map(ProjetoResponseDTO::new)
            .toList();
            
    return ResponseEntity.ok(lista);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> deletarProjeto(@PathVariable Long id) {
    if (projetoRepository.existsById(id)) {
        projetoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
}
@PutMapping("/{id}")
public ResponseEntity<?> atualizarProjeto(@PathVariable Long id, @RequestBody Projeto projetoAtualizado) {
    return projetoRepository.findById(id).map(projeto -> {
        projeto.setTitulo(projetoAtualizado.getTitulo());
        projeto.setDescricao(projetoAtualizado.getDescricao());
        projeto.setTecnologias(projetoAtualizado.getTecnologias());
        projeto.setUrlRepositorio(projetoAtualizado.getUrlRepositorio());
        projeto.setUrlImagem(projetoAtualizado.getUrlImagem());
        
        Projeto salvo = projetoRepository.save(projeto);
        return ResponseEntity.ok(salvo);
    }).orElse(ResponseEntity.notFound().build());
}
}