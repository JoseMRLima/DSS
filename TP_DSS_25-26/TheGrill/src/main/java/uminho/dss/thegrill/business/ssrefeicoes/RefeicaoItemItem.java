package uminho.dss.thegrill.business.ssrefeicoes;

import uminho.dss.thegrill.data.ProdutoDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Classe que representa uma Refeição do tipo Item-a-Item.
 */
public class RefeicaoItemItem extends Refeicao {

    /** Lista de produtos da refeição */
    private List<Produto> produtos;

    // Construtores

    /**
     * Construtor por omissão de uma RefeiçãoItemItem.
     */
    public RefeicaoItemItem() {
        super();
        this.produtos = new ArrayList<>();
    }

    /**
     * Construtor parametrizado.
     * @param codRefeicao Código da refeição
     * @param produtos Lista de objetos Produto (que podem ter ingredientes alterados)
     */
    public RefeicaoItemItem(UUID codRefeicao, List<Produto> produtos) {
        super(codRefeicao);
        this.produtos = produtos;
    }

    /**
     * Construtor de cópia.
     * @param other refeição item-a-item a copiar
     */
    public RefeicaoItemItem(RefeicaoItemItem other) {
        super(other);
        this.produtos = new ArrayList<>();
        for (Produto p : other.getProdutos()) {
            this.produtos.add(p.clone());
        }
    }


    // métodos de instância

    /**
     * Devolve a lista de produtos
     */
    public List<Produto> getProdutos() {
        return this.produtos;
    }

    /**
     * Define a lista de produtos.
     */
    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    /**
     * Devolve a lista de códigos dos produtos da refeição.
     * @return lista de códigos dos produtos
     */
    public List<UUID> getCodsProdutos() {
        List<UUID> cods = new ArrayList<>();
        for (Produto p : this.produtos) {
            cods.add(p.getCodProduto());
        }
        return cods;
    }

    /**
     * Calcula o custo total da refeição item-a-item, somando o custo de todos os produtos.
     * @return custo total da refeição
     */
    @Override
    public float calcularCustoRefeicao() {
        float total = 0;
        // Agora iteramos diretamente sobre a lista de objetos
        for (Produto p : this.produtos) {
            total += p.getCusto();
        }
        return total;
    }

    /**
     * Calcula o tempo total de preparação.
     * @return tempo total em minutos
     */
    public int calcularTempoPreparacao() {
        int total = 0;
        for (Produto p : this.produtos) {
            total += p.getTempoConfecao();
        }
        return total;
    }

    /**
     * Cria uma cópia desta RefeiçãoItemItem.
     * @return cópia da refeição
     */
    @Override
    public RefeicaoItemItem clone() {
        return new RefeicaoItemItem(this);
    }

    /**
     * Devolve uma representação textual da RefeiçãoItemItem.
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RefeicaoItemItem {");
        sb.append("codRefeicao=").append(getCodRefeicao());
        // Imprime a lista de produtos (que vai chamar o toString de cada Produto)
        sb.append(", produtos=").append(this.produtos);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Compara esta RefeiçãoItemItem com outro objeto.
     * @param obj objeto a comparar
     * @return {@code true} se forem iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        RefeicaoItemItem other = (RefeicaoItemItem) obj;
        // Compara as listas de objetos (assumindo que Produto tem o equals bem definido)
        return this.produtos.equals(other.produtos);
    }

}
