package com.swimpoolschedule.controller;

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
        //Todo: melhorar nome de variavel t pra uma mais descritiva.
        return turmaRepository.findById(id).map(t -> {
            //Todo: criar pasta com exceptions customizadas e lançar uma exception customizada aqui, para evitar o uso de IllegalArgumentException.
            if (maxCapacidade < t.getAlunos().size()) {
                throw new IllegalArgumentException(
                        "Nova capacidade (" + maxCapacidade + ") é menor que o número atual de alunos (" + t.getAlunos().size() + ").");
            }
            t.setMaxCapacidade(maxCapacidade);
            return ResponseEntity.ok(turmaRepository.save(t));
        }).orElse(ResponseEntity.notFound().build());
    }

    //Todo: trocar o nome de delete para deleteTurma, para evitar confusão com o delete de UsuarioController, já que ambos usam o mesmo verbo HTTP e endpoint parecido.

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return turmaRepository.findById(id).map(t -> {
            turmaRepository.delete(t);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
