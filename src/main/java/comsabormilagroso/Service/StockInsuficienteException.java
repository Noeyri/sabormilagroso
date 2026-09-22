package comsabormilagroso.Service;

// Se lanza cuando no hay stock suficiente para completar una operación.
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}