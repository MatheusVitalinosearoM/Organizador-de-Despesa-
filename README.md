# Organizador-de-Despesa-
Organizador de Despesa com Java
# Sistema de Controle de Despesas

**Versão:** 1.0.0
**Autor:** Manus AI

---

## 1. Descrição do Projeto

Este é um sistema de console em Java para controle de despesas pessoais. Ele permite ao usuário gerenciar despesas, pagamentos, tipos de despesa e usuários, com todos os dados persistidos em arquivos de texto. O projeto foi desenvolvido seguindo os princípios da Programação Orientada a Objetos (POO), incluindo conceitos como abstração, herança, polimorfismo e encapsulamento.

---

## 2. Funcionalidades Principais

- **Gerenciamento de Despesas:**
  - Cadastro de despesas de diferentes tipos (Transporte, Eventual, Supérfluo).
  - Edição e exclusão de despesas.
  - Registro de pagamentos (parciais ou totais).

- **Listagem e Filtros:**
  - Listar despesas em aberto ou pagas.
  - Filtrar despesas por período (data de vencimento ou pagamento).

- **Gerenciamento de Tipos de Despesa:**
  - Criar, editar, listar e excluir categorias de despesa (ex: Alimentação, Moradia).

- **Gerenciamento de Usuários:**
  - Cadastrar, editar e listar usuários.
  - Sistema de autenticação (não implementado no menu principal, mas a base existe).
  - Senhas armazenadas de forma segura com criptografia SHA-256.

- **Persistência de Dados:**
  - Todas as informações são salvas em arquivos de texto no formato `CSV` (separado por `|`).

---

## 3. Tecnologias Utilizadas

- **Linguagem:** Java 11 ou superior
- **Paradigma:** Programação Orientada a Objetos (POO)
- **Persistência:** Arquivos de texto (`.txt`)
- **Criptografia:** SHA-256 para senhas

---

## 4. Estrutura do Projeto

O projeto está organizado da seguinte forma para garantir a separação de responsabilidades:

```
sistema-despesas/
├── .gitignore         # Arquivos e diretórios a serem ignorados pelo Git
├── README.md          # Este arquivo
├── data/                # Diretório para arquivos de dados
│   ├── despesas.txt
│   ├── tipos_despesa.txt
│   └── usuarios.txt
├── docs/                # Diretório para documentação
│   ├── README.md
│   └── CHANGELOG.md
└── src/                 # Código-fonte do projeto
    ├── Main.java        # Classe principal que executa o sistema
    ├── interfaces/      # Contratos (interfaces) do sistema
    │   └── Pagavel.java
    ├── models/          # Classes de domínio (entidades)
    │   ├── Despesa.java
    │   ├── DespesaEventual.java
    │   ├── DespesaSuperfluo.java
    │   ├── DespesaTransporte.java
    │   ├── TipoDespesa.java
    │   └── Usuario.java
    ├── services/        # Classes de serviço (regras de negócio)
    │   ├── DespesaService.java
    │   ├── TipoDespesaService.java
    │   └── UsuarioService.java
    └── utils/           # Classes utilitárias
        ├── CriptografiaUtil.java
        └── FileManager.java
```

---

## 5. Como Compilar e Executar

### **Pré-requisitos:**
- JDK (Java Development Kit) versão 11 ou superior instalado e configurado no `PATH` do sistema.

### **Passos para Execução:**

1. **Navegue até o diretório `src`** do projeto via terminal:
   ```sh
   cd /caminho/para/o/projeto/sistema-despesas/src
   ```

2. **Compile todos os arquivos `.java`**:
   ```sh
   javac */*.java Main.java
   ```
   Este comando compila os arquivos de todos os subdiretórios (`models`, `services`, etc.) e a classe `Main`.

3. **Execute a classe principal `Main`**:
   ```sh
   java Main
   ```

4. O sistema será iniciado e o menu principal será exibido no console.

---

## 6. Documentação das Classes

### **6.1. Pacote `models`**

| Classe | Descrição |
| --- | --- |
| `Despesa` | **Classe abstrata** que serve como base para todas as despesas. Define atributos e comportamentos comuns. |
| `DespesaTransporte` | Subclasse de `Despesa` para gastos com transporte. Sobrescreve `calcularMultaAtraso`. |
| `DespesaEventual` | Subclasse de `Despesa` para gastos eventuais. |
| `DespesaSuperfluo` | Subclasse de `Despesa` para gastos não essenciais. |
| `TipoDespesa` | Representa uma categoria de despesa (ex: Alimentação). |
| `Usuario` | Representa um usuário do sistema, com login e senha criptografada. |

### **6.2. Pacote `interfaces`**

| Interface | Descrição |
| --- | --- |
| `Pagavel` | Define um contrato para objetos que podem ser pagos, como `Despesa`. Garante que a classe implemente métodos para registrar pagamentos e verificar o status. |

### **6.3. Pacote `services`**

| Classe | Descrição |
| --- | --- |
| `DespesaService` | Gerencia a lógica de negócio para despesas (CRUD, filtros, etc.). |
| `TipoDespesaService` | Gerencia a lógica de negócio para os tipos de despesa. |
| `UsuarioService` | Gerencia a lógica de negócio para usuários, incluindo cadastro e autenticação. |

### **6.4. Pacote `utils`**

| Classe | Descrição |
| --- | --- |
| `FileManager` | Classe utilitária com **métodos estáticos** para ler e escrever em arquivos de texto. |
| `CriptografiaUtil` | Classe utilitária com **métodos estáticos** para criptografar senhas com SHA-256. |

### **6.5. Classe Principal**

| Classe | Descrição |
| --- | --- |
| `Main` | Ponto de entrada do programa. Contém o menu de console e orquestra as chamadas para os serviços. |

---

## 7. Conceitos de POO Aplicados

- **Abstração:** A classe `Despesa` é abstrata, definindo um modelo genérico de despesa sem ser instanciável diretamente. Ela esconde a complexidade e expõe apenas funcionalidades essenciais.

- **Herança:** As classes `DespesaTransporte`, `DespesaEventual` e `DespesaSuperfluo` herdam de `Despesa`, reutilizando código e estendendo funcionalidades específicas.

- **Polimorfismo:**
  - **Sobrescrita:** Métodos como `calcularMultaAtraso()` e `getCategoria()` são sobrescritos nas subclasses para fornecer implementações específicas.
  - **Interfaces:** A `DespesaService` trabalha com objetos do tipo `Despesa`, mas pode manipular instâncias de qualquer uma de suas subclasses (`DespesaTransporte`, etc.) de forma transparente.

- **Encapsulamento:** Os atributos das classes são privados (`private`) e acessados através de métodos públicos (`getters` e `setters`), protegendo a integridade dos dados.

- **Sobrecarga de Construtores:** Classes como `Despesa` e `TipoDespesa` possuem múltiplos construtores com diferentes parâmetros, oferecendo flexibilidade na criação de objetos.

- **Atributos e Métodos Estáticos:**
  - `CriptografiaUtil` e `FileManager` usam métodos estáticos, pois suas funcionalidades não dependem de um estado de objeto.
  - Classes de modelo usam atributos estáticos (`proximoId`) para controlar a geração de IDs únicos globalmente.

---

## 8. Persistência de Dados

Os dados são armazenados no diretório `data/` em arquivos de texto. O caractere `|` é usado como delimitador.

- **`usuarios.txt`:** `id|login|senhaCriptografada|nome|ativo`
- **`tipos_despesa.txt`:** `id|nome|descricao`
- **`despesas.txt`:** `id|descricao|valor|vencimento|tipoId|pago|dataPagamento|valorPago|categoria|...campos específicos...`

---

## 9. Segurança

As senhas dos usuários não são armazenadas em texto plano. Elas são criptografadas usando o algoritmo **SHA-256** através da classe `CriptografiaUtil`. Ao fazer login, a senha fornecida é criptografada e comparada com o hash armazenado, garantindo que a senha original nunca seja exposta.
