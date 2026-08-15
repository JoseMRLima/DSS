package uminho.dss.thegrill.business.sspedidos;

import java.util.UUID;

/**
 * Representa a fatura associada a um pagamento.
 */
public class FaturaPagamento {

    // variáveis de instância

    /** código de fatura */
    private UUID codFatura;


    // construtores

    /**
     * Construtor por omissão de FaturaPagamento
     */
    public FaturaPagamento() {
        this.codFatura = UUID.randomUUID();
    }


    // métodos de instância

    /**
     * Devolve o código de uma Fatura de Pagamento
     * @return Identificador da fatura
     */
    public UUID getCodFatura() {
        return codFatura;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Fatura de Pagamento {");
        builder.append("código: ").append(codFatura);
        builder.append("}");

        return builder.toString();
    }

}
