package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DespesaEventual extends Despesa {
    private String evento;
    private boolean recorrente;

    public DespesaEventual(int id, String descricao, double valor, LocalDate dataVencimento, 
                          TipoDespesa tipo, String evento, boolean recorrente) {
        super(id, descricao, valor, dataVencimento, tipo);
        this.evento = evento;
        this.recorrente = recorrente;
    }

    public DespesaEventual(String descricao, double valor, LocalDate dataVencimento, 
                          TipoDespesa tipo, String evento, boolean recorrente) {
        super(descricao, valor, dataVencimento, tipo);
        this.evento = evento;
        this.recorrente = recorrente;
    }

    public DespesaEventual(String descricao, double valor, String evento) {
        super(descricao, valor);
        this.evento = evento;
        this.recorrente = false;
    }

    @Override
    public String getCategoria() {
        return "EVENTUAL";
    }

    @Override
    public double calcularMultaAtraso() {
        if (!estaVencida()) {
            return 0.0;
        }
        long diasAtraso = ChronoUnit.DAYS.between(dataVencimento, LocalDate.now());
        return valor * 0.01 * diasAtraso;
    }

    public String getEvento() {
        return evento;
    }
    
    public void setEvento(String evento) {
        this.evento = evento;
    }
    
    public boolean isRecorrente() {
        return recorrente;
    }
    
    public void setRecorrente(boolean recorrente) {
        this.recorrente = recorrente;
    }
    
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + evento + "|" + recorrente;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Evento: %s | Recorrente: %s", 
                evento, recorrente ? "Sim" : "Não");
    }
}
