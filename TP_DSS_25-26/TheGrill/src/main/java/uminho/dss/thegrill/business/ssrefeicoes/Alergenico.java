package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.UUID;

/**
 * Classe que representa um Ingrediente do tipo Alergénico.
 * Um alergénico é um ingrediente que pode causar reações adversas,
 * estando associado a um tipo de alergia e a um nível de risco.
 */
public class Alergenico extends Ingrediente {

    /** Tipo de alergia */
    private String tipoAlergia;

    /** Nível de risco associado */
    private String nivelRisco;

    // Construtores

    /**
     * Construtor por omissão.
     * Inicializa o alergénico com valores vazios.
     */
    public Alergenico() {
        super();
        this.tipoAlergia = "";
        this.nivelRisco = "";
    }

    /**
     * Construtor parametrizado.
     *
     * @param codIngrediente código do ingrediente
     * @param stock quantidade em stock
     * @param tempoSolicitacao tempo de solicitação
     * @param tipoAlergia tipo de alergia
     * @param nivelRisco nível de risco
     */
    public Alergenico(UUID codIngrediente, String nome, int stock, int tempoSolicitacao,
                      String tipoAlergia, String nivelRisco) {
        super(codIngrediente, nome, true, stock, tempoSolicitacao);
        this.tipoAlergia = tipoAlergia;
        this.nivelRisco = nivelRisco;
    }

    /**
     * Construtor de cópia.
     *
     * @param other alergénico a copiar
     */
    public Alergenico(Alergenico other) {
        super(other);
        this.tipoAlergia = other.tipoAlergia;
        this.nivelRisco = other.nivelRisco;
    }

    // Getters e Setters

    /**
     * Devolve o tipo de alergia.
     * @return tipo de alergia
     */
    public String getTipoAlergia() {
        return tipoAlergia;
    }

    /**
     * Altera o tipo de alergia.
     * @param tipoAlergia novo tipo de alergia
     */
    public void setTipoAlergia(String tipoAlergia) {
        this.tipoAlergia = tipoAlergia;
    }

    /**
     * Devolve o nível de risco.
     * @return nível de risco
     */
    public String getNivelRisco() {
        return nivelRisco;
    }

    /**
     * Altera o nível de risco.
     * @param nivelRisco novo nível de risco
     */
    public void setNivelRisco(String nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    // Métodos de instância

    /**
     * Cria uma cópia do alergénico.
     * @return cópia do alergénico
     */
    @Override
    public Alergenico clone() {
        return new Alergenico(this);
    }

    /**
     * Devolve uma representação textual do alergénico.
     *
     * @return representação em string do alergénico
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alergenico {");
        sb.append("codIngrediente=").append(getCodIngrediente());
        sb.append(", nome='").append(getNome()).append('\'');
        sb.append(", stock=").append(getStock());
        sb.append(", tempoSolicitacao=").append(getTempoSolicitacao());
        sb.append(", tipoAlergia='").append(tipoAlergia).append('\'');
        sb.append(", nivelRisco='").append(nivelRisco).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Compara este alergénico com outro objeto.
     * @param obj objeto a comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (this.getClass() != obj.getClass()) return false;

        Alergenico other = (Alergenico) obj;
        return tipoAlergia.equals(other.tipoAlergia)
                && nivelRisco.equals(other.nivelRisco);
    }

}
