### Especificação do Sistema de Agendamento


API para gerenciamento de agendamentos de aulas de natação, responsável por controlar usuários, horários, capacidade das turmas e regras de matrícula.



O sistema garante que todo agendamento respeite as regras de negócio definidas, validando disponibilidade, capacidade e elegibilidade do usuário antes da confirmação.

Regras de negócio
O usuário deve estar cadastrado.
O horário deve existir.
Um usuário não pode ser matriculado duas vezes na mesma turma.
A capacidade máxima da turma deve ser respeitada.
Usuários inadimplentes não podem agendar, exceto bolsistas.
Apenas vagas disponíveis podem ser ocupadas.
Fluxos do usuário
Cadastrar usuário.
Consultar usuários.
Atualizar ou remover usuários.
Realizar agendamento.
Consultar alunos por turma.
Consultar disponibilidade de vagas.
Critérios de aceitação
Agendamentos válidos devem ser concluídos com sucesso.
Agendamentos inválidos devem retornar mensagens de erro claras.
A capacidade da turma nunca poderá ser excedida.
Não deve existir matrícula duplicada na mesma turma.
As regras de pagamento devem ser aplicadas em todos os agendamentos.
A disponibilidade de vagas deve refletir o estado atual das matrículas.
Evoluções previstas
Cancelamento e alteração de agendamentos.
Lista de espera.
Histórico de operações.
Controle de manutenção de horários.
Autenticação e perfis de acesso.
Auditoria, paginação, filtros e cache.
Migração para PostgreSQL e conteinerização com Docker.