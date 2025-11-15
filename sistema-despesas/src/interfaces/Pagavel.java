package interfaces;

import java.time.LocalDate;


public interface Pagavel {
    
    boolean registrarPagamento(double valor, LocalDate data);
    boolean estaPago();
    double getValorTotal();
    double getValorPago();
    double getSaldoRestante();
}
