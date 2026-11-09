package com.swimpoolschedule.controller;

import com.swimpoolschedule.exception.BadRequestException;
import com.swimpoolschedule.model.Turma;
import com.swimpoolschedule.repository.TurmaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/turmas", produces = "application/json")
public class TurmaController {

    private final TurmaRepository turmaRepository;

    public TurmaController(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    @GetMapping
    public List<Turma> all() {
        return turmaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turma> get(@PathVariable Long id) {
        return turmaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/turmas/{id}/capacidade?maxCapacidade=6
     * Allows expanding (or shrinking, if no students are affected) the lane capacity.
     */
    @PutMapping("/{id}/capacidade")
    public ResponseEntity<Turma> updateCapacidade(
            @PathVariable Long id,
            @RequestParam int maxCapacidade) {
        // Use a descriptive variable name instead of a single-letter lambda param.
        return turmaRepository.findById(id).map(turma -> {
            // Use a domain-specific exception to provide clearer semantics
            // instead of throwing a raw IllegalArgumentException.
            if (maxCapacidade < turma.getAlunos().size()) {
                throw new BadRequestException(
                        "Nova capacidade (" + maxCapacidade + ") é menor que o número atual de alunos (" + turma.getAlunos().size() + ").");
            }
            turma.setMaxCapacidade(maxCapacidade);
            return ResponseEntity.ok(turmaRepository.save(turma));
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/turmas/{id}

     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurma(@PathVariable Long id) {
        return turmaRepository.findById(id).map(turma -> {
            turmaRepository.delete(turma);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
