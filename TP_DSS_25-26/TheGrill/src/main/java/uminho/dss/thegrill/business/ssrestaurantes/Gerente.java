package uminho.dss.thegrill.business.ssrestaurantes;

import java.util.UUID;

/**
 * Representa um Gerente de restaurante.
 */
public class Gerente extends Funcionario {

    // variáveis de instância

    private Restaurante restaurante;


    // construtores

    /**
     * Construtor por Omissão.
     */
    public Gerente() {
        super();
        this.restaurante = null;
    }

    /**
     * Construtor de Gerente.
     *
     * @param codFuncionario Identificador único
     * @param nome Nome do Gerente
     * @param email Email do Gerente
     * @param senha Senha de acesso
     * @param restaurante O restaurante gerido por este funcionário
     */
    public Gerente(UUID codFuncionario, String nome, String email, String senha, Restaurante restaurante) {
        super(codFuncionario, nome, email, senha);
        this.restaurante = restaurante;
    }

    /**
     * Construtor de cópia.
     */
    public Gerente(Gerente g) {
        super(g);
        this.restaurante = g.getRestaurante();
    }


    // métodos de instância

    /**
     * Obtém o restaurante associado ao gerente.
     * @return O objeto Restaurante
     */
    public Restaurante getRestaurante() {
        return restaurante;
    }

    /**
     * Verifica a igualdade entre dois gerentes.
     * @param obj Objeto a comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Gerente other = (Gerente) obj;
        return super.equals(other) && this.restaurante.equals(other.restaurante);
    }

    /**
     * Devolve uma representação textual do gerente.
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Gerente {");
        builder.append(super.toString());
        builder.append(", restaurante: ").append(this.restaurante.getCodRestaurante());
        builder.append('}');

        return builder.toString();
    }

}