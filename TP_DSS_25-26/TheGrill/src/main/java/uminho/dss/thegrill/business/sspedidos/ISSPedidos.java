package uminho.dss.thegrill.business.sspedidos;

import uminho.dss.thegrill.business.ssrefeicoes.Refeicao;

import java.util.List;
import java.util.UUID;

/**
 * Interface que define os serviços disponibilizados pelo subsistema de Pedidos.
 * Este subsistema é responsável pela gestão do ciclo de vida
 * dos pedidos, incluindo estados, pagamentos, tempos estimados e cancelamentos.
 */
public interface ISSPedidos {

    /**
     * Adiciona uma nota a um pedido.
     * @param codPedido Identificador do pedido
     * @param nota Texto da nota
     */
    void adicionarNotaPedido(UUID codPedido, String nota);

    /**
     * Adiciona um pedido ao sistema.
     * @param codPedido Identificador do pedido
     */
    void adicionarPedido(UUID codPedido);

    /**
     * Altera o estado de um pedido.
     * @param codPedido Identificador único do pedido
     * @param estado Novo estado do pedido
     */
    void alterarEstadoPedido(UUID codPedido, EstadoPedido estado);

    /**
     * Atrasa um pedido dado um número de minutos
     * @param codPedido Identificador do pedido
     * @param tempo número de minutos a adicionar
     */
    void atrasarPedido(UUID codPedido, int tempo);

    /**
     * Calcula o tempo estimado de preparação de um pedido.
     * @param codPedido Identificador do pedido
     * @return Tempo estimado em minutos
     */
    int calcularTempoPreparacao(UUID codPedido);

    /**
     * Devolve o tempo estimado que o cliente terá de esperar pelo pedido
     * @param codPedido itentificador do Pedido
     * @return tempo de espera em minutos
     */
    int getTempoEspera(UUID codPedido);

    /**
     * Calcula o valor total de um pedido.
     * @param codPedido Identificador único do pedido
     * @return Valor total do pedido
     */
    float calcularValorPedido(UUID codPedido);

    /**
     * Cancela um pedido existente.
     * @param codPedido Identificador do pedido
     */
    void cancelarPedido(UUID codPedido);

    /**
     * Gera a fatura associada a um pagamento.
     * @param codPagamento Identificador do pagamento
     * @return Fatura de pagamento
     */
    FaturaPagamento gerarFaturaPagamento(UUID codPagamento);

    /**
     * Gera o talão de caixa para pagamento de um pedido.
     * @param codPedido Identificador único do pedido
     * @return Talão de pagamento
     */
    TalaoCaixaPagamento gerarTalaoCaixaPagamento(UUID codPedido);

    /**
     * Gera o talão de levantamento de um pedido.
     * @param codPedido Identificador do pedido
     * @return Talão de levantamento
     */
    TalaoLevantamento gerarTalaoLevantamento(UUID codPedido);

    /**
     * Lista os pedidos que se encontram em fila para confeção.
     * @return Lista de identificadores de pedidos
     */
    List<Pedido> listarPedidosAConfecionar();

    /**
     * Obtém o balcão de entrega associado a um pedido.
     * @param codPedido Identificador único do pedido
     * @return Número do balcão de entrega
     */
    int obterBalcaoEntrega(UUID codPedido);

    /**
     * Regista o embalamento de um pedido.
     * @param codPedido Identificador único do pedido
     */
    void registarEmbalamento(UUID codPedido);

    /**
     * Regista o empacotamento de um pedido.
     * @param codPedido Identificador único do pedido
     */
    void registarEmpratamento(UUID codPedido);

    /**
     * Regista o pagamento de um pedido.
     * @param codPedido Identificador do pedido
     * @param tipoPagamento Tipo de pagamento utilizado
     * @return Identificador do pagamento registado
     */
    UUID registarPagamento(UUID codPedido, String tipoPagamento);

    /**
     * Regista a entrega de um pedido.
     * @param codPedido Identificador do pedido
     */
    void registarEntrega(UUID codPedido);

    /**
     * Lista os pedidos que já estão finalizados pela cozinha (Embalados ou Empratados)
     * mas que ainda não foram entregues ao balcão.
     * @return Lista de pedidos prontos para transporte
     */
    List<Pedido> listarPedidosParaEntrega();

    /**
     *
     * @param refeicao
     */
    UUID registarPedido(Refeicao refeicao, UUID restauranteId);

    /**
     * Remove e devolve o próximo pedido a ser confecionado.
     * @return Pedido a confecionar
     */
    Pedido removerProximoPedido();

    /**
     * Reordena a lista de pedidos em confeção.
     */
    void reordenarListaPedidos();

    /**
     * Devolve as tarefas pendentes ou em confeção de um determinado posto.
     * @param posto Nome do posto (ex: "GRELHA")
     * @return Lista de tarefas
     */
    List<TarefaConfecao> getTarefasPosto(String posto);

    /**
     * Altera o estado de uma tarefa individual e atualiza o estado do pedido se necessário.
     * @param idTarefa Identificador da tarefa
     * @param novoEstado Novo estado (EM_CONFECAO, PRONTO)
     */
    void alterarEstadoTarefa(UUID idTarefa, EstadoPedido novoEstado);

}
