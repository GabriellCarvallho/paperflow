# PaperFlow

**Documentação do projeto**
`README.md` e documentação final do projeto.

**Equipe:** Gabriel Pereira de Carvalho, Felipe Oliveira Raimundo, Daniel Alves da Silva
**Disciplina:** Padrões de Projeto de Software
**Professor:** Alex Sandro da Cunha Rêgo
**Curso:** CST em Sistemas para Internet — IFPB
**Repositório:** https://github.com/GabriellCarvallho/paperflow

---

## 1. Visão geral do projeto

PaperFlow é um sistema para gerenciamento do fluxo de submissão e avaliação de artigos científicos em eventos acadêmicos. A proposta é permitir o cadastro de usuários, submissão de artigos, organização de áreas temáticas, formação de comitê técnico, avaliação de trabalhos e geração de informações de acompanhamento do evento.

**Descrição completa do projeto:** O PaperFlow cobre todo o ciclo de um evento acadêmico de submissão de artigos. Um coordenador cria o evento, define suas áreas temáticas e monta o comitê técnico convidando pesquisadores já cadastrados. Uma vez iniciado, o evento passa a aceitar submissões: autores enviam artigos vinculados a uma ou mais áreas temáticas, podendo indicar coautores. Os artigos são então distribuídos aos revisores do comitê, que os avaliam e emitem um parecer (aceito ou rejeitado). Os autores são notificados por e-mail sobre o resultado da avaliação, e o coordenador acompanha o andamento de tudo isso por um dashboard com o resumo do evento, além de poder consultar os artigos submetidos através de filtros combinados por área temática, status e autor.

---

## 2. Tecnologias e organização inicial

- **Linguagem:** Java.
- **Gerenciamento do projeto:** Maven (com Maven Wrapper — `./mvnw`).
- **Persistência atual:** em memória, através de gateways (`InMemoryUserGateway`, `InMemoryPaperGateway`, `InMemoryReviewAssignmentGateway`, `InMemoryEmailGateway`), com previsão de substituição por persistência em banco de dados.
- **Interface atual:** console, com menu interativo de texto.
- **Organização arquitetural:** separação em camadas inspirada na Arquitetura Limpa.

**Observação sobre persistência:** os dados atualmente não são persistidos entre execuções — cada gateway guarda as informações em memória enquanto o programa está em execução. A troca por uma implementação em banco de dados é possível sem alterar os casos de uso, já que eles dependem apenas das interfaces de gateway (Dependency Inversion Principle).

---

## 3. Organização por camadas

| Camada/Pacote | Responsabilidade |
|---|---|
| `domain` | Entidades e regras centrais do domínio, como `User`, `ThematicArea`, `Event`, `Paper`. |
| `application` | Casos de uso, fábricas, interfaces de gateway e filtros da aplicação. |
| `infrastructure` | Implementações técnicas, como os gateways em memória e o envio de e-mail. |
| `presentation` | Interface em modo console do sistema (menus e leitura/escrita de texto). |

---

## 4. Como executar o projeto

O projeto inclui o Maven Wrapper, então não é necessário ter o Maven instalado para executá-lo — apenas o Java (JDK 17 ou superior).

### Passo a passo

1. Clonar ou baixar o projeto do GitHub:
   ```bash
   git clone https://github.com/GabriellCarvallho/paperflow.git
   cd paperflow
   ```

2. Compilar o projeto:

   No Linux/Mac:
   ```bash
   ./mvnw clean compile
   ```

   No Windows:
   ```cmd
   mvnw.cmd clean compile
   ```

3. Executar o sistema:

   No Linux/Mac:
   ```bash
   ./mvnw exec:java -Dexec.mainClass="com.system.paperflow.Main"
   ```

   No Windows:
   ```cmd
   mvnw.cmd exec:java -Dexec.mainClass="com.system.paperflow.Main"
   ```

4. O sistema abre em modo console, com um menu interativo guiando o usuário pelas funcionalidades disponíveis (cadastro, login, gestão de eventos, submissão de artigos, avaliação, dashboard, etc).

Não é necessário instalar nenhum banco de dados: os dados são mantidos em memória durante a execução, e um coordenador padrão é criado automaticamente para permitir testar o sistema sem cadastro prévio (ver seção 6.1).

### Alternativa: executar pela IDE

1. Abrir o projeto na IDE (IntelliJ, Eclipse ou VS Code com extensão Java) pela pasta que contém o `pom.xml`.
2. Aguardar a IDE carregar as dependências Maven.
3. Executar a classe `src/main/java/com/system/paperflow/Main.java`.

### Fluxo de uso do sistema

Ao iniciar, o sistema cria automaticamente um coordenador padrão e exibe suas credenciais na tela — não é necessário cadastrar um coordenador manualmente para testar o sistema.

**Menu inicial:**
```text
1. Entrar
2. Cadastrar pesquisador
3. Ver credenciais do coordenador
4. Sair
```

**Fluxo sugerido para testar o sistema de ponta a ponta:**

1. **Entrar como coordenador** (opção 3 para ver as credenciais, depois opção 1 para logar).
2. No **menu do coordenador**, nessa ordem:
   - `Criar novo evento` — informa nome, cidade, data final, prazo de submissão e categoria.
   - `Cadastrar área temática` — cadastra ao menos uma área para o evento.
   - `Adicionar revisor ao comitê` — informa o e-mail de um pesquisador já cadastrado e suas áreas de expertise (é necessário ao menos um pesquisador cadastrado antes deste passo — ver item 3 abaixo).
   - `Iniciar recebimento de submissões` — ativa o evento (só é possível com ao menos uma área temática e um revisor cadastrados).
3. **Sair da conta** e **cadastrar um pesquisador** (menu inicial, opção 2) para atuar como autor/revisor.
4. **Entrar como pesquisador** e, no **menu do pesquisador**:
   - `Submeter artigo` — informa título, resumo, áreas e coautores (opcional).
   - `Meus artigos` — lista os artigos submetidos e, quando avaliados, os pareceres recebidos.
5. **Voltar como coordenador** para:
   - `Distribuir artigos para revisão` — distribui automaticamente os artigos submetidos entre os revisores do comitê.
   - `Listar artigos do evento` — consulta os artigos submetidos.
   - `Dashboard` — visualiza o resumo do evento (submetidos, revisores, avaliados, pendentes).
6. **Entrar novamente como o pesquisador que é revisor** para:
   - `Minhas revisões` — lista as atribuições de revisão recebidas.
   - `Concluir revisão` — registra contribuições, críticas e o veredito de uma revisão pendente.
7. Em qualquer momento, a opção `Ver emails registrados` (disponível nos dois menus) mostra as notificações que o sistema enviaria aos autores sobre o resultado das avaliações.

---

## 5. Divisão da equipe e módulos

A tabela abaixo serve como esqueleto para a documentação final. Cada integrante poderá preencher o módulo que desenvolveu, os requisitos atendidos e os padrões utilizados.

| Integrante | Módulo/Requisitos | Padrões GoF utilizados | Status |
|---|---|---|---|
| Felipe Oliveira Raimundo | RF02 - Cadastro de usuários; RF03 - Áreas temáticas; RF04 - Comitê técnico | Factory Method; Composite; Observer | Implementado em versão funcional inicial |
| Gabriel Pereira de Carvalho | RF01 - Início do evento; RF05 - Submissão e ciclo de vida do artigo; RF08 - Dashboard; RF10 - Filtro de artigos (adicional) | State; Chain of Responsibility | Implementado em versão funcional inicial |
| Daniel Alves da Silva | RF06 - Distribuição de artigos; RF07 - Avaliação; RF09 - Notificação de autores | Adicionar padrões utilizados | Adicionar status |

---

## 6. Módulo de Usuários, Áreas Temáticas e Comitê Técnico

Este módulo contempla os requisitos **RF02, RF03 e RF04**. Eles foram implementados em sequência porque um requisito prepara a base para o próximo: primeiro o sistema precisa cadastrar usuários, depois o coordenador cadastra as áreas temáticas do evento, e por fim o coordenador adiciona revisores ao comitê técnico a partir de usuários já cadastrados.

```text
RF02 - Cadastro de usuários
        ↓
RF03 - Cadastro de áreas temáticas
        ↓
RF04 - Comitê técnico de avaliação
```

---

### 6.1 RF02 - Cadastro de usuários

O RF02 permite cadastrar pesquisadores no sistema informando **e-mail, senha e instituição**. O e-mail é tratado como chave do usuário, portanto a principal regra de negócio é impedir o cadastro de dois usuários com o mesmo e-mail.

Todo usuário cadastrado por este fluxo entra como `Researcher`. Um pesquisador passa a atuar também como revisor quando é adicionado ao comitê técnico de um evento (RF04).

#### Padrão utilizado: Factory Method

O **Factory Method** foi utilizado para centralizar a criação dos usuários. Em vez de instanciar diretamente nos casos de uso, a criação é delegada a uma classe criadora dedicada, o que facilita adicionar variações futuras sem alterar o fluxo de cadastro.

```text
ResearcherFactory → cria Researcher
CoordinatorFactory → cria o coordenador padrão do sistema
```

#### Classes principais do RF02

- `ResearcherFactory`, `CoordinatorFactory`: fábricas responsáveis pela criação dos usuários.
- `RegisterUserUseCase`: executa o cadastro de um novo pesquisador.
- `LoginUserUseCase`: autentica um usuário já cadastrado.
- `UserGateway`: interface usada pelos casos de uso para persistência de usuários.
- `InMemoryUserGateway`: implementação em memória, usada na versão atual do projeto.

#### Coordenador padrão

Para permitir testar o sistema sem exigir um cadastro manual, o sistema cria automaticamente um coordenador padrão ao iniciar, caso ele ainda não exista.

| Campo | Valor |
|---|---|
| E-mail | coordenador@paperflow.com |
| Senha | admin123 |
| Tipo | Coordinator |

---

### 6.2 RF03 - Cadastro de áreas temáticas
#### Padrão utilizado: Composite

O RF03 permite que o coordenador cadastre as áreas temáticas de um evento. Cada área é uma palavra-chave simples, usada posteriormente pelos autores ao submeter artigos (RF05) e pelos revisores para indicar suas expertises (RF04).

#### Regras de negócio do RF03

- Somente o coordenador do evento pode cadastrar áreas temáticas.
- As áreas cadastradas ficam vinculadas ao evento atual.
- Um evento precisa ter ao menos uma área temática cadastrada antes de ser iniciado (ver RF01).

#### Classes principais do RF03

- `ThematicArea`: entidade que representa uma área temática do evento.
- `CreateThematicAreaUseCase`: cadastra uma nova área temática vinculada ao evento atual.

---

### 6.3 RF04 - Comitê técnico de avaliação

#### Padrão utilizado: Observer

O RF04 permite que o coordenador monte o comitê técnico do evento, adicionando pesquisadores já cadastrados como revisores e associando a cada um suas áreas de expertise.

```text
Coordenador informa e-mail de um pesquisador cadastrado
        ↓
Informa as áreas de expertise do revisor
        ↓
Pesquisador é adicionado ao comitê do evento
```

#### Regras de negócio do RF04

- O usuário a ser adicionado precisa já estar cadastrado no sistema (RF02).
- É necessário informar ao menos uma área de expertise para o revisor.
- As áreas de expertise informadas precisam estar entre as áreas temáticas já cadastradas no evento (RF03).
- Um evento precisa ter ao menos um revisor no comitê antes de ser iniciado (ver RF01).

#### Classes principais do RF04

- `AddReviewerUseCase`: adiciona um pesquisador cadastrado ao comitê técnico do evento, com suas áreas de expertise.

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

#### Padrão utilizado: State

O **State** foi aplicado porque o artigo (`Paper`) muda de comportamento conforme seu estado muda, e cada estado permite (ou não) determinadas transições.


#### Regras de negócio do RF05

- O artigo só pode ser submetido se o evento estiver aberto para submissões.
- É obrigatório informar título, resumo e pelo menos uma área temática.
- Coautores informados precisam já estar cadastrados no sistema.

#### Classes principais do RF05

- `Paper`: entidade que representa o artigo (contexto do State).
- Classes de estado do artigo (conferir nomes atuais no código).
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

A estrutura permite extensão sem mudanças grandes no código existente. É possível adicionar novas fábricas de usuário e novos filtros de artigo sem reescrever a lógica principal.

### Dependency Inversion Principle

Os casos de uso dependem de interfaces de gateway, como `UserGateway`, `TopicPersistence`, `CommitteePersistence`, `PaperGateway` e `ReviewAssignmentGateway`. As implementações concretas (atualmente em memória) ficam isoladas na camada de infraestrutura.

---

## 10. Diagramas de classe

**Diagrama geral:** Inserir o diagrama de classes completo após a integração de todos os módulos.

**Diagrama do módulo Usuários e Comitê:** Inserir diagrama com Factory Method destacado.

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
- Adicionar tutorial final de execução após a revisão completa do fluxo do sistema.
- Revisar o `README.md` final para deixar a entrega clara e objetiva.
