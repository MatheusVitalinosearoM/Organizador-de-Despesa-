package services;

import java.util.ArrayList;
import java.util.List;
import models.TipoDespesa;
import utils.FileManager;

public class TipoDespesaService {
    private static final String ARQUIVO = "tipos_despesa.txt";
    private final List<TipoDespesa> tipos;
    
    public TipoDespesaService() {
        this.tipos = new ArrayList<>();
        carregarTipos();
    }

    private void carregarTipos() {
        List<String> linhas = FileManager.lerArquivo(ARQUIVO);
        tipos.clear();
        
        for (String linha : linhas) {
            TipoDespesa tipo = TipoDespesa.fromFileString(linha);
            if (tipo != null) {
                tipos.add(tipo);
            }
        }

        if (tipos.isEmpty()) {
            criarTiposPadrao();
        }
    }

    private void salvarTipos() {
        List<String> linhas = new ArrayList<>();
        for (TipoDespesa tipo : tipos) {
            linhas.add(tipo.toFileString());
        }
        FileManager.escreverArquivo(ARQUIVO, linhas);
    }

    private void criarTiposPadrao() {
        tipos.add(new TipoDespesa("Alimentação", "Despesas com alimentação"));
        tipos.add(new TipoDespesa("Transporte", "Despesas com transporte"));
        tipos.add(new TipoDespesa("Saúde", "Despesas com saúde"));
        tipos.add(new TipoDespesa("Educação", "Despesas com educação"));
        tipos.add(new TipoDespesa("Lazer", "Despesas com lazer"));
        tipos.add(new TipoDespesa("Moradia", "Despesas com moradia"));
        tipos.add(new TipoDespesa("Outros", "Outras despesas"));
        salvarTipos();
    }

    public boolean adicionarTipo(TipoDespesa tipo) {
        if (buscarPorNome(tipo.getNome()) != null) {
            return false; // Tipo já existe
        }
        tipos.add(tipo);
        salvarTipos();
        return true;
    }

    public boolean atualizarTipo(TipoDespesa tipo) {
        for (int i = 0; i < tipos.size(); i++) {
            if (tipos.get(i).getId() == tipo.getId()) {
                tipos.set(i, tipo);
                salvarTipos();
                return true;
            }
        }
        return false;
    }

    public boolean removerTipo(int id) {
        boolean removido = tipos.removeIf(t -> t.getId() == id);
        if (removido) {
            salvarTipos();
        }
        return removido;
    }

    public TipoDespesa buscarPorId(int id) {
        return tipos.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public TipoDespesa buscarPorNome(String nome) {
        return tipos.stream()
                .filter(t -> t.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public List<TipoDespesa> listarTodos() {
        return new ArrayList<>(tipos);
    }

    public void exibirTodos() {
        if (tipos.isEmpty()) {
            System.out.println("Nenhum tipo de despesa cadastrado.");
            return;
        }
        
        System.out.println("\n=== TIPOS DE DESPESA ===");
        for (TipoDespesa tipo : tipos) {
            System.out.printf("ID: %d | Nome: %s | Descrição: %s%n", 
                    tipo.getId(), tipo.getNome(), tipo.getDescricao());
        }
    }
}
