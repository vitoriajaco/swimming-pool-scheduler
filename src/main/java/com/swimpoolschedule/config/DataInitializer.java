package com.swimpoolschedule.config;

import com.swimpoolschedule.service.AgendamentoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {
//Como o H2 é um banco em memória, ele é zerado toda vez que a aplicação reinicia.
// Sem o DataInitializer, as turmas não
// existiriam e qualquer tentativa de agendar retornaria erro de "Horário não existe".
    private final AgendamentoService agendamentoService;

    public DataInitializer(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @Override
    public void run(String... args) {
        agendamentoService.inicializarTurmas();
    }
}

