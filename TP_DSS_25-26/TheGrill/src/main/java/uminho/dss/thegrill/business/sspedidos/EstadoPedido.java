package uminho.dss.thegrill.business.sspedidos;

/**
 * Estados do ciclo de vida de um pedido na linha de produção.
 */
public enum EstadoPedido {
    /** Cliente iniciou a escolha mas ainda não fechou o pedido */
    INICIADO,

    /** Pedido pago; o sistema divide em tarefas para os postos (Grelha, Fritura, Bebidas, Montagem e Entrega) */
    PAGO,

    /** Pelo menos um posto de confeção já iniciou o trabalho */
    EM_CONFECAO,

    /** Todos os postos de base terminaram; aguarda montagem final */
    PRONTO,

    /** O pedido foi finalizado no prato para consumo local */
    EMPRATADO,

    /** O pedido foi embalado para take-away */
    EMBALADO,

    /** O pedido  está no balcão (Estado Final de sucesso) */
    ENTREGUE,

    /** Pedido interrompido por erro ou desistência */
    CANCELADO
}
