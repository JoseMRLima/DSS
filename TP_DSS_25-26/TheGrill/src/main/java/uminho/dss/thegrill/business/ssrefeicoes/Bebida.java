package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.List;
import java.util.UUID;

/**
 * Classe que representa uma Bebida.
 *
 */
public class Bebida extends Produto {

    /** Porção da bebida */
    private Porcao porcao;

    // Construtores

    /**
     * Construtor por omissão de Bebida.
     */
    public Bebida() {
        super();
        this.porcao = Porcao.MEDIO;
    }

    /**
     * Construtor parametrizado de Bebida.
     * Usado para criação ou reconstrução de uma bebida completa.
     *
     * @param codProduto código identificador do produto
     * @param designacao designação da bebida
     * @param custo custo da bebida
     * @param tempoConfecao tempo de confeção
     * @param obrigatorios lista de códigos de ingredientes obrigatórios
     * @param alternativos lista de códigos de ingredientes alternativos
     * @param substituiveis lista de códigos de ingredientes substituíveis
     * @param porcao porção da bebida
     */
    public Bebida(UUID codProduto,
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
     * Construtor de cópia de Bebida.
     *
     * @param other bebida a copiar
     */
    public Bebida(Bebida other) {
        super(other);
        this.porcao = other.porcao;
    }

    // Getters e Setters

    /**
     * Devolve a porção da bebida.
     *
     * @return porção da bebida
     */
    public Porcao getPorcao() {
        return porcao;
    }

    /**
     * Altera a porção da bebida.
     *
     * @param porcao nova porção
     */
    public void setPorcao(Porcao porcao) {
        this.porcao = porcao;
    }

    // Métodos de instância

    /**
     * Cria uma cópia da bebida.
     *
     * @return cópia da bebida
     */
    @Override
    public Bebida clone() {
        return new Bebida(this);
    }

    /**
     * Devolve uma representação textual da bebida.
     *
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Bebida {");
        sb.append("codProduto=").append(getCodProduto());
        sb.append(", designacao=").append(getDesignacao());
        sb.append(", custo=").append(getCusto());
        sb.append(", tempoConfecao=").append(getTempoConfecao());
        sb.append(", porcao=").append(porcao);
        sb.append("}");

        return sb.toString();
    }

    /**
     * Compara esta bebida com outro objeto.
     *
     * @param obj objeto a comparar
     * @return {@code true} se forem iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        Bebida other = (Bebida) obj;
        return porcao == other.porcao;
    }

}
