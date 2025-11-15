package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DespesaSuperfluo extends Despesa {
    private String categoria;
    private int prioridade; 

    public DespesaSuperfluo(int id, String descricao, double valor, LocalDate dataVencimento, 
                           TipoDespesa tipo, String categoria, int prioridade) {
        super(id, descricao, valor, dataVencimento, tipo);
        this.categoria = categoria;
        this.prioridade = Math.min(Math.max(prioridade, 1), 5); 
    }

    public DespesaSuperfluo(String descricao, double valor, LocalDate dataVencimento, 
                           TipoDespesa tipo, String categoria, int prioridade) {
        super(descricao, valor, dataVencimento, tipo);
        this.categoria = categoria;
        this.prioridade = Math.min(Math.max(prioridade, 1), 5);
    }

    public DespesaSuperfluo(String descricao, double valor, String categoria) {
        super(descricao, valor);
        this.categoria = categoria;
        this.prioridade = 3; 
    }

    @Override
    public String getCategoria() {
        return "SUPERFLUO";
    }
    
    @Override
    public double calcularMultaAtraso() {
        if (!estaVencida()) {
            return 0.0;
        }
        long diasAtraso = ChronoUnit.DAYS.between(dataVencimento, LocalDate.now());
        return valor * 0.005 * diasAtraso;
    }

    public boolean isBaixaPrioridade() {
        return prioridade >= 4;
    }
    
    public String getCategoriaEspecifica() {
        return categoria;
    }
    
    public void setCategoriaEspecifica(String categoria) {
        this.categoria = categoria;
    }
    
    public int getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(int prioridade) {
        this.prioridade = Math.min(Math.max(prioridade, 1), 5);
    }
    
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + categoria + "|" + prioridade;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Categoria: %s | Prioridade: %d/5", 
                categoria, prioridade);
    }
}
