package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DespesaTransporte extends Despesa {
    private String veiculo;
    private double quilometragem;

    public DespesaTransporte(int id, String descricao, double valor, LocalDate dataVencimento, 
                            TipoDespesa tipo, String veiculo, double quilometragem) {
        super(id, descricao, valor, dataVencimento, tipo);
        this.veiculo = veiculo;
        this.quilometragem = quilometragem;
    }

    public DespesaTransporte(String descricao, double valor, LocalDate dataVencimento, 
                            TipoDespesa tipo, String veiculo, double quilometragem) {
        super(descricao, valor, dataVencimento, tipo);
        this.veiculo = veiculo;
        this.quilometragem = quilometragem;
    }

    public DespesaTransporte(String descricao, double valor, String veiculo) {
        super(descricao, valor);
        this.veiculo = veiculo;
        this.quilometragem = 0.0;
    }

    @Override
    public String getCategoria() {
        return "TRANSPORTE";
    }

    @Override
    public double calcularMultaAtraso() {
        if (!estaVencida()) {
            return 0.0;
        }
        long diasAtraso = ChronoUnit.DAYS.between(dataVencimento, LocalDate.now());
        return valor * 0.02 * diasAtraso;
    }

    public double calcularCustoPorKm() {
        if (quilometragem > 0) {
            return valor / quilometragem;
        }
        return 0.0;
    }

    public String getVeiculo() {
        return veiculo;
    }
    
    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }
    
    public double getQuilometragem() {
        return quilometragem;
    }
    
    public void setQuilometragem(double quilometragem) {
        this.quilometragem = quilometragem;
    }
    
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + veiculo + "|" + quilometragem;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Veículo: %s | KM: %.2f", veiculo, quilometragem);
    }
}
