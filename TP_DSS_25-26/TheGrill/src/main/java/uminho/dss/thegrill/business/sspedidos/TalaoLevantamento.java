package uminho.dss.thegrill.business.sspedidos;

import java.util.UUID;

/**
 * Representa o talão utilizado para levantamento do pedido.
 */
public class TalaoLevantamento {

    // variáveis de instância

    private UUID codTalaoLev;


    // construtores

    /**
     * Construtor do talão de levantamento.
     */
    public TalaoLevantamento() {
        this.codTalaoLev = UUID.randomUUID();
    }


    // métodos de instância

    /**
     * Devolve o código do TalaoLevantamento
     * @return Identificador do talão
     */
    public UUID getCodTalaoLev() {
        return codTalaoLev;
    }

}
