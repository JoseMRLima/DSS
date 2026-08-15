package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.List;
import java.util.UUID;

/**
 * Interface do subsistema de Refeições
 * Este subsistema é responsável pela gestão de refeições, produtos, ingredientes e
 * respetivos alergénicos, fornecendo operações para isso.
 */
public interface ISSRefeicoes {

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
     *
     * @param codProduto
     * @return
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

    int solicitarIngrediente(UUID codIngrediente);

}
