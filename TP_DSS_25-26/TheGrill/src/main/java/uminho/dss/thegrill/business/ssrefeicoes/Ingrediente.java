package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.UUID;

/**
 * Classe que representa um Ingrediente.
 *
 */
public class Ingrediente {

    /** Código identificador do ingrediente */
    private UUID codIngrediente;

    /** Nome do ingrediente (Ex: "Água", "Tomate") */
    private String nome;

    /** Indica se este ingrediente é considerado um alergénico */
    private boolean isAlergenico;

    /** Quantidade disponível em stock */
    private int stock;

    /** Tempo necessário para solicitar o ingrediente (em minutos) */
    private int tempoSolicitacao;

    // Construtores

    /**
     * Construtor por omissão.
     */
    public Ingrediente() {
        this.codIngrediente = UUID.randomUUID();
        this.nome = "";
        this.isAlergenico = false;
        this.stock = 0;
        this.tempoSolicitacao = 0;
    }

    /**
     * Construtor completo usado pelo DataLoader.
     * @param codIngrediente Código único
     * @param nome Nome do ingrediente
     * @param isAlergenico Se é alergénico (true/false)
     * @param stock Quantidade inicial
     * @param tempoSolicitacao Tempo de reposição
     */
    public Ingrediente(UUID codIngrediente, String nome, boolean isAlergenico, int stock, int tempoSolicitacao) {
        this.codIngrediente = codIngrediente;
        this.nome = nome;
        this.isAlergenico = isAlergenico;
        this.stock = stock;
        this.tempoSolicitacao = tempoSolicitacao;
    }

    /**
     * Construtor de cópia.
     * @param other ingrediente a copiar
     */
    public Ingrediente(Ingrediente other) {
        this.codIngrediente = other.codIngrediente;
        this.nome = other.nome;
        this.isAlergenico = other.isAlergenico;
        this.stock = other.stock;
        this.tempoSolicitacao = other.tempoSolicitacao;
    }

    // Getters e Setters

    /**
     * Devolve o código do ingrediente.
     * @return código do ingrediente
     */
    public UUID getCodIngrediente() {
        return codIngrediente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAlergenico() {
        return isAlergenico;
    }

    public void setAlergenico(boolean alergenico) {
        isAlergenico = alergenico;
    }

    /**
     * Devolve a quantidade disponível em stock.
     * @return quantidade em stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Altera a quantidade disponível em stock.
     * @param stock nova quantidade em stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Devolve o tempo necessário para solicitar o ingrediente.
     * @return tempo de solicitação
     */
    public int getTempoSolicitacao() {
        return tempoSolicitacao;
    }

    /**
     * Altera o tempo necessário para solicitar o ingrediente.
     * @param tempoSolicitacao novo tempo de solicitação
     */
    public void setTempoSolicitacao(int tempoSolicitacao) {
        this.tempoSolicitacao = tempoSolicitacao;
    }

    public void aumentarStock(int quantidade) {
        this.stock += quantidade;
    }

    // Métodos de instância

    /**
     * Cria uma cópia do ingrediente.
     * @return cópia do ingrediente
     */
    public Ingrediente clone() {
        return new Ingrediente(this);
    }

    /**
     * Devolve uma representação textual do ingrediente.
     * @return representação em string do ingrediente
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingrediente {");
        sb.append("codIngrediente=").append(codIngrediente);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", isAlergenico=").append(isAlergenico);
        sb.append(", stock=").append(stock);
        sb.append(", tempoSolicitacao=").append(tempoSolicitacao);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Compara este ingrediente com outro objeto.
     * @param obj objeto a comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Ingrediente other = (Ingrediente) obj;
        return this.codIngrediente.equals(other.codIngrediente) && this.stock == other.stock
                && this.tempoSolicitacao == other.tempoSolicitacao;
    }

}
