# PaperFlow

**Documentação parcial do módulo de Usuários, Áreas Temáticas e Comitê Técnico**  
Base para `README.md` e documentação final do projeto.

**Equipe:** Adicionar nomes dos integrantes.  
**Disciplina:** Padrões de Projeto de Software.  
**Professor:** Adicionar nome do professor.  
**Repositório:** Adicionar link do GitHub.  
**Branch atual:** Adicionar nome da branch ou branch final de entrega.

---

## 1. Visão geral do projeto

PaperFlow é um sistema para gerenciamento do fluxo de submissão e avaliação de artigos científicos em eventos acadêmicos. A proposta é permitir o cadastro de usuários, submissão de artigos, organização de áreas temáticas, formação de comitê técnico, avaliação de trabalhos e geração de informações de acompanhamento do evento.

Esta documentação ainda é parcial. Neste momento, ela descreve apenas o módulo de Usuários, Áreas Temáticas e Comitê Técnico. As demais partes do grupo podem ser adicionadas posteriormente nas seções reservadas.

**Descrição completa do projeto:** Adicionar uma descrição geral final depois que todos os módulos estiverem integrados.

---

## 2. Tecnologias e organização inicial

- **Linguagem:** Java.
- **Gerenciamento do projeto:** Maven.
- **Persistência local:** SQLite, criado automaticamente durante a execução.
- **Interface atual:** simulação pelo console, com previsão de telas em Swing.
- **Organização arquitetural:** separação em camadas inspirada na Arquitetura Limpa.

**Observação sobre persistência:** as classes responsáveis por salvar dados no SQLite continuam existindo na camada de infraestrutura, mas não serão apresentadas como implementação do padrão GoF Adapter. Após a revisão da equipe, essa parte será descrita apenas como persistência concreta em SQLite.

---

## 3. Organização por camadas

| Camada/Pacote | Responsabilidade |
|---|---|
| `domain` | Entidades e regras centrais do domínio, como `User`, `Topic`, `TopicGroup` e `CommitteeInvitation`. |
| `application` | Casos de uso, fábricas, interfaces de persistência e eventos/observadores da aplicação. |
| `infrastructure` | Implementações técnicas, como persistência com SQLite. |
| `presentation` | Demonstrações em console e, futuramente, telas Swing. |

---

## 4. Como executar o projeto

Esta seção deve ser revisada no final da integração do grupo. A ideia é que o professor consiga baixar o projeto, abrir na IDE e executar o `Main.java` sem instalar SQLite manualmente.

**Passo a passo final:** Adicionar instruções definitivas depois que todas as telas e módulos forem integrados.

1. Clonar ou baixar o projeto do GitHub.
2. Abrir o projeto na IDE pela pasta que contém o `pom.xml`.
3. Aguardar a IDE carregar as dependências Maven.
4. Executar `src/main/java/com/system/paperflow/Main.java`.

---

## 5. Divisão da equipe e módulos

A tabela abaixo serve como esqueleto para a documentação final. Cada integrante poderá preencher o módulo que desenvolveu, os requisitos atendidos e os padrões utilizados.

| Integrante | Módulo/Requisitos | Padrões GoF utilizados | Status |
|---|---|---|---|
| Felipe Oliveira Raimundo | RF02 - Cadastro de usuários; RF03 - Áreas temáticas; RF04 - Comitê técnico | Factory Method; Composite; Observer | Implementado em versão funcional inicial |
| Integrante 2 | Adicionar requisitos desenvolvidos | Adicionar padrões utilizados | Adicionar status |
| Integrante 3 | Adicionar requisitos desenvolvidos | Adicionar padrões utilizados | Adicionar status |


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
- `UserPersistence`: interface usada pelos casos de uso para persistência de usuários.
- Classes de persistência SQLite renomeadas no projeto: implementam o salvamento e a consulta de usuários na infraestrutura.

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
- Classe de persistência SQLite renomeada no projeto: salva e reconstrói a árvore de áreas temáticas.
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
- Classe de persistência SQLite renomeada no projeto: salva convites e expertises dos revisores.
- `RF04CommitteeDemo`: simulação em console do RF04.

---

## 7. Persistência com SQLite

A persistência dos módulos RF02, RF03 e RF04 foi implementada com SQLite para facilitar a execução local. O banco é criado automaticamente, assim como as tabelas necessárias para usuários, áreas temáticas, convites e expertises dos revisores.

---

## 8. Princípios de projeto considerados

### Single Responsibility Principle

Os casos de uso foram mantidos focados nas regras de negócio. Validações de entrada, como campos vazios ou formato visual de e-mail, serão tratadas futuramente na camada de interface. Isso evita que os casos de uso acumulem responsabilidades de formulário.

### Open/Closed Principle

A estrutura permite extensão sem mudanças grandes no código existente. É possível adicionar novos criadores de usuário, novos componentes temáticos e novos observadores do comitê sem reescrever a lógica principal.

### Dependency Inversion Principle

Os casos de uso dependem de interfaces como `UserPersistence`, `TopicPersistence` e `CommitteePersistence`. As classes concretas de SQLite ficam na infraestrutura.

---

## 9. Diagramas de classe

**Diagrama geral:** Inserir o diagrama de classes completo após a integração de todos os módulos.

**Diagrama do módulo Usuários e Comitê:** Inserir diagrama com Factory Method, Composite e Observer destacados.

---

## 10. Seções reservadas para os demais módulos

| Módulo | Requisitos | Responsável | Padrões | Descrição a adicionar |
|---|---|---|---|---|
| Start, submissão e dashboard | RF01, RF05, RF08 | Preencher | Preencher | Adicionar explicação das classes, fluxo e simulação. |
| Avaliação e notificações | RF06, RF07, RF09 | Preencher | Preencher | Adicionar explicação das classes, fluxo e simulação. |
| RF10 | Requisito adicional | Preencher | Preencher | Adicionar quando a equipe definir o requisito e o padrão. |

---

## 11. Próximos passos

- Integrar a documentação dos demais integrantes.
- Atualizar nomes finais das classes de persistência SQLite, caso ainda mudem.
- Adicionar diagrama de classes com os padrões destacados.
- Adicionar tutorial final de execução após a integração das telas Swing.
- Revisar o `README.md` final para deixar a entrega clara e objetiva.
