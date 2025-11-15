import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import models.*;
import services.*;
import utils.*;


public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static DespesaService despesaService;
    private static TipoDespesaService tipoDespesaService;
    private static UsuarioService usuarioService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static void main(String[] args) {
        try (scanner) {
            System.out.println("=".repeat(60));
            System.out.println("    SISTEMA DE CONTROLE DE DESPESAS - v0.0.1");
            System.out.println("=".repeat(60));
            
            
            FileManager.inicializarDiretorio();
            tipoDespesaService = new TipoDespesaService();
            despesaService = new DespesaService(tipoDespesaService);
            usuarioService = new UsuarioService();
            
            
            menuPrincipal();
            
            System.out.println("\nSistema encerrado. Até logo!");
        }
    }
    

    private static void menuPrincipal() {
        int opcao;
        
        do {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("              MENU PRINCIPAL");
            System.out.println("=".repeat(60));
            System.out.println("1. Entrar Despesa");
            System.out.println("2. Anotar Pagamento");
            System.out.println("3. Listar Despesas em Aberto no período");
            System.out.println("4. Listar Despesas Pagas no período");
            System.out.println("5. Gerenciar Tipos de Despesa");
            System.out.println("6. Gerenciar Usuários");
            System.out.println("7. Exibir Estatísticas");
            System.out.println("0. Sair");
            System.out.println("=".repeat(60));
            System.out.print("Escolha uma opção: ");
            
            opcao = lerInteiro();
            
            switch(opcao) {
                case 1 -> {
                    System.out.println("\n>>> Funcionalidade: Entrar Despesa");
                    entrarDespesa();
                }
                case 2 -> {
                    System.out.println("\n>>> Funcionalidade: Anotar Pagamento");
                    anotarPagamento();
                }
                case 3 -> {
                    System.out.println("\n>>> Funcionalidade: Listar Despesas em Aberto no período");
                    listarDespesasEmAberto();
                }
                case 4 -> {
                    System.out.println("\n>>> Funcionalidade: Listar Despesas Pagas no período");
                    listarDespesasPagas();
                }
                case 5 -> {
                    System.out.println("\n>>> Funcionalidade: Gerenciar Tipos de Despesa");
                    gerenciarTiposDespesa();
                }
                case 6 -> {
                    System.out.println("\n>>> Funcionalidade: Gerenciar Usuários");
                    gerenciarUsuarios();
                }
                case 7 -> {
                    System.out.println("\n>>> Funcionalidade: Exibir Estatísticas");
                    despesaService.exibirEstatisticas();
                }
                case 0 -> System.out.println("\nEncerrando sistema...");
                default -> System.out.println("\n❌ Opção inválida! Tente novamente.");
            }
        } while(opcao != 0);
    }
    

    private static void entrarDespesa() {
        System.out.println("\n--- CADASTRAR NOVA DESPESA ---");
        
   
        System.out.println("\nEscolha o tipo de despesa:");
        System.out.println("1. Transporte");
        System.out.println("2. Eventual");
        System.out.println("3. Supérfluo");
        System.out.print("Opção: ");
        int tipoDespesa = lerInteiro();
        
        scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Valor (R$): ");
        double valor = lerDouble();
        
        System.out.print("Data de vencimento (dd/MM/yyyy): ");
        scanner.nextLine(); 
        LocalDate vencimento = lerData();
        
    
        tipoDespesaService.exibirTodos();
        System.out.print("\nID do tipo de despesa: ");
        int tipoId = lerInteiro();
        TipoDespesa tipo = tipoDespesaService.buscarPorId(tipoId);
        
        Despesa despesa = null;
        
        switch(tipoDespesa) {
            case 1 -> { 
                scanner.nextLine();
                System.out.print("Veículo: ");
                String veiculo = scanner.nextLine();
                System.out.print("Quilometragem: ");
                double km = lerDouble();
                despesa = new DespesaTransporte(descricao, valor, vencimento, tipo, veiculo, km);
            }
                
            case 2 -> { 
                scanner.nextLine();
                System.out.print("Evento: ");
                String evento = scanner.nextLine();
                System.out.print("É recorrente? (S/N): ");
                boolean recorrente = scanner.nextLine().equalsIgnoreCase("S");
                despesa = new DespesaEventual(descricao, valor, vencimento, tipo, evento, recorrente);
            }
                
            case 3 -> { 
                scanner.nextLine();
                System.out.print("Categoria específica: ");
                String cat = scanner.nextLine();
                System.out.print("Prioridade (1-5, sendo 5 menos prioritário): ");
                int prioridade = lerInteiro();
                despesa = new DespesaSuperfluo(descricao, valor, vencimento, tipo, cat, prioridade);
            }
                
            default -> {
                System.out.println("❌ Tipo inválido!");
                return;
            }
        }
        
        if (despesaService.adicionarDespesa(despesa)) {
            System.out.println("\n✅ Despesa cadastrada com sucesso!");
            System.out.println(despesa);
        } else {
            System.out.println("\n❌ Erro ao cadastrar despesa.");
        }
    }
    
   
    private static void anotarPagamento() {
        System.out.println("\n--- ANOTAR PAGAMENTO ---");
        
        
        List<Despesa> emAberto = despesaService.listarEmAberto();
        if (emAberto.isEmpty()) {
            System.out.println("Nenhuma despesa em aberto.");
            return;
        }
        
        despesaService.exibirDespesas(emAberto);
        
        System.out.print("\nID da despesa: ");
        int id = lerInteiro();
        
        Despesa despesa = despesaService.buscarPorId(id);
        if (despesa == null) {
            System.out.println("❌ Despesa não encontrada!");
            return;
        }
        
        if (despesa.estaPago()) {
            System.out.println("❌ Despesa já está paga!");
            return;
        }
        
        System.out.printf("Saldo restante: R$ %.2f%n", despesa.getSaldoRestante());
        System.out.print("Valor do pagamento: R$ ");
        double valorPagamento = lerDouble();
        
        System.out.print("Data do pagamento (dd/MM/yyyy): ");
        scanner.nextLine(); 
        LocalDate dataPagamento = lerData();
        
        if (despesa.registrarPagamento(valorPagamento, dataPagamento)) {
            despesaService.atualizarDespesa(despesa);
            System.out.println("\n✅ Pagamento registrado com sucesso!");
            if (despesa.estaPago()) {
                System.out.println("✅ Despesa totalmente paga!");
            } else {
                System.out.printf("Saldo restante: R$ %.2f%n", despesa.getSaldoRestante());
            }
        } else {
            System.out.println("\n❌ Erro ao registrar pagamento. Verifique o valor.");
        }
    }
    
  
    private static void listarDespesasEmAberto() {
        System.out.println("\n--- DESPESAS EM ABERTO ---");
        System.out.println("1. Todas");
        System.out.println("2. Por período");
        System.out.print("Opção: ");
        int opcao = lerInteiro();
        
        List<Despesa> despesas;
        
        if (opcao == 2) {
            System.out.print("Data início (dd/MM/yyyy): ");
            scanner.nextLine();
            LocalDate inicio = lerData();
            System.out.print("Data fim (dd/MM/yyyy): ");
            LocalDate fim = lerData();
            despesas = despesaService.listarEmAbertoPorPeriodo(inicio, fim);
        } else {
            despesas = despesaService.listarEmAberto();
        }
        
        despesaService.exibirDespesas(despesas);
        
        if (!despesas.isEmpty()) {
            menuAcoesDespesa(despesas);
        }
    }
    

    private static void listarDespesasPagas() {
        System.out.println("\n--- DESPESAS PAGAS ---");
        System.out.println("1. Todas");
        System.out.println("2. Por período");
        System.out.print("Opção: ");
        int opcao = lerInteiro();
        
        List<Despesa> despesas;
        
        if (opcao == 2) {
            System.out.print("Data início (dd/MM/yyyy): ");
            scanner.nextLine();
            LocalDate inicio = lerData();
            System.out.print("Data fim (dd/MM/yyyy): ");
            LocalDate fim = lerData();
            despesas = despesaService.listarPagasPorPeriodo(inicio, fim);
        } else {
            despesas = despesaService.listarPagas();
        }
        
        despesaService.exibirDespesas(despesas);
        
        if (!despesas.isEmpty()) {
            menuAcoesDespesa(despesas);
        }
    }
   
    private static void menuAcoesDespesa(List<Despesa> despesas) {
        System.out.println("\n--- AÇÕES ---");
        System.out.println("1. Editar Despesa");
        System.out.println("2. Excluir Despesa");
        System.out.println("0. Voltar ao Menu Principal");
        System.out.print("Opção: ");
        int opcao = lerInteiro();
        
        switch(opcao) {
            case 1 -> editarDespesa();
            case 2 -> excluirDespesa();
            case 0 -> {
            }
            default -> System.out.println("❌ Opção inválida!");
        }
    }
    
    
    private static void editarDespesa() {
        System.out.print("\nID da despesa a editar: ");
        int id = lerInteiro();
        
        Despesa despesa = despesaService.buscarPorId(id);
        if (despesa == null) {
            System.out.println("❌ Despesa não encontrada!");
            return;
        }
        
        scanner.nextLine();
        System.out.print("Nova descrição (Enter para manter): ");
        String descricao = scanner.nextLine();
        if (!descricao.isEmpty()) {
            despesa.setDescricao(descricao);
        }
        
        System.out.print("Novo valor (0 para manter): ");
        double valor = lerDouble();
        if (valor > 0) {
            despesa.setValor(valor);
        }
        
        if (despesaService.atualizarDespesa(despesa)) {
            System.out.println("✅ Despesa atualizada com sucesso!");
        } else {
            System.out.println("❌ Erro ao atualizar despesa.");
        }
    }
    
  
    private static void excluirDespesa() {
        System.out.print("\nID da despesa a excluir: ");
        int id = lerInteiro();
        
        System.out.print("Confirma exclusão? (S/N): ");
        scanner.nextLine();
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            if (despesaService.removerDespesa(id)) {
                System.out.println("✅ Despesa excluída com sucesso!");
            } else {
                System.out.println("❌ Despesa não encontrada!");
            }
        }
    }
    
   
    private static void gerenciarTiposDespesa() {
        int opcao;
        
        do {
            System.out.println("\n--- GERENCIAR TIPOS DE DESPESA ---");
            System.out.println("1. Listar Tipos");
            System.out.println("2. Criar Tipo");
            System.out.println("3. Editar Tipo");
            System.out.println("4. Excluir Tipo");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInteiro();
            
            switch(opcao) {
                case 1 -> tipoDespesaService.exibirTodos();
                case 2 -> criarTipoDespesa();
                case 3 -> editarTipoDespesa();
                case 4 -> excluirTipoDespesa();
                case 0 -> {
                }
                default -> System.out.println("❌ Opção inválida!");
            }
        } while(opcao != 0);
    }
    
   
    private static void criarTipoDespesa() {
        scanner.nextLine();
        System.out.print("Nome do tipo: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        TipoDespesa tipo = new TipoDespesa(nome, descricao);
        if (tipoDespesaService.adicionarTipo(tipo)) {
            System.out.println("✅ Tipo criado com sucesso!");
        } else {
            System.out.println("❌ Tipo já existe!");
        }
    }
    
    
    private static void editarTipoDespesa() {
        tipoDespesaService.exibirTodos();
        System.out.print("\nID do tipo a editar: ");
        int id = lerInteiro();
        
        TipoDespesa tipo = tipoDespesaService.buscarPorId(id);
        if (tipo == null) {
            System.out.println("❌ Tipo não encontrado!");
            return;
        }
        
        scanner.nextLine();
        System.out.print("Novo nome (Enter para manter): ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) {
            tipo.setNome(nome);
        }
        
        System.out.print("Nova descrição (Enter para manter): ");
        String descricao = scanner.nextLine();
        if (!descricao.isEmpty()) {
            tipo.setDescricao(descricao);
        }
        
        if (tipoDespesaService.atualizarTipo(tipo)) {
            System.out.println("✅ Tipo atualizado com sucesso!");
        } else {
            System.out.println("❌ Erro ao atualizar tipo.");
        }
    }
    
   
    private static void excluirTipoDespesa() {
        tipoDespesaService.exibirTodos();
        System.out.print("\nID do tipo a excluir: ");
        int id = lerInteiro();
        
        System.out.print("Confirma exclusão? (S/N): ");
        scanner.nextLine();
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            if (tipoDespesaService.removerTipo(id)) {
                System.out.println("✅ Tipo excluído com sucesso!");
            } else {
                System.out.println("❌ Tipo não encontrado!");
            }
        }
    }
    
   
    private static void gerenciarUsuarios() {
        int opcao;
        
        do {
            System.out.println("\n--- GERENCIAR USUÁRIOS ---");
            System.out.println("1. Listar Usuários");
            System.out.println("2. Cadastrar Usuário");
            System.out.println("3. Editar Usuário");
            System.out.println("4. Alterar Senha");
            System.out.println("5. Desativar Usuário");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInteiro();
            
            switch(opcao) {
                case 1 -> usuarioService.exibirTodos();
                case 2 -> cadastrarUsuario();
                case 3 -> editarUsuario();
                case 4 -> alterarSenha();
                case 5 -> desativarUsuario();
                case 0 -> {
                }
                default -> System.out.println("❌ Opção inválida!");
            }
        } while(opcao != 0);
    }
    
   
    private static void cadastrarUsuario() {
        scanner.nextLine();
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        if (usuarioService.cadastrarUsuario(login, senha, nome)) {
            System.out.println("✅ Usuário cadastrado com sucesso!");
        } else {
            System.out.println("❌ Login já existe!");
        }
    }
    
    
    private static void editarUsuario() {
        usuarioService.exibirTodos();
        System.out.print("\nID do usuário a editar: ");
        int id = lerInteiro();
        
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            System.out.println("❌ Usuário não encontrado!");
            return;
        }
        
        scanner.nextLine();
        System.out.print("Novo nome (Enter para manter): ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) {
            usuario.setNome(nome);
        }
        
        if (usuarioService.atualizarUsuario(usuario)) {
            System.out.println("✅ Usuário atualizado com sucesso!");
        } else {
            System.out.println("❌ Erro ao atualizar usuário.");
        }
    }
    
    
    private static void alterarSenha() {
        usuarioService.exibirTodos();
        System.out.print("\nID do usuário: ");
        int id = lerInteiro();
        
        scanner.nextLine();
        System.out.print("Senha atual: ");
        String senhaAtual = scanner.nextLine();
        System.out.print("Nova senha: ");
        String novaSenha = scanner.nextLine();
        
        if (usuarioService.alterarSenha(id, senhaAtual, novaSenha)) {
            System.out.println("✅ Senha alterada com sucesso!");
        } else {
            System.out.println("❌ Senha atual incorreta ou usuário não encontrado!");
        }
    }
    
 
    private static void desativarUsuario() {
        usuarioService.exibirTodos();
        System.out.print("\nID do usuário a desativar: ");
        int id = lerInteiro();
        
        System.out.print("Confirma desativação? (S/N): ");
        scanner.nextLine();
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            if (usuarioService.desativarUsuario(id)) {
                System.out.println("✅ Usuário desativado com sucesso!");
            } else {
                System.out.println("❌ Usuário não encontrado!");
            }
        }
    }
    
    
    private static int lerInteiro() {
        while (!scanner.hasNextInt()) {
            System.out.print("❌ Valor inválido! Digite um número: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    private static double lerDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("❌ Valor inválido! Digite um número: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
    
    private static LocalDate lerData() {
        while (true) {
            try {
                String dataStr = scanner.nextLine();
                return LocalDate.parse(dataStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.print("❌ Data inválida! Use o formato dd/MM/yyyy: ");
            }
        }
    }
}
