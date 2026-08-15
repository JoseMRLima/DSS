package uminho.dss.thegrill.business.ssrestaurantes;

import java.util.UUID;

/**
 * Representa um funcionário genérico do sistema.
 */
public class Funcionario {

    // variáveis de instância

    private UUID codFuncionario;
    private String nome;
    private String email;
    private String senha;


    // construtores

    /**
     * Construtor por Omissão.
     */
    public Funcionario() {
        this.codFuncionario = null;
        this.nome = "";
        this.email = "";
        this.senha = "";
    }

    /**
     * Construtor parametrizado de Funcionario.
     *
     * @param codFuncionario Identificador único do funcionário
     * @param nome Nome do funcionário
     * @param email Email de contacto
     * @param senha Senha de acesso
     */
    public Funcionario(UUID codFuncionario, String nome, String email, String senha) {
        this.codFuncionario = codFuncionario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    /**
     * Construtor de cópia.
     */
    public Funcionario(Funcionario f) {
        this.codFuncionario = f.getCodFuncionario();
        this.nome = f.getNome();
        this.email = f.getEmail();
        this.senha = f.getSenha();
    }


    // métodos de instância

    /**
     * @return Identificador único do funcionário
     */
    public UUID getCodFuncionario() {
        return codFuncionario;
    }

    /**
     * @return Nome do funcionário
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return Email do funcionário
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Senha do funcionário
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Verifica a igualdade entre dois funcionários.
     * @param obj Objeto a comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Funcionario other = (Funcionario) obj;
        return this.codFuncionario.equals(other.codFuncionario) && this.nome.equals(other.nome)
                && this.email.equals(other.email) && this.senha.equals(other.senha);
    }

    /**
     * Devolve uma representação textual do funcionário.
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Funcionário {");
        builder.append("código: ").append(this.codFuncionario);
        builder.append(", nome: ").append(this.nome);
        builder.append(", email: ").append(this.email);
        builder.append('}');

        return builder.toString();
    }

}