package uminho.dss.thegrill.business;

import uminho.dss.thegrill.business.sspedidos.*;
import uminho.dss.thegrill.business.ssrefeicoes.*;
import uminho.dss.thegrill.business.ssrestaurantes.*;

import java.util.List;
import java.util.UUID;

/**
 * Interface da Lógica de Negócio do sistema TheGrill.
 */
public interface ITheGrillLN {

    boolean login(String email, String senha);

    void logout();

    /**
     * Verifica se um funcionário tem permissões para consultar informações dos restaurantes.
     * @return true se tiver permissão, false caso contrário
     */
    boolean podeConsultarInformacoes();

    /**
     * Lista os indicadores de um restaurante específico.
     * @param codRestaurante Identificador do restaurante
     * @return Lista de objetos Indicador associados ao restaurante
     */
    List<Indicador> listarIndicadores(UUID codRestaurante);

    /**
     * Lista todos os restaurantes registados no sistema.
     * @return Lista de objetos Restaurante
     */
    List<Restaurante> listarRestaurantes();

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
     * Atrasa um pedido dado um número de minutos.
     * (Renomeado de alterarTempoPedido para bater certo com a Facade)
     * @param codPedido Identificador do pedido
     * @param tempo número de minutos a adicionar
     */
    void atrasarPedido(UUID codPedido, int tempo);

    /**
     * Calcula o tempo estimado de preparação de um pedido.
     * (Renomeado de calcularTempoEstimado para bater certo com a Facade)
     * @param codPedido Identificador do pedido
     * @return Tempo estimado em minutos
     */
    int calcularTempoPreparacao(UUID codPedido);

    /**
     * Devolve o tempo estimado que o cliente terá de esperar pelo pedido.
     * (Adicionado para completar a API)
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
     * @return Lista de pedidos
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
     * Regista um novo pedido no sistema.
     * @param refeicao Refeição a encomendar
     * @param restauranteId ID do restaurante onde é feito o pedido
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
     * Lista todos os pratos principais disponíveis
     * @return lista de pratos principais
     */
    List<Prato> listarPratosPrincipais();

    /**
     * Lista todas as bebidas disponíveis
     * @return lista de bebidas
     */
    List<Bebida> listarBebidas();

    /**
     * Lista todos os acompanhamentos disponíveis
     * @return lista de acompanhamentos
     */
    List<Acompanhamento> listarAcompanhamentos();

    /**
     * Cria uma refeição do tipo menu, que contém um prato principal, uma bebida e um acompanhamento
     * @param prt prato principal
     * @param beb bebida
     * @param acm acompanhamento
     * @return identificador da refeição criada
     */
    UUID criarRefeicaoMenu(Prato prt, Bebida beb, Acompanhamento acm);

    /**
     * Lista os alergénicos associados a um produto
     * @param codProduto identificador do produto
     * @return lista de alergénicos do produto
     */
    List<Alergenico> listarAlergenicos(UUID codProduto);

    /**
     * Cria uma refeição composta por um conjunto do produtos
     * @param produtos lista de produtos da refeição
     * @return identificador da refeição criada
     */
    UUID criarRefeicaoItemItem(List<Produto> produtos);

    /**
     * Lista todos os produtos disponiveis
     * @return lista de produtos
     */
    List<Produto> listarProdutos();

    /**
     * Valida se uma refeição pode ser preparada
     * @param codRefeicao identificador da refeição
     * @return true se a refeição for válida, false caso contrário
     */
    boolean validarRefeicao(UUID codRefeicao);

    /**
     * Lista ingredientes alternativos para um produto
     * @param codProduto ID do produto
     * @return Lista de ingredientes
     */
    List<Ingrediente> listarIngredientesAlternativos(UUID codProduto);

    /**
     * Substitui um ingrediente de um produto por outro
     * @param codProduto identificador do produto
     * @param codIngEntrada identificador do ingrediente a adicionar
     * @param codIngSaida identificador do ingrediente a remover
     */
    void substituirIngrediente(UUID codProduto, UUID codIngEntrada, UUID codIngSaida);

    /**
     * Remove um ingrediente de um produto
     * @param codProduto indetificador do produto
     * @param codIngrediente identificador do ingrediente
     */
    void removerIngrediente(UUID codProduto, UUID codIngrediente);

    /**
     * Adiciona um ingrediente a um produto
     * @param codProduto identificador do produto
     * @param codIngrediente identificador do ingrediente
     */
    void adicionarIngrediente(UUID codProduto, UUID codIngrediente);

    /**
     * Obtém o stock disponivel de um ingrediente
     * @param codIngrediente identificador do ingrediente
     * @return quantidade disponivel em stock
     */
    int obterStockIngrediente(UUID codIngrediente);

    /**
     * Obtém o tempo de solicitação de um ingrediente
     * @param codIngrediente identificador de um ingrediente
     * @return tempo necessário para solicitar o ingrediente
     */
    float obterTempoSolicitacaoIngrediente(UUID codIngrediente);

    /**
     * Obtém a lista de tarefas para aquele posto
     *
     * @param posto posto de trabalho
     * @return lista de tarefas do posto de trabalho
     */
    List<TarefaConfecao> getTarefasPosto(String posto);

    /**
     * Altera o estado de uma tarefa
     *
     * @param idTarefa identificador da tarefa
     * @param novoEstado novo estado da tarefa
     */
    void alterarEstadoTarefa(UUID idTarefa, EstadoPedido novoEstado);

    /**
     * Obtém a lista de pedidos para entrega
     *
     * @return lista de pedidos para entrega
     */
    List<Pedido> listarPedidosParaEntrega();

    /**
     * Regista uma entrega de um pedido
     *
     * @param codPedido identificador da pedido
     */
    void registarEntrega(UUID codPedido);

}