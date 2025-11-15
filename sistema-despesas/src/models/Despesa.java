package models;

import interfaces.Pagavel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Despesa implements Pagavel {
    protected int id;
    protected String descricao;
    protected double valor;
    protected LocalDate dataVencimento;
    protected TipoDespesa tipo;
    protected boolean pago;
    protected LocalDate dataPagamento;
    protected double valorPago;
    
    private static int totalDespesas = 0;
    private static int proximoId = 1;
    
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public Despesa(int id, String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipo) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.tipo = tipo;
        this.pago = false;
        this.valorPago = 0.0;
        totalDespesas++;
        if (id >= proximoId) {
            proximoId = id + 1;
        }
    }
    
    public Despesa(String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipo) {
        this(proximoId++, descricao, valor, dataVencimento, tipo);
    }
    
    public Despesa(String descricao, double valor) {
        this(descricao, valor, LocalDate.now().plusDays(30), null);
    }
    
    public abstract String getCategoria();
    
    public abstract double calcularMultaAtraso();

    @Override
    public boolean registrarPagamento(double valor, LocalDate data) {
        if (valor <= 0 || valor > getSaldoRestante()) {
            return false;
        }
        this.valorPago += valor;
        this.dataPagamento = data;
        if (this.valorPago >= this.valor) {
            this.pago = true;
        }
        return true;
    }
    
    @Override
    public boolean estaPago() {
        return pago;
    }
    
    @Override
    public double getValorTotal() {
        return valor;
    }
    
    @Override
    public double getValorPago() {
        return valorPago;
    }
    
    @Override
    public double getSaldoRestante() {
        return valor - valorPago;
    }

    public boolean estaVencida() {
        return !pago && LocalDate.now().isAfter(dataVencimento);
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public double getValor() {
        return valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public LocalDate getDataVencimento() {
        return dataVencimento;
    }
    
    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    
    public TipoDespesa getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoDespesa tipo) {
        this.tipo = tipo;
    }
    
    public LocalDate getDataPagamento() {
        return dataPagamento;
    }
    
    public void setPago(boolean pago) {
        this.pago = pago;
    }
    
    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
    
    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public static int getTotalDespesas() {
        return totalDespesas;
    }

    public static void resetarContador() {
        totalDespesas = 0;
        proximoId = 1;
    }

    public String toFileString() {
        return id + "|" + 
               descricao + "|" + 
               valor + "|" + 
               dataVencimento.format(DATE_FORMATTER) + "|" + 
               (tipo != null ? tipo.getId() : "0") + "|" + 
               pago + "|" + 
               (dataPagamento != null ? dataPagamento.format(DATE_FORMATTER) : "") + "|" + 
               valorPago + "|" + 
               getCategoria();
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s | R$ %.2f | Venc: %s | %s | Status: %s",
                id, descricao, valor, 
                dataVencimento.format(DATE_FORMATTER),
                getCategoria(),
                pago ? "PAGO" : "EM ABERTO");
    }
}
