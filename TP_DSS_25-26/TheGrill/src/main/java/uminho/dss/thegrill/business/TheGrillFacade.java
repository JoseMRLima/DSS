package uminho.dss.thegrill.business;

import uminho.dss.thegrill.business.sspedidos.*;
import uminho.dss.thegrill.business.ssrefeicoes.*;
import uminho.dss.thegrill.business.ssrestaurantes.*;

import java.util.List;
import java.util.UUID;

/**
 * A Facade principal do sistema TheGrill.
 * Esta classe atua como ponto único de entrada para a Interface de Utilizador (UI),
 * encaminhando os pedidos para os subsistemas corretos (Restaurantes, Pedidos, Refeições).
 */
public class TheGrillFacade implements ITheGrillLN {

    // variáveis de instância

    private ISSRestaurantes ssRestaurantes;
    private ISSPedidos ssPedidos;
    private ISSRefeicoes ssRefeicoes;


    // construtores

    /**
     * Construtor da Facade Principal.
     * Inicializa os subsistemas de Restaurantes, Pedidos e Refeições.
     */
    public TheGrillFacade() {
        this.ssRestaurantes = new SSRestaurantesFacade();
        this.ssPedidos = new SSPedidosFacade();
        this.ssRefeicoes = new SSRefeicoesFacade();
    }

    /**
     * Realiza o login de um funcionário no sistema.
     * @param email Email do funcionário
     * @param senha Senha de acesso
     * @return true se o login for bem-sucedido, false caso contrário
     */
    @Override
    public boolean login(String email, String senha) {
        return this.ssRestaurantes.login(email, senha);
    }

    /**
     * Termina a sessão do funcionário atual.
     */
    @Override
    public void logout() {
        this.ssRestaurantes.logout();
    }

    /**
     * Verifica se o funcionário autenticado tem permissões de gestão (Gerente ou COO).
     * @return true se tiver permissão, false caso contrário
     */
    @Override
    public boolean podeConsultarInformacoes() {
        return this.ssRestaurantes.podeConsultarInformacoes();
    }

    /**
     * Lista os indicadores de desempenho de um restaurante específico.
     * @param codRestaurante Identificador do restaurante
     * @return Lista de objetos Indicador
     */
    @Override
    public List<Indicador> listarIndicadores(UUID codRestaurante) {
        return this.ssRestaurantes.listarIndicadores(codRestaurante);
    }

    /**
     * Lista todos os restaurantes registados no sistema.
     * @return Lista de restaurantes
     */
    @Override
    public List<Restaurante> listarRestaurantes() {
        return this.ssRestaurantes.listarRestaurantes();
    }

    /**
     * Adiciona uma nota personalizada a um pedido existente.
     * @param codPedido Identificador do pedido
     * @param nota Texto da nota
     */
    @Override
    public void adicionarNotaPedido(UUID codPedido, String nota) {
        this.ssPedidos.adicionarNotaPedido(codPedido, nota);
    }

    /**
     * Adiciona um pedido à fila de processamento.
     * @param codPedido Identificador do pedido
     */
    @Override
    public void adicionarPedido(UUID codPedido) {
        this.ssPedidos.adicionarPedido(codPedido);
    }

    /**
     * Altera o estado de um pedido manualmente.
     * @param codPedido Identificador único do pedido
     * @param estado Novo estado do pedido
     */
    @Override
    public void alterarEstadoPedido(UUID codPedido, EstadoPedido estado) {
        this.ssPedidos.alterarEstadoPedido(codPedido, estado);
    }

    /**
     * Atrasa a previsão de entrega de um pedido.
     * @param codPedido Identificador do pedido
     * @param tempo Número de minutos a adicionar ao tempo estimado
     */
    @Override
    public void atrasarPedido(UUID codPedido, int tempo) {
        this.ssPedidos.atrasarPedido(codPedido, tempo);
    }

    /**
     * Calcula o tempo total estimado para a preparação de um pedido.
     * @param codPedido Identificador do pedido
     * @return Tempo em minutos
     */
    @Override
    public int calcularTempoPreparacao(UUID codPedido) {
        return this.ssPedidos.calcularTempoPreparacao(codPedido);
    }

    /**
     * Obtém o tempo de espera atual previsto para o cliente.
     * @param codPedido Identificador do pedido
     * @return Tempo de espera em minutos
     */
    @Override
    public int getTempoEspera(UUID codPedido) {
        return this.ssPedidos.getTempoEspera(codPedido);
    }

    /**
     * Calcula o valor monetário total de um pedido.
     * @param codPedido Identificador do pedido
     * @return Valor total em euros
     */
    @Override
    public float calcularValorPedido(UUID codPedido) {
        return this.ssPedidos.calcularValorPedido(codPedido);
    }

    /**
     * Cancela um pedido, removendo-o da fila ativa.
     * @param codPedido Identificador do pedido
     */
    @Override
    public void cancelarPedido(UUID codPedido) {
        this.ssPedidos.cancelarPedido(codPedido);
    }

    /**
     * Gera uma fatura para um pagamento efetuado.
     * @param codPagamento Identificador do pagamento
     * @return Objeto FaturaPagamento
     */
    @Override
    public FaturaPagamento gerarFaturaPagamento(UUID codPagamento) {
        return this.ssPedidos.gerarFaturaPagamento(codPagamento);
    }

    /**
     * Gera o talão de caixa para o cliente.
     * @param codPedido Identificador do pedido
     * @return Objeto TalaoCaixaPagamento
     */
    @Override
    public TalaoCaixaPagamento gerarTalaoCaixaPagamento(UUID codPedido) {
        return this.ssPedidos.gerarTalaoCaixaPagamento(codPedido);
    }

    /**
     * Gera o talão de senha para levantamento ao balcão.
     * @param codPedido Identificador do pedido
     * @return Objeto TalaoLevantamento
     */
    @Override
    public TalaoLevantamento gerarTalaoLevantamento(UUID codPedido) {
        return this.ssPedidos.gerarTalaoLevantamento(codPedido);
    }

    /**
     * Regista o pagamento de um pedido, iniciando o seu processamento na cozinha.
     * @param codPedido Identificador do pedido
     * @param tipoPagamento Método de pagamento (Numerário, Multibanco, etc.)
     * @return Identificador do pagamento gerado
     */
    @Override
    public UUID registarPagamento(UUID codPedido, String tipoPagamento) {
        return this.ssPedidos.registarPagamento(codPedido, tipoPagamento);
    }

    /**
     * Regista um novo pedido no sistema associado a um restaurante.
     * @param refeicao A refeição escolhida
     * @param restauranteId O restaurante onde o pedido é feito
     * @return O ID do novo pedido
     */
    @Override
    public UUID registarPedido(Refeicao refeicao, UUID restauranteId) {
        return this.ssPedidos.registarPedido(refeicao, restauranteId);
    }

    /**
     * Lista todos os pedidos que estão na fila de espera para serem confecionados.
     * @return Lista de pedidos
     */
    @Override
    public List<Pedido> listarPedidosAConfecionar() {
        return this.ssPedidos.listarPedidosAConfecionar();
    }

    /**
     * Obtém o número do balcão onde o pedido deve ser levantado.
     * @param codPedido Identificador do pedido
     * @return Número do balcão
     */
    @Override
    public int obterBalcaoEntrega(UUID codPedido) {
        return this.ssPedidos.obterBalcaoEntrega(codPedido);
    }

    /**
     * Marca o pedido como EMBALADO (para Take-Away).
     * @param codPedido Identificador do pedido
     */
    @Override
    public void registarEmbalamento(UUID codPedido) {
        this.ssPedidos.registarEmbalamento(codPedido);
    }

    /**
     * Marca o pedido como EMPRATADO (para comer no local).
     * @param codPedido Identificador do pedido
     */
    @Override
    public void registarEmpratamento(UUID codPedido) {
        this.ssPedidos.registarEmpratamento(codPedido);
    }

    /**
     * Retira o próximo pedido da fila de espera para iniciar a produção.
     * @return O pedido a ser iniciado
     */
    @Override
    public Pedido removerProximoPedido() {
        return this.ssPedidos.removerProximoPedido();
    }

    /**
     * Reordena a fila de pedidos com base na prioridade ou tempo de preparação.
     */
    @Override
    public void reordenarListaPedidos() {
        this.ssPedidos.reordenarListaPedidos();
    }

    /**
     * Obtém as tarefas pendentes para um posto de trabalho específico.
     * @param posto Nome do posto ("GRELHA", "FRITURA", "BEBIDAS", etc.)
     * @return Lista de tarefas de confeção
     */
    @Override
    public List<TarefaConfecao> getTarefasPosto(String posto) {
        return this.ssPedidos.getTarefasPosto(posto);
    }

    /**
     * Altera o estado de uma tarefa específica na cozinha.
     * @param idTarefa Identificador da tarefa
     * @param novoEstado Novo estado (ex: PRONTO)
     */
    @Override
    public void alterarEstadoTarefa(UUID idTarefa, EstadoPedido novoEstado) {
        this.ssPedidos.alterarEstadoTarefa(idTarefa, novoEstado);
    }

    /**
     * Lista os pedidos que já foram confecionados e montados, aguardando transporte para o balcão.
     * @return Lista de pedidos prontos para entrega
     */
    @Override
    public List<Pedido> listarPedidosParaEntrega() {
        return this.ssPedidos.listarPedidosParaEntrega();
    }

    /**
     * Regista que o pedido foi entregue no balcão ao cliente.
     * @param codPedido Identificador do pedido
     */
    @Override
    public void registarEntrega(UUID codPedido) {
        this.ssPedidos.registarEntrega(codPedido);
    }

    /**
     * Lista os pratos principais disponíveis no menu.
     * @return Lista de pratos
     */
    @Override
    public List<Prato> listarPratosPrincipais() {
        return this.ssRefeicoes.listarPratosPrincipais();
    }

    /**
     * Lista as bebidas disponíveis.
     * @return Lista de bebidas
     */
    @Override
    public List<Bebida> listarBebidas() {
        return this.ssRefeicoes.listarBebidas();
    }

    /**
     * Lista os acompanhamentos disponíveis.
     * @return Lista de acompanhamentos
     */
    @Override
    public List<Acompanhamento> listarAcompanhamentos() {
        return this.ssRefeicoes.listarAcompanhamentos();
    }

    /**
     * Cria uma refeição completa (Menu) com Prato, Bebida e Acompanhamento.
     * @param prt Prato escolhido
     * @param beb Bebida escolhida
     * @param acm Acompanhamento escolhido
     * @return ID da refeição criada
     */
    @Override
    public UUID criarRefeicaoMenu(Prato prt, Bebida beb, Acompanhamento acm) {
        return this.ssRefeicoes.criarRefeicaoMenu(prt, beb, acm);
    }

    /**
     * Lista os alergénicos presentes num determinado produto.
     * @param codProduto Identificador do produto
     * @return Lista de alergénicos
     */
    @Override
    public List<Alergenico> listarAlergenicos(UUID codProduto) {
        return this.ssRefeicoes.listarAlergenicos(codProduto);
    }

    /**
     * Cria uma refeição personalizada item a item.
     * @param produtos Lista de produtos selecionados
     * @return ID da refeição criada
     */
    @Override
    public UUID criarRefeicaoItemItem(List<Produto> produtos) {
        return this.ssRefeicoes.criarRefeicaoItemItem(produtos);
    }

    /**
     * Lista todos os produtos base do sistema.
     * @return Lista de produtos
     */
    @Override
    public List<Produto> listarProdutos() {
        return this.ssRefeicoes.listarProdutos();
    }

    /**
     * Valida se uma refeição é válida (ex: tem stock suficiente).
     * @param codRefeicao Identificador da refeição
     * @return true se válida
     */
    @Override
    public boolean validarRefeicao(UUID codRefeicao) {
        return this.ssRefeicoes.validarRefeicao(codRefeicao);
    }

    /**
     * Lista ingredientes alternativos disponíveis para um produto.
     * @param codProduto Identificador do produto
     * @return Lista de ingredientes
     */
    @Override
    public List<Ingrediente> listarIngredientesAlternativos(UUID codProduto) {
        return this.ssRefeicoes.listarIngredientesAlternativos(codProduto);
    }

    /**
     * Substitui um ingrediente numa personalização de produto.
     * @param codProduto ID do produto
     * @param codIngEntrada ID do ingrediente a adicionar
     * @param codIngSaida ID do ingrediente a remover
     */
    @Override
    public void substituirIngrediente(UUID codProduto, UUID codIngEntrada, UUID codIngSaida) {
        this.ssRefeicoes.substituirIngrediente(codProduto, codIngEntrada, codIngSaida);
    }

    /**
     * Remove um ingrediente de um produto.
     * @param codProduto ID do produto
     * @param codIngrediente ID do ingrediente a remover
     */
    @Override
    public void removerIngrediente(UUID codProduto, UUID codIngrediente) {
        this.ssRefeicoes.removerIngrediente(codProduto, codIngrediente);
    }

    /**
     * Adiciona um ingrediente extra a um produto.
     * @param codProduto ID do produto
     * @param codIngrediente ID do ingrediente a adicionar
     */
    @Override
    public void adicionarIngrediente(UUID codProduto, UUID codIngrediente) {
        this.ssRefeicoes.adicionarIngrediente(codProduto, codIngrediente);
    }

    /**
     * Consulta o stock atual de um ingrediente.
     * @param codIngrediente ID do ingrediente
     * @return Quantidade em stock
     */
    @Override
    public int obterStockIngrediente(UUID codIngrediente) {
        return this.ssRefeicoes.obterStockIngrediente(codIngrediente);
    }

    /**
     * Consulta o tempo necessário para repor/solicitar um ingrediente.
     * @param codIngrediente ID do ingrediente
     * @return Tempo em minutos
     */
    @Override
    public float obterTempoSolicitacaoIngrediente(UUID codIngrediente) {
        return this.ssRefeicoes.obterTempoSolicitacaoIngrediente(codIngrediente);
    }

}