package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.List;
import java.util.UUID;

/**
 * Classe que representa um Acompanhamento.
 * Um acompanhamento é um {@link Produto} que possui uma porção associada.
 */
public class Acompanhamento extends Produto {

    /** Porção do acompanhamento */
    private Porcao porcao;

    // Construtores

    /**
     * Construtor por omissão de Acompanhamento.
     * Inicializa o acompanhamento com a porção média.
     */
    public Acompanhamento() {
        super();
        this.porcao = Porcao.MEDIO;
    }

    /**
     * Construtor parametrizado de Acompanhamento.
     *
     * @param codProduto código identificador do produto
     * @param designacao designação do acompanhamento
     * @param custo custo do acompanhamento
     * @param tempoConfecao tempo de confeção
     * @param obrigatorios lista de códigos de ingredientes obrigatórios
     * @param alternativos lista de códigos de ingredientes alternativos
     * @param substituiveis lista de códigos de ingredientes substituíveis
     * @param porcao porção do acompanhamento
     */
    public Acompanhamento(UUID codProduto,
                          String designacao,
                          float custo,
                          int tempoConfecao,
                          List<UUID> obrigatorios,
                          List<UUID> alternativos,
                          List<UUID> substituiveis,
                          Porcao porcao) {

        super(codProduto, designacao, custo, tempoConfecao,
                obrigatorios, alternativos, substituiveis);
        this.porcao = porcao;
    }

    /**
     * Construtor de cópia de Acompanhamento.
     *
     * @param other acompanhamento a copiar
     */
    public Acompanhamento(Acompanhamento other) {
        super(other);
        this.porcao = other.porcao;
    }

    // Getters e Setters

    /**
     * Devolve a porção do acompanhamento.
     *
     * @return porção do acompanhamento
     */
    public Porcao getPorcao() {
        return porcao;
    }

    /**
     * Altera a porção do acompanhamento.
     *
     * @param porcao nova porção
     */
    public void setPorcao(Porcao porcao) {
        this.porcao = porcao;
    }

    // Métodos de instância

    /**
     * Cria uma cópia do acompanhamento.
     *
     * @return cópia do acompanhamento
     */
    @Override
    public Acompanhamento clone() {
        return new Acompanhamento(this);
    }

    /**
     * Devolve uma representação textual do acompanhamento.
     *
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Acompanhamento {");
        sb.append("codProduto=").append(getCodProduto());
        sb.append(", designacao=").append(getDesignacao());
        sb.append(", custo=").append(getCusto());
        sb.append(", tempoConfecao=").append(getTempoConfecao());
        sb.append(", porcao=").append(porcao);
        sb.append("}");

        return sb.toString();
    }

    /**
     * Compara este acompanhamento com outro objeto.
     *
     * @param obj objeto a comparar
     * @return {@code true} se forem iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        Acompanhamento other = (Acompanhamento) obj;
        return porcao == other.porcao;
    }

}
