package com.swimpoolschedule.controller;

import com.swimpoolschedule.model.Usuario;
import com.swimpoolschedule.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(value = "/api/usuarios", produces = "application/json")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> all() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> get(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@Valid @RequestBody Usuario usuario) {
        // If matricula is provided it will be ignored if null generation is used
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @Valid @RequestBody Usuario updated) {
        return usuarioRepository.findById(id).map(u -> {
            u.setName(updated.getName());
            u.setNascimento(updated.getNascimento());
            u.setEmail(updated.getEmail());
            u.setPagamentoEmDia(updated.getPagamentoEmDia());
            u.setDisponibilidade(updated.getDisponibilidade());
            u.setMetodoPagamento(updated.getMetodoPagamento());
            usuarioRepository.save(u);
            return ResponseEntity.ok(u);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(u -> {
            usuarioRepository.delete(u);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

}

