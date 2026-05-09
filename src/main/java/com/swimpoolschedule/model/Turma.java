package com.swimpoolschedule.model;

import com.swimpoolschedule.enums.Horario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Each Turma maps to exactly one Horario slot.
     * Unique constraint prevents duplicate turmas for the same slot.
     */
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Horario horario;

    /**
     * Maximum number of students (lanes) for this turma.
     * Default 4 — change via application property or update endpoint.
     */
    @Column(nullable = false)
    private int maxCapacidade = 4;

    /**
     * Enrolled students. Only names/matricula are exposed via the summary endpoint.
     */
    @ManyToMany
    @JoinTable(
        name = "turma_alunos",
        joinColumns = @JoinColumn(name = "turma_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> alunos = new ArrayList<>();
}
