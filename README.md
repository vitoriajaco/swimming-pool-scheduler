# swimming-pool-scheduler

API REST para agendar aulas de natação. Controla alunos, horários e a capacidade de cada turma (número de raias) e valida as regras de matrícula antes de confirmar um agendamento.

> Projeto de estudo/portfólio, criado para praticar Java e Spring Boot com arquitetura em camadas.

## O que faz

- Cadastra, consulta, atualiza e remove alunos (usuários).
- Agenda um aluno em uma turma (horário fixo da semana).
- Lista os alunos matriculados em um horário.
- Informa quantas raias ainda estão livres em um horário.
- Permite consultar, remover e ajustar a capacidade das turmas.
- Devolve mensagens de erro claras quando um agendamento viola alguma regra.

## Tecnologias

| | |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.0.6 (Web MVC, Data JPA, Validation) |
| Build | Maven (com Maven Wrapper `./mvnw`) |
| Banco de dados | H2 em memória |
| Outros | Lombok, Hibernate Validator |

## Como rodar

**Pré-requisito:** JDK 17 ou superior. Maven e banco de dados não precisam ser instalados, porque o projeto traz o Maven Wrapper e usa H2 em memória.

```bash
git clone https://github.com/vitoriajaco/swimming-pool-scheduler.git
cd swimming-pool-scheduler
./mvnw spring-boot:run
```

No macOS ou Linux também dá para usar o script `./run.sh`. Ele encerra o processo que estiver ocupando a porta 8080 e depois inicia a aplicação.

A API sobe em `http://localhost:8080`.

### Banco H2

- Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:swimpoolschedule`, usuário `sa`, sem senha

O banco começa vazio a cada reinício. Na subida, o `DataInitializer` cria uma turma para cada horário do enum `Horario`, com capacidade definida por `pool.max-capacidade` em `application.properties` (padrão: 4).

### Testes

```bash
./mvnw test
```

### Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/usuarios` | Lista usuários |
| `GET` | `/api/usuarios/{matricula}` | Busca usuário |
| `POST` | `/api/usuarios` | Cadastra usuário |
| `PUT` | `/api/usuarios/{matricula}` | Atualiza usuário |
| `DELETE` | `/api/usuarios/{matricula}` | Remove usuário |
| `POST` | `/api/agendamentos` | Agenda um aluno em um horário |
| `GET` | `/api/agendamentos/{horario}/alunos` | Lista alunos de um horário (nome e matrícula) |
| `GET` | `/api/agendamentos/{horario}/disponibilidade` | Raias livres em um horário |
| `GET` | `/api/turmas` | Lista turmas |
| `GET` | `/api/turmas/{id}` | Busca turma |
| `PUT` | `/api/turmas/{id}/capacidade?maxCapacidade=6` | Altera a capacidade da turma |
| `DELETE` | `/api/turmas/{id}` | Remove turma |

Horários disponíveis: `SEGUNDA_08H`, `QUARTA_08H`, `SABADO_10H`.
Métodos de pagamento: `DINHEIRO`, `CREDITO`, `DEBITO`, `PIX`, `BOLSISTA`.

Exemplo de fluxo:

```bash
# 1. Cadastrar um aluno
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"name": "Ana Souza", "email": "ana@exemplo.com", "nascimento": "2000-05-10", "metodoPagamento": "PIX", "pagamentoEmDia": true}'

# 2. Agendar no sábado
curl -X POST http://localhost:8080/api/agendamentos \
  -H "Content-Type: application/json" \
  -d '{"matricula": 1, "horario": "SABADO_10H"}'

# 3. Consultar vagas
curl http://localhost:8080/api/agendamentos/SABADO_10H/disponibilidade
```

## Estrutura de pastas

```
src/
├── main/
│   ├── java/com/swimpoolschedule/
│   │   ├── SwimpoolscheduleApplication.java   # ponto de entrada
│   │   ├── config/        # inicialização de dados e tratamento global de erros
│   │   ├── controller/    # endpoints REST (usuários, agendamentos, turmas)
│   │   ├── service/       # regras de negócio de agendamento
│   │   ├── repository/    # acesso a dados (Spring Data JPA)
│   │   ├── model/         # entidades JPA (Usuario, Turma)
│   │   ├── dto/           # objetos de entrada e saída da API
│   │   ├── enums/         # Horario, MetodoPagamento
│   │   └── exception/     # exceções de domínio
│   └── resources/
│       └── application.properties
├── test/                  # testes automatizados
└── specify.md             # especificação funcional do sistema
```

O fluxo segue a arquitetura em camadas: **controller → service → repository → banco**.

## Regras de negócio

Para que um agendamento seja confirmado:

1. **O aluno deve estar cadastrado.** Se a matrícula não existir, a API devolve `400`.
2. **O horário deve existir.** Se não houver turma para o horário, a API devolve `400`.
3. **Não há matrícula duplicada.** Um aluno não pode se matricular duas vezes na mesma turma (`400`).
4. **Inadimplentes não agendam.** Um aluno com `pagamentoEmDia = false` é bloqueado (`409`). **Bolsistas** (`metodoPagamento = BOLSISTA`) ficam isentos dessa regra.
5. **A capacidade da turma é respeitada.** Cada aluno ocupa uma raia, e quando a turma lota o agendamento é recusado (`409`).

Outras regras:

- Existe exatamente uma turma por horário.
- A capacidade de uma turma não pode ficar menor que o número de alunos já matriculados (`400`).
- No cadastro de usuário, `name`, `email` (em formato válido) e `metodoPagamento` são obrigatórios.

Os erros seguem o formato:

```json
{ "status": 409, "mensagem": "Horário com turma completa, não temos raias disponíveis." }
```

## Status

🚧 **v1, em desenvolvimento.** Projeto de estudo/portfólio.

O fluxo principal já funciona: cadastro de alunos, agendamento com validação das regras e consultas de alunos e vagas.

Próximos passos (ver `src/specify.md`):

- [ ] Cancelamento e alteração de agendamentos
- [ ] Lista de espera
- [ ] Histórico de operações
- [ ] Controle de manutenção de horários
- [ ] Autenticação e perfis de acesso
- [ ] Auditoria, paginação, filtros e cache
- [ ] Migração para PostgreSQL e conteinerização com Docker
- [ ] Testes unitários e de integração das regras de negócio
