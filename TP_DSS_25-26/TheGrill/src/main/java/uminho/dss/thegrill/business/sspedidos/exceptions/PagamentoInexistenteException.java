package uminho.dss.thegrill.business.sspedidos.exceptions;

public class PagamentoInexistenteException extends RuntimeException {
    public PagamentoInexistenteException(String message) {
        super(message);
    }
}
