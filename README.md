# PaperFlow

**Documentação do projeto**
`README.md` e documentação final do projeto.

**Equipe:** Adicionar nomes dos integrantes.
**Disciplina:** Padrões de Projeto de Software.
**Professor:** Adicionar nome do professor.
**Repositório:** Adicionar link do GitHub.
**Branch atual:** Adicionar nome da branch ou branch final de entrega.

---

## 1. Visão geral do projeto

PaperFlow é um sistema para gerenciamento do fluxo de submissão e avaliação de artigos científicos em eventos acadêmicos. A proposta é permitir o cadastro de usuários, submissão de artigos, organização de áreas temáticas, formação de comitê técnico, avaliação de trabalhos e geração de informações de acompanhamento do evento.

**Descrição completa do projeto:** Adicionar uma descrição geral final depois que todos os módulos estiverem integrados.

---

## 2. Tecnologias e organização inicial

- **Linguagem:** Java.
- **Gerenciamento do projeto:** Maven (com Maven Wrapper — `./mvnw`).
- **Persistência atual:** em memória, através de gateways (`InMemoryUserGateway`, `InMemoryPaperGateway`, `InMemoryReviewAssignmentGateway`, `InMemoryEmailGateway`), com previsão de substituição por persistência em banco de dados.
- **Interface atual:** interface gráfica própria, construída sobre um sistema de telas e componentes (`presentation/ui`).
- **Organização arquitetural:** separação em camadas inspirada na Arquitetura Limpa.

**Observação sobre persistência:** os dados atualmente não são persistidos entre execuções — cada gateway guarda as informações em memória enquanto o programa está em execução. A troca por uma implementação em banco de dados é possível sem alterar os casos de uso, já que eles dependem apenas das interfaces de gateway (Dependency Inversion Principle).

---

## 3. Organização por camadas

| Camada/Pacote | Responsabilidade |
|---|---|
| `domain` | Entidades e regras centrais do domínio, como `User`, `Topic`, `TopicGroup`, `Event`, `Paper` e `CommitteeInvitation`. |
| `application` | Casos de uso, fábricas, interfaces de gateway, filtros e eventos/observadores da aplicação. |
| `infrastructure` | Implementações técnicas, como os gateways em memória e o envio de e-mail. |
| `presentation` | Interface gráfica do sistema (telas e componentes de UI). |

---

## 4. Como executar o projeto

O projeto inclui o Maven Wrapper, então não é necessário ter o Maven instalado para executá-lo.

1. Clonar ou baixar o projeto do GitHub.
2. Abrir um terminal na pasta raiz do projeto (onde está o `pom.xml`).
3. Rodar o projeto:

   No Linux/Mac:
   ```bash
   ./mvnw clean compile
   ./mvnw exec:java -Dexec.mainClass="com.system.paperflow.Main"
   ```

   No Windows:
   ```cmd
   mvnw.cmd clean compile
   mvnw.cmd exec:java -Dexec.mainClass="com.system.paperflow.Main"
   ```

Não é necessário instalar nenhum banco de dados: os dados são mantidos em memória durante a execução.

---

## 5. Divisão da equipe e módulos

A tabela abaixo serve como esqueleto para a documentação final. Cada integrante poderá preencher o módulo que desenvolveu, os requisitos atendidos e os padrões utilizados.

| Integrante | Módulo/Requisitos | Padrões GoF utilizados | Status |
|---|---|---|---|
| Felipe Oliveira Raimundo | RF02 - Cadastro de usuários; RF03 - Áreas temáticas; RF04 - Comitê técnico | Factory Method; Composite; Observer | Implementado em versão funcional inicial |
| Gabriel | RF01 - Início do evento; RF05 - Submissão e ciclo de vida do artigo; RF08 - Dashboard; RF10 - Filtro de artigos (adicional) | Chain of Responsibility | Implementado em versão funcional inicial |
| Daniel | RF06 - Distribuição de artigos; RF07 - Avaliação; RF09 - Notificação de autores | Adicionar padrões utilizados | Adicionar status |

---

## 6. Módulo de Usuários, Áreas Temáticas e Comitê Técnico

Este módulo contempla os requisitos **RF02, RF03 e RF04**. Eles foram implementados em sequência porque um requisito prepara a base para o próximo: primeiro o sistema precisa cadastrar usuários, depois o coordenador cadastra as áreas temáticas, e por fim o coordenador forma o comitê técnico com usuários previamente cadastrados.

```text
RF02 - Cadastro de usuários
        ↓
RF03 - Cadastro de áreas temáticas
        ↓
RF04 - Comitê técnico de avaliação
```

---

### 6.1 RF02 - Cadastro de usuários

O RF02 permite cadastrar usuários no sistema informando **e-mail, senha e instituição**. O e-mail é tratado como chave do usuário, portanto a principal regra de negócio é impedir o cadastro de dois usuários com o mesmo e-mail.

Nesta primeira versão, o usuário comum entra como `Researcher`. O papel de `Reviewer` não é criado diretamente no cadastro: ele surge depois, no RF04, quando o usuário aceita o convite para participar do comitê técnico.

#### Padrão utilizado: Factory Method

O **Factory Method** foi utilizado para centralizar a criação dos tipos de usuário. Em vez de espalhar criações diretas de `Researcher`, `Reviewer` ou `Coordinator` pelos casos de uso, a criação é delegada para classes criadoras.

```text
UserCreator
 ├── ResearcherCreator
 ├── ReviewerCreator
 └── CoordinatorCreator
```

Essa escolha deixa o código mais fácil de estender. Caso surja outro tipo de usuário, a tendência é adicionar uma nova classe criadora, sem precisar alterar o fluxo principal do cadastro.

#### Classes principais do RF02

- `UserCreator`, `ResearcherCreator`, `ReviewerCreator` e `CoordinatorCreator`: classes relacionadas à criação dos tipos de usuário.
- `RegisterUserUseCase`: executa o fluxo de cadastro de usuário.
- `FindUserByEmailUseCase`: busca um usuário cadastrado pelo e-mail.
- `EnsureDefaultCoordinatorUseCase`: garante que exista um coordenador padrão para a simulação.
- `UserPersistence` ou `UserGateway`: interface usada pelos casos de uso para persistência de usuários.
- Implementação em memória: guarda os usuários cadastrados durante a execução.

#### Coordenador padrão

Para permitir a simulação dos requisitos RF03 e RF04, o sistema cria automaticamente um coordenador padrão caso ele ainda não exista no banco. Esse coordenador representa o usuário responsável por cadastrar áreas temáticas e formar o comitê técnico.

| Campo | Valor |
|---|---|
| Nome | Coordenador Geral |
| E-mail | coordenador@paperflow.com |
| Senha | admin123 |
| Instituição | PaperFlow |
| Tipo | Coordinator |

---

### 6.2 RF03 - Cadastro de áreas temáticas

O RF03 permite que o coordenador cadastre as áreas temáticas do evento. Essas áreas continuam sendo palavras-chave, usadas posteriormente pelos autores na submissão dos artigos e pelos revisores na indicação de expertise.

#### Padrão utilizado: Composite

O **Composite** foi aplicado para organizar palavras-chave em uma árvore de grupos e subgrupos temáticos. Assim, o sistema consegue tratar uma área simples e uma área composta de forma uniforme.

```text
TopicComponent
 ├── Topic       → palavra-chave simples
 └── TopicGroup  → grupo de áreas e subáreas
```

Exemplo de organização temática:

```text
Inteligência Artificial
 ├── Machine Learning
 ├── Visão Computacional
 └── Processamento de Linguagem Natural
      ├── LLMs
      └── Mineração de Texto
```

#### Regras de negócio do RF03

- Somente um coordenador cadastrado pode gerenciar áreas temáticas.
- Um grupo de áreas deve possuir pelo menos uma palavra-chave.
- Não deve haver palavras-chave duplicadas dentro da mesma árvore temática.

#### Classes principais do RF03

- `TopicComponent`: interface comum do Composite.
- `Topic`: folha da árvore, representando uma palavra-chave.
- `TopicGroup`: elemento composto, representando um grupo de áreas.
- `CreateTopicTreeUseCase`: cadastra a árvore de áreas temáticas.
- `ListTopicTreeUseCase`: lista as áreas temáticas cadastradas.
- `TopicPersistence`: interface de persistência das áreas.
- Implementação em memória: salva e reconstrói a árvore de áreas temáticas durante a execução.
- `RF03TopicCompositeDemo`: simulação em console do RF03.

---

### 6.3 RF04 - Comitê técnico de avaliação

O RF04 permite que o coordenador forme o comitê técnico do evento. O fluxo começa com o convite de um usuário já cadastrado. O convite inicia como `PENDING` e pode ser aceito ou rejeitado pelo usuário convidado.

```text
Coordenador convida usuário cadastrado
        ↓
Convite criado com status PENDING
        ↓
Usuário aceita ou rejeita
        ↓
Se aceitar, passa a atuar como Reviewer
        ↓
Reviewer informa áreas de expertise
```

#### Padrão GoF utilizado: Observer

O **Observer** foi utilizado para notificar eventos internos do processo de convite do comitê técnico. A implementação não envia e-mail real, pois essa responsabilidade pertence aos requisitos de notificação/e-mail do projeto. Aqui, o objetivo é registrar e demonstrar eventos como convite criado, aceito ou rejeitado.

```text
CommitteeInvitationPublisher
        ↓ notifica
CommitteeInvitationObserver
        ↓
ConsoleCommitteeObserver
CommitteeAuditTrailObserver
```

#### Regras de negócio do RF04

- Somente coordenador pode convidar usuários para o comitê.
- O usuário convidado precisa estar previamente cadastrado.
- Não pode existir convite pendente duplicado para o mesmo usuário.
- O convite só pode ser aceito ou rejeitado se estiver com status `PENDING`.
- O convite só pode ser respondido pelo usuário convidado.
- Para aceitar o convite, o usuário deve informar pelo menos uma área de expertise.
- Ao aceitar o convite, o usuário passa a atuar como `Reviewer`.

#### Classes principais do RF04

- `CommitteeInvitation`: entidade que representa o convite para o comitê técnico.
- `InviteReviewerUseCase`: cria convite para um usuário cadastrado.
- `AcceptCommitteeInvitationUseCase`: aceita convite e transforma o usuário em `Reviewer`.
- `RejectCommitteeInvitationUseCase`: rejeita convite pendente.
- `FindCommitteeInvitationByIdUseCase`: busca convite por identificador.
- `FindPendingCommitteeInvitationByReviewerEmailUseCase`: localiza convite pendente por usuário convidado.
- `ListCommitteeInvitationsUseCase`: lista os convites registrados.
- `ListTechnicalCommitteeUseCase`: lista revisores aceitos no comitê.
- `CommitteeInvitationPublisher` e `CommitteeInvitationObserver`: estrutura do Observer.
- `ConsoleCommitteeObserver`: mostra eventos no console.
- `CommitteeAuditTrailObserver`: mantém histórico interno dos eventos observados.
- `CommitteePersistence`: interface de persistência do comitê.
- Implementação em memória: salva convites e expertises dos revisores durante a execução.
- `RF04CommitteeDemo`: simulação em console do RF04.

---

## 7. Módulo de Início de Evento, Submissão e Dashboard

Este módulo contempla os requisitos **RF01, RF05, RF08 e RF10**. Eles foram implementados em sequência porque um requisito prepara a base para o próximo: primeiro o sistema precisa ter um evento ativo, depois os autores submetem artigos para esse evento, e por fim o coordenador acompanha o andamento do evento pelo dashboard e pode consultar os artigos com filtros combinados.

```text
RF01 - Início do evento
        ↓
RF05 - Submissão e ciclo de vida do artigo
        ↓
RF08 - Dashboard do evento
        ↓
RF10 - Filtro de artigos (requisito adicional)
```

---

### 7.1 RF01 - Início do evento

O RF01 prepara o sistema para receber um novo evento de submissão. Cada evento é identificado por **nome, cidade e período**, possui uma data limite de submissão e uma categoria (Full Paper, Short Paper ou Demo).

O evento nasce em preparação (`CreateEventUseCase`) e só passa a aceitar submissões depois de iniciado (`StartEventUseCase`). Para ser iniciado, o evento precisa ter pelo menos uma área temática e pelo menos um membro no comitê técnico cadastrados.

#### Classes principais do RF01

- `Event`: entidade que representa o evento, com `start()` (valida pré-requisitos e ativa o evento), `isOpenForSubmission()` (verifica se está ativo e dentro do prazo) e `isStarted()`.
- `EventCategory`: enum com as categorias do evento (FULL_PAPER, SHORT_PAPER, DEMO).
- `CreateEventUseCase`: cria um novo evento em preparação.
- `StartEventUseCase`: ativa um evento já criado, liberando-o para submissões.
- `EventManager`: mantém referência ao evento atualmente em uso pela aplicação.

---

### 7.2 RF05 - Submissão e ciclo de vida do artigo

O RF05 trata do fluxo de submissão de um artigo científico e do seu ciclo de avaliação: submetido, em avaliação, aceito ou rejeitado. O artigo só pode ser submetido para um evento que já esteja iniciado (`isOpenForSubmission()`) e exige título, resumo e ao menos uma área temática.

O ciclo de vida do artigo é representado pelo enum `PaperStatus`, que assume os valores `SUBMITTED`, `UNDER_REVIEW`, `ACCEPTED` e `REJECTED` conforme o andamento da avaliação.

#### Regras de negócio do RF05

- O artigo só pode ser submetido se o evento estiver aberto para submissões.
- É obrigatório informar título, resumo e pelo menos uma área temática.
- Coautores informados precisam já estar cadastrados no sistema.

#### Classes principais do RF05

- `Paper`: entidade que representa o artigo.
- `PaperStatus`: enum com os estados possíveis do artigo.
- `SubmitPaperUseCase`: executa a submissão de um novo artigo.
- `ListAuthorPapersUseCase`: lista os artigos de um autor.
- `ListEventPapersUseCase`: lista os artigos submetidos a um evento.
- `PaperGateway`: interface de persistência do artigo.

---

### 7.3 RF08 - Dashboard do evento

O RF08 apresenta um resumo do andamento do evento: quantidade de artigos submetidos, quantidade avaliada e quantidade pendente de avaliação.

O `DashboardUseCase` consulta os artigos persistidos e retorna um `DashboardData`, um `record` que agrupa os totais calculados. A escolha por um `record` (em vez de uma classe DTO tradicional) segue a decisão da equipe de manter a estrutura de dados o mais enxuta possível, já que ele só carrega os totais já calculados, sem lógica própria.

#### Classes principais do RF08

- `DashboardUseCase`: consulta os artigos e calcula os totais do evento.
- `DashboardData`: record com os dados do resumo (total submetido, total avaliado e total pendente).

---

### 7.4 RF10 - Filtro de artigos (requisito adicional)

O RF10 é o requisito adicional definido pela equipe. Ele permite consultar artigos submetidos combinando múltiplos critérios de busca ao mesmo tempo: área temática, status e autor.

#### Padrão GoF utilizado: Chain of Responsibility

O **Chain of Responsibility** foi escolhido porque cada critério de filtro é independente dos demais e pode ser combinado em cadeia. Cada filtro recebe a lista de artigos já reduzida pelo filtro anterior e aplica sua própria regra, sem conhecer os outros filtros presentes na cadeia.

```text
PaperFilter
 ├── TopicFilter   → filtra por área temática
 ├── StatusFilter  → filtra por status
 └── AuthorFilter  → filtra por autor
```

Exemplo de uso, filtrando artigos de uma área temática que ainda estão com status `Submitted`:

```java
PaperFilter chain = new TopicFilter(topic);
chain.linkWith(new StatusFilter("Submitted"));

List<Paper> filtered = chain.apply(papers);
```

Essa estrutura permite adicionar novos critérios de filtro no futuro sem alterar os filtros já existentes, respeitando o princípio Aberto/Fechado.

#### Classes principais do RF10

- `PaperFilter`: handler abstrato da cadeia de filtros.
- `TopicFilter`, `StatusFilter`, `AuthorFilter`: filtros concretos.
- `FilterPapersUseCase`: monta e executa a cadeia de filtros sobre a lista de artigos.

---

## 8. Persistência

A persistência dos módulos do sistema é feita através de interfaces de gateway (Dependency Inversion Principle), com implementações concretas em memória para a versão atual (`InMemoryUserGateway`, `InMemoryPaperGateway`, `InMemoryReviewAssignmentGateway`, `InMemoryEmailGateway`). Os dados são mantidos enquanto o programa está em execução e são perdidos ao encerrar a aplicação.

A troca por uma persistência em banco de dados (ex: SQLite) é possível sem alterar os casos de uso, bastando implementar as interfaces de gateway já existentes.

---

## 9. Princípios de projeto considerados

### Single Responsibility Principle

Os casos de uso foram mantidos focados nas regras de negócio. Validações de entrada, como campos vazios ou formato visual de e-mail, serão tratadas futuramente na camada de interface. Isso evita que os casos de uso acumulem responsabilidades de formulário.

### Open/Closed Principle

A estrutura permite extensão sem mudanças grandes no código existente. É possível adicionar novos criadores de usuário, novos componentes temáticos, novos observadores do comitê e novos filtros de artigo sem reescrever a lógica principal.

### Dependency Inversion Principle

Os casos de uso dependem de interfaces de gateway, como `UserGateway`, `TopicPersistence`, `CommitteePersistence`, `PaperGateway` e `ReviewAssignmentGateway`. As implementações concretas (atualmente em memória) ficam isoladas na camada de infraestrutura.

---

## 10. Diagramas de classe

**Diagrama geral:** Inserir o diagrama de classes completo após a integração de todos os módulos.

**Diagrama do módulo Usuários e Comitê:** Inserir diagrama com Factory Method, Composite e Observer destacados.

**Diagrama do módulo Início, Submissão e Dashboard:** Inserir diagrama com State e Chain of Responsibility destacados.

---

## 11. Seções reservadas para os demais módulos

| Módulo | Requisitos | Responsável | Padrões | Descrição a adicionar |
|---|---|---|---|---|
| Avaliação e notificações | RF06, RF07, RF09 | Preencher | Preencher | Adicionar explicação das classes, fluxo e simulação. |

---

## 12. Próximos passos

- Integrar a documentação dos demais integrantes.
- Avaliar a substituição da persistência em memória por um banco de dados, se necessário.
- Adicionar diagrama de classes com os padrões destacados.
- Adicionar tutorial final de execução após a revisão de todas as telas.
- Revisar o `README.md` final para deixar a entrega clara e objetiva.