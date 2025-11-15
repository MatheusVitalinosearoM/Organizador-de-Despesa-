package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.*;
import utils.FileManager;

public class DespesaService {
    private static final String ARQUIVO = "despesas.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private final List<Despesa> despesas;
    private final TipoDespesaService tipoDespesaService;
    
    public DespesaService(TipoDespesaService tipoDespesaService) {
        this.despesas = new ArrayList<>();
        this.tipoDespesaService = tipoDespesaService;
        carregarDespesas();
    }
    private void carregarDespesas() {
        List<String> linhas = FileManager.lerArquivo(ARQUIVO);
        despesas.clear();
        
        for (String linha : linhas) {
            Despesa despesa = parseDespesa(linha);
            if (despesa != null) {
                despesas.add(despesa);
            }
        }
    }
    private Despesa parseDespesa(String linha) {
        try {
            String[] partes = linha.split("\\|");
            if (partes.length < 9) return null;
            
            int id = Integer.parseInt(partes[0]);
            String descricao = partes[1];
            double valor = Double.parseDouble(partes[2]);
            LocalDate vencimento = LocalDate.parse(partes[3], DATE_FORMATTER);
            int tipoId = Integer.parseInt(partes[4]);
            boolean pago = Boolean.parseBoolean(partes[5]);
            LocalDate dataPagamento = partes[6].isEmpty() ? null : LocalDate.parse(partes[6], DATE_FORMATTER);
            double valorPago = Double.parseDouble(partes[7]);
            String categoria = partes[8];
            
            TipoDespesa tipo = tipoDespesaService.buscarPorId(tipoId);
            
            Despesa despesa = null;

            switch (categoria) {
                case "TRANSPORTE" -> {
                    String veiculo = partes.length > 9 ? partes[9] : "";
                    double km = partes.length > 10 ? Double.parseDouble(partes[10]) : 0.0;
                    despesa = new DespesaTransporte(id, descricao, valor, vencimento, tipo, veiculo, km);
                }
                    
                case "EVENTUAL" -> {
                    String evento = partes.length > 9 ? partes[9] : "";
                    boolean recorrente = partes.length > 10 ? Boolean.parseBoolean(partes[10]) : false;
                    despesa = new DespesaEventual(id, descricao, valor, vencimento, tipo, evento, recorrente);
                }
                    
                case "SUPERFLUO" -> {
                    String cat = partes.length > 9 ? partes[9] : "";
                    int prioridade = partes.length > 10 ? Integer.parseInt(partes[10]) : 3;
                    despesa = new DespesaSuperfluo(id, descricao, valor, vencimento, tipo, cat, prioridade);
                }
            }
            
            if (despesa != null) {
                despesa.setPago(pago);
                despesa.setValorPago(valorPago);
                despesa.setDataPagamento(dataPagamento);
            }
            
            return despesa;
            
        } catch (NumberFormatException e) {
            System.err.println("Erro ao parsear despesa: " + e.getMessage());
            return null;
        }
    }

    private void salvarDespesas() {
        List<String> linhas = new ArrayList<>();
        for (Despesa despesa : despesas) {
            linhas.add(despesa.toFileString());
        }
        FileManager.escreverArquivo(ARQUIVO, linhas);
    }

    public boolean adicionarDespesa(Despesa despesa) {
        despesas.add(despesa);
        salvarDespesas();
        return true;
    }
    
    public boolean atualizarDespesa(Despesa despesa) {
        for (int i = 0; i < despesas.size(); i++) {
            if (despesas.get(i).getId() == despesa.getId()) {
                despesas.set(i, despesa);
                salvarDespesas();
                return true;
            }
        }
        return false;
    }

    public boolean removerDespesa(int id) {
        boolean removido = despesas.removeIf(d -> d.getId() == id);
        if (removido) {
            salvarDespesas();
        }
        return removido;
    }

    public Despesa buscarPorId(int id) {
        return despesas.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Despesa> listarEmAberto() {
        return despesas.stream()
                .filter(d -> !d.estaPago())
                .collect(Collectors.toList());
    }

    public List<Despesa> listarPagas() {
        return despesas.stream()
                .filter(Despesa::estaPago)
                .collect(Collectors.toList());
    }

    public List<Despesa> listarEmAbertoPorPeriodo(LocalDate inicio, LocalDate fim) {
        return despesas.stream()
                .filter(d -> !d.estaPago())
                .filter(d -> !d.getDataVencimento().isBefore(inicio) && !d.getDataVencimento().isAfter(fim))
                .collect(Collectors.toList());
    }

    public List<Despesa> listarPagasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return despesas.stream()
                .filter(Despesa::estaPago)
                .filter(d -> d.getDataPagamento() != null)
                .filter(d -> !d.getDataPagamento().isBefore(inicio) && !d.getDataPagamento().isAfter(fim))
                .collect(Collectors.toList());
    }
    
    public List<Despesa> listarVencidas() {
        return despesas.stream()
                .filter(Despesa::estaVencida)
                .collect(Collectors.toList());
    }
    
    public double calcularTotalEmAberto() {
        return despesas.stream()
                .filter(d -> !d.estaPago())
                .mapToDouble(Despesa::getSaldoRestante)
                .sum();
    }

    public double calcularTotalPago() {
        return despesas.stream()
                .filter(Despesa::estaPago)
                .mapToDouble(Despesa::getValorPago)
                .sum();
    }

    public void exibirDespesas(List<Despesa> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(100));
        for (Despesa despesa : lista) {
            System.out.println(despesa);
            if (despesa.estaVencida()) {
                System.out.printf("  ⚠ VENCIDA! Multa: R$ %.2f%n", despesa.calcularMultaAtraso());
            }
        }
        System.out.println("=".repeat(100));
        System.out.printf("Total de despesas: %d%n", lista.size());
    }

    public void exibirEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.printf("Total de despesas cadastradas: %d%n", despesas.size());
        System.out.printf("Despesas em aberto: %d (R$ %.2f)%n", 
                listarEmAberto().size(), calcularTotalEmAberto());
        System.out.printf("Despesas pagas: %d (R$ %.2f)%n", 
                listarPagas().size(), calcularTotalPago());
        System.out.printf("Despesas vencidas: %d%n", listarVencidas().size());
    }
}
