package com.swimpoolschedule.controller;

import com.swimpoolschedule.dto.AgendamentoRequest;
import com.swimpoolschedule.dto.AlunoSummary;
import com.swimpoolschedule.enums.Horario;
import com.swimpoolschedule.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/agendamentos", produces = "application/json")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    /**
     * POST /api/agendamentos
     * Body: { "matricula": 1, "horario": "SABADO_10H" }
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> agendar(
            @Valid @RequestBody AgendamentoRequest request) {
        String msg = agendamentoService.agendar(request.matricula(), request.horario());
        return ResponseEntity.ok(Map.of("mensagem", msg));
    }

    /**
     * GET /api/agendamentos/{horario}/alunos
     * Returns only nome and matricula of enrolled students.
     * Example: GET /api/agendamentos/SABADO_10H/alunos
     */
    @GetMapping("/{horario}/alunos")
    public List<AlunoSummary> listarAlunos(@PathVariable Horario horario) {
        return agendamentoService.listarAlunos(horario);
    }

    /**
     * GET /api/agendamentos/{horario}/disponibilidade
     * Returns how many lanes are still available.
     * Example: GET /api/agendamentos/SABADO_10H/disponibilidade
     */
    @GetMapping("/{horario}/disponibilidade")
    public ResponseEntity<Map<String, Object>> disponibilidade(@PathVariable Horario horario) {
        int livres = agendamentoService.raiasDisponiveis(horario);
        return ResponseEntity.ok(Map.of(
                "horario", horario,
                "raiasDisponiveis", livres
        ));
    }
}

