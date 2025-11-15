# Changelog

## [1.0.0] - 2025-11-15 - MVP Completo

Esta versão representa o MVP (Minimum Viable Product) completo do Sistema de Controle de Despesas, atendendo a todos os requisitos do trabalho B4T01.

### Adicionado
- **Implementação Completa das Funcionalidades:**
  - Cadastro, edição, exclusão e listagem de despesas.
  - Registro de pagamentos totais e parciais.
  - Gerenciamento completo de tipos de despesa.
  - Gerenciamento completo de usuários com senhas criptografadas.
- **Aplicação de Conceitos de POO:**
  - Herança (`Despesa` -> `DespesaTransporte`, etc.).
  - Polimorfismo (sobrescrita de métodos e uso de interfaces).
  - Encapsulamento em todas as classes de modelo.
  - Abstração com a classe `Despesa`.
- **Persistência em Arquivos:**
  - Todas as entidades (`Despesa`, `Usuario`, `TipoDespesa`) são salvas em arquivos de texto no diretório `data/`.
- **Segurança:**
  - Criptografia de senhas de usuário com SHA-256.
- **Documentação Completa:**
  - `README.md` detalhado com estrutura, como executar, e conceitos de POO.
  - Javadoc básico nas classes e métodos.

---

## [0.1.0] - 2025-11-15 - Arquitetura e POC

Esta versão foca no planejamento da arquitetura, separação de prioridades e na criação de uma Prova de Conceito (POC).

### Adicionado
- **Definição da Arquitetura:**
  - Estrutura de pacotes (`models`, `services`, `utils`, `interfaces`).
  - Definição das classes principais e seus relacionamentos.
- **Prova de Conceito (POC):**
  - Implementação da funcionalidade de "Gerenciar Tipos de Despesa" como prova de conceito.
  - Validação do fluxo de leitura e escrita em arquivo (`FileManager`).
- **Planejamento do MVP:**
  - Definição do escopo do MVP, priorizando as funcionalidades essenciais de despesas e pagamentos.

---

## [0.0.1] - 2025-11-15 - Estrutura Inicial e Menu

Este é o primeiro commit do projeto, atendendo aos requisitos da entrega B4T01.1.

### Adicionado
- **Criação do Repositório:**
  - Repositório Git inicializado.
- **Estrutura de Diretórios:**
  - Criação da estrutura de pastas `/src`, `/docs`, `/data`.
- **Menu Principal:**
  - Implementação da classe `Main` com um menu de console funcional.
  - Cada opção do menu exibe uma mensagem `System.out.println` indicando a funcionalidade (conforme solicitado no primeiro commit).
- **Documentação Inicial:**
  - Criação do arquivo `docs/README.md` com a descrição inicial do projeto.
  - Criação deste arquivo `docs/CHANGELOG.md`.
