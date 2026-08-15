package uminho.dss.thegrill.business.sspedidos;

import java.util.UUID;

/**
 * Representa o talão emitido para pagamento na caixa.
 */
public class TalaoCaixaPagamento {

    // variáveis de instância

    /** código de TalaoCaixaPagamento */
    private UUID codTalaoPag;
    /** código de Pedido */
    private UUID codPedido;


    // construtores

    /**
     * Construtor do talão de pagamento.
     * @param codPedido Identificador do pedido
     */
    public TalaoCaixaPagamento(UUID codPedido) {
        this.codTalaoPag = UUID.randomUUID();
        this.codPedido = codPedido;
    }


    // métodos de instância

    /**
     * Devolve o código de TalaoCaixaPagamento
     * @return Identificador do talão
     */
    public UUID getCodTalaoPag() {
        return codTalaoPag;
    }

    /**
     * Devolve o código do Pedido
     * @return Identificador do pedido
     */
    public UUID getNrPedido() {
        return codPedido;
    }

}
