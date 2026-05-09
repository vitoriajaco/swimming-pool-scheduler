
package com.swimpoolschedule.model;

import com.swimpoolschedule.enums.MetodoPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matricula;

    @NotBlank(message = "nome é obrigatório")
    private String name;

    private LocalDate nascimento;

    @NotBlank(message = "email é obrigatório")
    @Email(message = "email deve ser válido")
    private String email;

    private Boolean pagamentoEmDia = Boolean.TRUE;

    @NotNull(message = "metodoPagamento é obrigatório")
    @Enumerated(EnumType.STRING)
    @JsonProperty("metodoPagamento")
    @JsonAlias({"paymentMethod"})
    private MetodoPagamento metodoPagamento;

    // Map<dia, horariosCommaSeparated>
    @ElementCollection
    @CollectionTable(name = "usuario_disponibilidade", joinColumns = @JoinColumn(name = "usuario_id"))
    @MapKeyColumn(name = "dia")
    @Column(name = "horarios")
    private Map<String, String> disponibilidade = new HashMap<>();

}

