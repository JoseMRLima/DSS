package uminho.dss.thegrill.business.sspedidos;

import java.util.UUID;

/**
 * Representa um pagamento efetuado por um cliente.
 */
public class Pagamento {

    // variáveis de instância

    /** Identificador único do pagamento*/
    private UUID codPagamento;
    /** Identificador do pedido associado ao pagamento */
    private UUID codPedido;
    /** Valor total pago */
    private float total;
    /** Tipo de pagamento (ex.: dinheiro, cartão, MBWay) */
    private String tipo;


    // construtores

    public Pagamento() {
        this.codPagamento = UUID.randomUUID();
        this.codPedido = null;
        this.total = 0;
        this.tipo = "";
    }

    public Pagamento(UUID codPedido, float total, String tipo) {
        this.codPagamento = UUID.randomUUID();
        this.codPedido = codPedido;
        this.total = total;
        this.tipo = tipo;
    }

    public Pagamento(Pagamento other) {
        this.codPagamento = other.getCodPagamento();
        this.codPedido = other.getCodPedido();
        this.total = other.getTotal();
        this.tipo = other.getTipo();
    }

    public Pagamento(UUID codPagamento, UUID codPedido, float total, String tipo) {
        this.codPagamento = codPagamento;
        this.codPedido = codPedido;
        this.total = total;
        this.tipo = tipo;
    }

    // métodos de instância

    /**
     * Obtém o identificador do pagamento.
     * @return UUID do pagamento
     */
    public UUID getCodPagamento() {
        return codPagamento;
    }

    /**
     * Obtém o identificador do pedido associado.
     * @return UUID do pedido
     */
    public UUID getCodPedido() {
        return codPedido;
    }

    /**
     * Obtém o valor total pago.
     * @return Valor pago
     */
    public float getTotal() {
        return total;
    }

    /**
     * Obtém o tipo de pagamento.
     * @return Tipo de pagamento
     */
    public String getTipo() {
        return tipo;
    }

    public FaturaPagamento gerarFaturaPagamento() {
        return new FaturaPagamento();
    }

    /**
     * Cria uma cópia do pagamento.
     * @return Cópia do pagamento
     */
    @Override
    public Pagamento clone() {
        return new Pagamento(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Pagamento other = (Pagamento) obj;
        return this.codPagamento.equals(other.codPagamento) && this.codPedido.equals(other.codPedido)
                && this.total == other.total && this.tipo.equals(other.tipo);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Pagamento {");
        builder.append("código: ").append(codPagamento);
        builder.append(", pedido: ").append(codPedido);
        builder.append(", total: ").append(total);
        builder.append(", tipo: ").append(tipo);
        builder.append("}");

        return builder.toString();
    }

}
