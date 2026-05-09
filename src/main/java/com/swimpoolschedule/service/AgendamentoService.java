package com.swimpoolschedule.service;

import com.swimpoolschedule.dto.AlunoSummary;
import com.swimpoolschedule.enums.Horario;
import com.swimpoolschedule.model.Turma;
import com.swimpoolschedule.model.Usuario;
import com.swimpoolschedule.repository.TurmaRepository;
import com.swimpoolschedule.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgendamentoService {

    private final TurmaRepository turmaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Default pool capacity — configurable in application.properties.
     * To change: pool.max-capacidade=6
     */
    @Value("${pool.max-capacidade:4}")
    private int defaultMaxCapacidade;

    public AgendamentoService(TurmaRepository turmaRepository,
                              UsuarioRepository usuarioRepository) {
        this.turmaRepository = turmaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Initializes one Turma per Horario if they don't exist yet.
     * Called on application startup via DataInitializer.
     */
    @Transactional
    public void inicializarTurmas() {
        for (Horario horario : Horario.values()) {
            turmaRepository.findByHorario(horario).orElseGet(() -> {
                Turma turma = new Turma();
                turma.setHorario(horario);
                turma.setMaxCapacidade(defaultMaxCapacidade);
                return turmaRepository.save(turma);
            });
        }
    }

    /**
     * Books a spot for the student in the requested schedule slot.
     *
     * @return success message
     * @throws IllegalStateException  if turma is full
     * @throws IllegalArgumentException if student not found or already enrolled
     */
    @Transactional
    public String agendar(Long matricula, Horario horario) {
        Usuario aluno = usuarioRepository.findById(matricula)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aluno com matrícula " + matricula + " não encontrado."));

        Turma turma = turmaRepository.findByHorario(horario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Horário " + horario + " não existe."));

        boolean jaMatriculado = turma.getAlunos().stream()
                .anyMatch(u -> u.getMatricula().equals(matricula));
        if (jaMatriculado) {
            throw new IllegalArgumentException("Aluno já está matriculado neste horário.");
        }

        if (turma.getAlunos().size() >= turma.getMaxCapacidade()) {
            throw new IllegalStateException(
                    "Horário com turma completa, não temos raias disponíveis.");
        }

        turma.getAlunos().add(aluno);
        turmaRepository.save(turma);
        return "Horário agendado com sucesso!";
    }

    /**
     * Returns a summary (nome + matricula) of students enrolled in a given slot.
     */
    @Transactional(readOnly = true)
    public List<AlunoSummary> listarAlunos(Horario horario) {
        Turma turma = turmaRepository.findByHorario(horario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Horário " + horario + " não existe."));

        return turma.getAlunos().stream()
                .map(u -> new AlunoSummary(u.getName(), u.getMatricula()))
                .toList();
    }

    /**
     * Returns how many spots are still available in a given slot.
     */
    @Transactional(readOnly = true)
    public int raiasDisponiveis(Horario horario) {
        Turma turma = turmaRepository.findByHorario(horario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Horário " + horario + " não existe."));
        return turma.getMaxCapacidade() - turma.getAlunos().size();
    }
}

