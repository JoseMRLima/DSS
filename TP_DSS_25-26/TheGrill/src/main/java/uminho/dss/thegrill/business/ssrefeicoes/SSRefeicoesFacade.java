package uminho.dss.thegrill.business.ssrefeicoes;

import uminho.dss.thegrill.business.ssrefeicoes.exceptions.IngredienteInexistenteException;
import uminho.dss.thegrill.business.ssrefeicoes.exceptions.ProdutoInexistenteException;
import uminho.dss.thegrill.data.RefeicaoDAO;
import uminho.dss.thegrill.data.ProdutoDAO;
import uminho.dss.thegrill.data.IngredienteDAO;

import java.util.*;

/**
 * Facade do subsistema de Refeições.
 */
public class SSRefeicoesFacade implements ISSRefeicoes {

    // variáveis de classe

    /** quantidade de stock a adicionar a um ingrediente */
    private static final int ACRESCIMO_INGREDIENTES = 10;

    // variáveis de instância

    private Map<UUID, Refeicao> refeicoes;
    private Map<UUID, Produto> produtos;
    private Map<UUID, Ingrediente> ingredientes;


    // construtores

    public SSRefeicoesFacade() {
        this.refeicoes = RefeicaoDAO.getInstance();
        this.produtos = ProdutoDAO.getInstance();
        this.ingredientes = IngredienteDAO.getInstance();
    }


    // métodos de instância

    /**
     * Lista todos os pratos principais disponíveis.
     * @return lista de pratos principais
     */
    @Override
    public List<Prato> listarPratosPrincipais() {
        List<Prato> res = new ArrayList<>();

        for (Produto p : this.produtos.values()) {
            if (p instanceof Prato) {
                res.add((Prato) p);
            }
        }

        return res;
    }

    /**
     * Lista todas as bebidas disponíveis.
     * @return lista de bebidas
     */
    @Override
    public List<Bebida> listarBebidas() {
        List<Bebida> res = new ArrayList<>();

        for (Produto p : this.produtos.values()) {
            if (p instanceof Bebida) {
                res.add((Bebida) p);
            }
        }

        return res;
    }

    /**
     * Lista todos os acompanhamentos disponíveis.
     * @return lista de acompanhamentos
     */
    @Override
    public List<Acompanhamento> listarAcompanhamentos() {
        List<Acompanhamento> res = new ArrayList<>();

        for (Produto p : this.produtos.values()) {
            if (p instanceof Acompanhamento) {
                res.add((Acompanhamento) p);
            }
        }

        return res;
    }

    /**
     * Cria uma nova refeição do tipo menu.
     *
     * Uma refeição menu é composta por um prato principal, uma bebida
     * e um acompanhamento. O identificador da refeição é gerado
     * automaticamente.
     *
     * A refeição criada é armazenada no subsistema através do DAO
     * de refeições.
     *
     * @param prt prato principal do menu
     * @param beb bebida do menu
     * @param acm acompanhamento do menu
     * @return identificador da refeição criada
     */
    @Override
    public UUID criarRefeicaoMenu(Prato prt, Bebida beb, Acompanhamento acm) {
        RefeicaoMenu menu = new RefeicaoMenu(prt, beb, acm);
        UUID codRefeicao = menu.getCodRefeicao();
        this.refeicoes.put(codRefeicao, menu);
        return codRefeicao;
    }

    /**
     * Devolve a lista de alergénicos associados a um produto.
     * @param codProduto código do produto
     * @return lista de alergénicos do produto
     * @throws ProdutoInexistenteException se o produto não existir
     */
    @Override
    public List<Alergenico> listarAlergenicos(UUID codProduto) throws ProdutoInexistenteException {
        Produto prod = this.produtos.get(codProduto);

        if (prod == null) {
            throw new ProdutoInexistenteException("Produto inexistente");
        }

        return prod.listarAlergenicos();
    }

    /**
     * Cria uma refeição do tipo Refeição Item a Item.
     *
     * A refeição é composta por vários produtos
     * São armazenados apenas os códigos dos produtos
     *
     * @param produtos lista de produtos que compõem a refeição
     * @return identificador da refeição criada
     */
    @Override
    public UUID criarRefeicaoItemItem(List<Produto> produtos) {
        UUID codRefeicao = UUID.randomUUID();

        RefeicaoItemItem ref = new RefeicaoItemItem(codRefeicao, produtos);

        this.refeicoes.put(codRefeicao, ref);

        return codRefeicao;
    }

    /**
     * Devolve a lista de todos os produtos existentes no sistema.
     * @return lista de produtos
     */
    @Override
    public List<Produto> listarProdutos() {
        List<Produto> res = new ArrayList<>();

        for (UUID codP : this.produtos.keySet()) {
            Produto p = this.produtos.get(codP);
            if (p != null) {
                res.add(p);
            }
        }

        return res;
    }

    /**
     * Verifica se uma refeição é válida
     */
    @Override
    public boolean validarRefeicao(UUID codRefeicao) {
        Refeicao ref = this.refeicoes.get(codRefeicao);
        if (ref == null) return false;

        Map<UUID, Integer> necessidades = new HashMap<>();
        List<Produto> produtosParaValidar = obterProdutosDaRefeicao(ref);

        for (Produto p : produtosParaValidar) {
            List<UUID> ingredientesDoPrato = p.getCodsFinais();

            for (UUID codIng : ingredientesDoPrato) {
                necessidades.put(codIng, necessidades.getOrDefault(codIng, 0) + 1);
            }
        }

        // Verificar no Stock
        for (Map.Entry<UUID, Integer> entry : necessidades.entrySet()) {
            UUID idIngrediente = entry.getKey();
            int qtdNecessaria = entry.getValue();

            Ingrediente ing = this.ingredientes.get(idIngrediente);

            if (ing == null || ing.getStock() < qtdNecessaria) {
                return false;
            }
        }
        return true;
    }

    /**
     * Auxiliar: Devolve a lista de produtos associados a uma refeição.
     */
    private List<Produto> obterProdutosDaRefeicao(Refeicao ref) {
        List<Produto> res = new ArrayList<>();
        if (ref instanceof RefeicaoMenu) {
            RefeicaoMenu menu = (RefeicaoMenu) ref;
            res.add(menu.getPrato());
            res.add(menu.getBebida());
            res.add(menu.getAcompanhamento());
        } else if (ref instanceof RefeicaoItemItem) {
            RefeicaoItemItem rii = (RefeicaoItemItem) ref;
            res.addAll(rii.getProdutos());
        }
        return res;
    }

    /**
     * Devolve a lista de ingredientes alternativos de um produto
     * @param codProduto código do produto
     * @return lista de ingredientes alternativos
     * @throws ProdutoInexistenteException se o produto não existir
     */
    @Override
    public List<Ingrediente> listarIngredientesAlternativos(UUID codProduto) throws ProdutoInexistenteException {
        Produto prod = this.produtos.get(codProduto);

        if (prod == null) {
            throw new ProdutoInexistenteException("Produto inexistente: " + codProduto);
        }

        return prod.listarIngredientesAlternativos();
    }

    /**
     * Substitui um ingrediente de um produto por outro
     * @param codProduto   código do produto
     * @param codIngSaida  ingrediente a remover
     * @param codIngEntrada ingrediente a adicionar
     * @throws ProdutoInexistenteException se o produto não existir
     */
    @Override
    public void substituirIngrediente(UUID codProduto, UUID codIngSaida, UUID codIngEntrada) throws ProdutoInexistenteException {
        Produto prod = this.produtos.get(codProduto);

        if (prod == null) {
            throw new ProdutoInexistenteException("Produto inexistente: " + codProduto);
        }

        prod.substituirIngrediente(codIngSaida, codIngEntrada);

        this.produtos.put(codProduto, prod);
    }


    /**
     * Remove um ingrediente de um produto
     * @param codProduto código do produto
     * @param codIngrediente código do ingrediente a remover
     * @throws ProdutoInexistenteException se o produto não existir
     */
    @Override
    public void removerIngrediente(UUID codProduto, UUID codIngrediente) throws ProdutoInexistenteException {
        Produto prod = this.produtos.get(codProduto);

        if (prod == null) {
            throw new ProdutoInexistenteException("Produto inexistente: " + codProduto);
        }

        prod.removerIngrediente(codIngrediente);

        this.produtos.put(codProduto, prod);
    }

    /**
     * Adiciona um ingrediente a um produto
     * @param codProduto código do produto
     * @param codIngrediente código do ingrediente a adicionar
     * @throws ProdutoInexistenteException se o produto não existir
     */
    @Override
    public void adicionarIngrediente(UUID codProduto, UUID codIngrediente) throws ProdutoInexistenteException {

        Produto prod = this.produtos.get(codProduto);

        if (prod == null) {
            throw new ProdutoInexistenteException("Produto inexistente: " + codProduto);
        }

        prod.adicionarIngrediente(codIngrediente);

        this.produtos.put(codProduto, prod);
    }


    /**
     * Devolve o stock disponível de um ingrediente.
     * @param codIngrediente código do ingrediente
     * @return quantidade em stock
     * @throws IngredienteInexistenteException se o ingrediente não existir
     */
    @Override
    public int obterStockIngrediente(UUID codIngrediente) throws IngredienteInexistenteException {

        Ingrediente ing = this.ingredientes.get(codIngrediente);

        if (ing == null) {
            throw new IngredienteInexistenteException("Ingrediente inexistente: " + codIngrediente);
        }

        return ing.getStock();
    }

    /**
     * Devolve o tempo necessário para solicitar um ingrediente
     * @param codIngrediente código do ingrediente
     * @return tempo de solicitação
     * @throws IngredienteInexistenteException se o ingrediente não existir
     */
    @Override
    public float obterTempoSolicitacaoIngrediente(UUID codIngrediente) throws IngredienteInexistenteException {

        Ingrediente ing = this.ingredientes.get(codIngrediente);

        if (ing == null) {
            throw new IngredienteInexistenteException("Ingrediente inexistente: " + codIngrediente);
        }

        return ing.getTempoSolicitacao();
    }

    public int solicitarIngrediente(UUID codIngrediente) throws IngredienteInexistenteException {
        Ingrediente ing = this.ingredientes.get(codIngrediente);

        if (ing == null)
            throw new IngredienteInexistenteException("Ingrediente inexistente");

        ing.aumentarStock(SSRefeicoesFacade.ACRESCIMO_INGREDIENTES);
        this.ingredientes.put(codIngrediente, ing);

        return ing.getTempoSolicitacao();
    }

}
