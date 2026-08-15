package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.UUID;

/**
 * Classe abstrata que representa uma Refeição.
 * Uma refeição é identificada unicamente por um código (UUID)
 * e define o comportamento comum a todos os tipos de refeição.
 */
public abstract class Refeicao {

    // variáveis de instância

    /** Código identificador único da refeição */
    private UUID codRefeicao;


    // construtores

    /**
     * Construtor por omissão.
     */
    public Refeicao() {
        this.codRefeicao = UUID.randomUUID();
    }

    /**
     * Construtor parametrizado.
     * @param codRefeicao código identificador da refeição
     */
    public Refeicao(UUID codRefeicao) {
        this.codRefeicao = codRefeicao;
    }

    /**
     * Construtor de cópia.
     * @param other refeição a copiar
     */
    public Refeicao(Refeicao other) {
        this.codRefeicao = other.codRefeicao;
    }


    // métodos de instância

    /**
     * Devolve o código identificador da refeição.
     * @return código da refeição
     */
    public UUID getCodRefeicao() {
        return codRefeicao;
    }

    /**
     * Calcula o custo total da refeição.
     * @return custo total da refeição
     */
    public abstract float calcularCustoRefeicao();

    public abstract int calcularTempoPreparacao();

    /**
     * Cria uma cópia da refeição.
     * @return cópia da refeição
     */
    @Override
    public abstract Refeicao clone();

    /**
     * Devolve uma representação textual da refeição.
     * @return representação textual da refeição
     */
    @Override
    public abstract String toString();

    /**
     * Compara esta refeição com outro objeto.
     * @param obj objeto a comparar
     * @return {@code true} se forem iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Refeicao other = (Refeicao) obj;
        return codRefeicao.equals(other.codRefeicao);
    }

}
