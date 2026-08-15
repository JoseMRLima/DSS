package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.UUID;

/**
 * Classe que representa uma Refeição do tipo Menu.
 * Uma refeição menu é composta por um prato principal, uma bebida e um acompanhamento.
 */
public class RefeicaoMenu extends Refeicao {

    // variáveis de instância

    /** prato principal do menu */
    private Prato prato;
    /** bebida do menu */
    private Bebida bebida;
    /** acompanhamento do menu */
    private Acompanhamento acompanhamento;


    // construtores

    /**
     * Construtor por omissão de uma RefeiçãoMenu.
     */
    public RefeicaoMenu() {
        super();
        this.prato = null;
        this.bebida = null;
        this.acompanhamento = null;
    }

    /**
     * Construtor de criação de uma nova RefeiçãoMenu.
     * O identificador da refeição é gerado automaticamente.
     * @param prato prato principal
     * @param bebida bebida
     * @param acompanhamento acompanhamento
     */
    public RefeicaoMenu(Prato prato,
                        Bebida bebida,
                        Acompanhamento acompanhamento) {
        super();
        this.prato = prato;
        this.bebida = bebida;
        this.acompanhamento = acompanhamento;
    }

    /**
     * Construtor parametrizado de uma RefeiçãoMenu
     * @param codRefeicao identificador da refeição
     * @param prato prato principal
     * @param bebida bebida
     * @param acompanhamento acompanhamento
     */
    public RefeicaoMenu(UUID codRefeicao,
                        Prato prato,
                        Bebida bebida,
                        Acompanhamento acompanhamento) {
        super(codRefeicao);
        this.prato = prato;
        this.bebida = bebida;
        this.acompanhamento = acompanhamento;
    }

    /**
     * Construtor de cópia de uma RefeiçãoMenu.
     * @param other RefeiçãoMenu a copiar
     */
    public RefeicaoMenu(RefeicaoMenu other) {
        super(other);
        this.prato = other.prato;
        this.bebida = other.bebida;
        this.acompanhamento = other.acompanhamento;
    }


    // métodos de instância

    /**
     * Devolve o prato principal do menu.
     * @return prato principal
     */
    public Prato getPrato() {
        return prato;
    }

    /**
     * Devolve a bebida do menu.
     * @return bebida
     */
    public Bebida getBebida() {
        return bebida;
    }

    /**
     * Devolve o acompanhamento do menu.
     * @return acompanhamento
     */
    public Acompanhamento getAcompanhamento() {
        return acompanhamento;
    }

    /**
     * Calcula o custo total da refeição menu, somando o custo do prato, bebida e acompanhamento.
     * @return custo total da refeição
     */
    @Override
    public float calcularCustoRefeicao() {
        // TODO: aplicar desconto
        return prato.getCusto()
                + bebida.getCusto()
                + acompanhamento.getCusto();
    }

    public int calcularTempoPreparacao() {
        int total = 0;

        total += this.prato.getTempoConfecao();
        total += this.bebida.getTempoConfecao();
        total += this.acompanhamento.getTempoConfecao();

        return total;
    }

    /**
     * Cria uma cópia desta RefeiçãoMenu.
     * @return cópia da refeição menu
     */
    @Override
    public RefeicaoMenu clone() {
        return new RefeicaoMenu(this);
    }

    /**
     * Devolve uma representação textual da RefeiçãoMenu
     * @return representação textual da refeição menu
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("RefeicaoMenu {");
        sb.append("codRefeicao=").append(getCodRefeicao());
        sb.append(", prato=").append(prato);
        sb.append(", bebida=").append(bebida);
        sb.append(", acompanhamento=").append(acompanhamento);
        sb.append('}');

        return sb.toString();
    }

    /**
     * Compara esta RefeiçãoMenu com outro objeto.
     * @param obj objeto a comparar
     * @return {@code true} se forem iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        RefeicaoMenu other = (RefeicaoMenu) obj;
        return prato.equals(other.prato)
                && bebida.equals(other.bebida)
                && acompanhamento.equals(other.acompanhamento);
    }

}
