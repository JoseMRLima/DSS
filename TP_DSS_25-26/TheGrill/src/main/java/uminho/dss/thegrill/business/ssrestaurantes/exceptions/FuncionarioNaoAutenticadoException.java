package uminho.dss.thegrill.business.ssrestaurantes.exceptions;

public class FuncionarioNaoAutenticadoException extends RuntimeException {
    public FuncionarioNaoAutenticadoException(String message) {
        super(message);
    }
}
