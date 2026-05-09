package com.swimpoolschedule.dto;

import com.swimpoolschedule.enums.Horario;
import jakarta.validation.constraints.NotNull;

public record AgendamentoRequest(
    @NotNull(message = "matricula é obrigatória")
    Long matricula,

    @NotNull(message = "horario é obrigatório")
    Horario horario
) {}

