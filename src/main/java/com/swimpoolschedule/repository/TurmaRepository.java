package com.swimpoolschedule.repository;

import com.swimpoolschedule.enums.Horario;
import com.swimpoolschedule.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    Optional<Turma> findByHorario(Horario horario);
}

