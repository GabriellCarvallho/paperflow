# PaperFlow

**Equipe:** Daniel Lucas Alves da Silva, Gabriel Pereira de Carvalho, Felipe Oliveira Raimundo  
**Disciplina:** Padrões de Projeto de Software  
**Professor:** Alex Sandro da Cunha Rêgo  
**Curso:** CST em Sistemas para Internet — IFPB  
**Repositório:** https://github.com/GabriellCarvallho/paperflow

---

## 1. Visão geral do projeto

PaperFlow é um sistema para gerenciamento do fluxo de submissão e avaliação de artigos científicos em eventos acadêmicos. A proposta é permitir o cadastro de usuários, submissão de artigos, organização de áreas temáticas, formação de comitê técnico, avaliação de trabalhos e geração de informações para acompanhamento do evento.

O **PaperFlow** é uma aplicação desenvolvida em **Java** para execução em **Terminal/Console**. O sistema cobre todo o ciclo de um evento acadêmico de submissão de artigos. Um coordenador cria o evento, define suas áreas temáticas e monta o comitê técnico convidando pesquisadores já cadastrados. Uma vez iniciado, o evento passa a aceitar submissões: autores enviam artigos vinculados a uma ou mais áreas temáticas, podendo indicar coautores. Os artigos são então distribuídos aos revisores do comitê, que os avaliam e emitem um parecer (aceito ou rejeitado). Os autores são notificados por e-mail sobre o resultado da avaliação, enquanto o coordenador acompanha todo o andamento do evento por meio de um dashboard com informações resumidas, além de poder consultar os artigos submetidos.

---

## 2. Arquitetura

O PaperFlow foi desenvolvido seguindo os princípios da **Arquitetura Limpa (Clean Architecture)**, buscando manter as regras de negócio independentes dos detalhes de implementação. Dessa forma, o domínio da aplicação permanece protegido de mudanças na interface de usuário, na persistência de dados ou em serviços externos.

A arquitetura foi organizada em camadas, cada uma com responsabilidades bem definidas, reduzindo o acoplamento entre os módulos e facilitando a manutenção, os testes e a evolução do sistema.

```text
src/main/java/com/system/paperflow
├── application
│   ├── dto
│   ├── factory
│   ├── command
│   ├── event
│   ├── gateway
│   ├── observer
│   ├── strategy
│   └── usecase
│
├── domain
│   ├── entity
│   ├── dashboard
│   ├── state
│   └── template
│
├── infrastructure
│   └── gateway
│
├── presentation
│   └── console
│
└── Main.java
```

### Responsabilidade das camadas

| Camada | Responsabilidade |
|---------|------------------|
| `domain` | Contém as entidades e as regras de negócio da aplicação. É o núcleo do sistema e não depende das demais camadas. |
| `application` | Implementa os casos de uso da aplicação, coordenando as operações do sistema por meio de serviços, estratégias, comandos, eventos e interfaces (gateways). |
| `infrastructure` | Contém as implementações técnicas, como gateways e integrações com serviços externos, implementando os contratos definidos pelas camadas internas. |
| `presentation` | Responsável pela interface em modo console, realizando a interação com o usuário e acionando os casos de uso da aplicação. |

### Fluxo de dependências

Na Arquitetura Limpa, as dependências sempre apontam para as camadas mais internas. Assim, a infraestrutura e a interface de usuário dependem da aplicação e do domínio, enquanto o domínio permanece totalmente independente.

```text
             Presentation
                   │
                   ▼
             Application
                   │
                   ▼
               Domain
                   ▲
                   │
           Infrastructure
```

---

## 3. Como executar o projeto

O projeto inclui o **Maven Wrapper**, portanto não é necessário ter o Maven instalado. É preciso apenas possuir o **Java JDK 17** (ou superior).

### Passo a passo

1. Clone o repositório:

   ```bash
   git clone https://github.com/GabriellCarvallho/paperflow.git
   cd paperflow
   ```

2. Compile o projeto.

   **Linux/macOS:**

   ```bash
   ./mvnw clean compile
   ```

   **Windows:**

   ```cmd
   mvnw.cmd clean compile
   ```

3. Configure as variáveis de ambiente.

   Crie um arquivo chamado `.env`, copie o conteúdo de `.env-example` e preencha as informações do SMTP do Gmail e a respectiva **Senha de App** da conta Google.

4. Execute o sistema.

   **Linux/macOS:**

   ```bash
   ./mvnw exec:java -Dexec.mainClass="com.system.paperflow.Main"
   ```

   **Windows:**

   ```cmd
   mvnw.cmd exec:java -Dexec.mainClass="com.system.paperflow.Main"
   ```

---

## 4. Divisão da equipe e módulos

A tabela abaixo apresenta a divisão das funcionalidades implementadas por cada integrante da equipe.

| Integrante | Módulo/Requisitos | Status |
|------------|-------------------|--------|
| Felipe Oliveira Raimundo | RF02 - Cadastro de usuários; RF03 - Áreas temáticas; RF04 - Comitê técnico | Concluído |
| Gabriel Pereira de Carvalho | RF01 - Início do evento; RF05 - Submissão e ciclo de vida do artigo; RF08 - Dashboard; RF10 - Filtro de artigos (adicional) | Concluído |
| Daniel Lucas Alves da Silva | RF06 - Distribuição de artigos; RF07 - Avaliação; RF09 - Notificação de autores | Concluído |

---

## 5. Observações

Para facilitar os testes, o sistema cria automaticamente um coordenador padrão durante a inicialização.

| Campo | Valor |
|-------|-------|
| E-mail | coordenador@paperflow.com |
| Senha | admin123 |
