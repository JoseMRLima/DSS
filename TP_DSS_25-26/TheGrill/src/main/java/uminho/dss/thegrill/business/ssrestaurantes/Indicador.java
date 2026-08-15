package uminho.dss.thegrill.business.ssrestaurantes;

import java.util.UUID;

/**
 * Representa os indicadores de desempenho de um restaurante.
 * Contém métricas como faturação e tempo médio de espera.
 */
public class Indicador {

    // variáveis de instância

    private UUID codIndicador;
    private float faturacao;
    private int tempoMedioEspera;
    private int numeroPedidos;
    private Restaurante restaurante;


    // construtores

    /**
     * Construtor por Omissão.
     */
    public Indicador() {
        this.codIndicador = null;
        this.faturacao = 0.0f;
        this.tempoMedioEspera = 0;
        this.numeroPedidos = 0;
        this.restaurante = null;
    }

    /**
     * Construtor de Indicador.
     *
     * @param codIndicador Identificador do indicador
     * @param faturacao Valor total faturado
     * @param tempoMedioEspera Tempo médio de espera em minutos
     */
    public Indicador(UUID codIndicador, float faturacao, int tempoMedioEspera, int numeroPedidos, Restaurante restaurante) {
        this.codIndicador = codIndicador;
        this.faturacao = faturacao;
        this.tempoMedioEspera = tempoMedioEspera;
        this.numeroPedidos = numeroPedidos;
        this.restaurante = restaurante;
    }

    /**
     * Construtor de Cópia.
     */
    public Indicador(Indicador i) {
        this.codIndicador = i.getCodIndicador();
        this.faturacao = i.getFaturacao();
        this.tempoMedioEspera = i.getTempoMedioEspera();
        this.numeroPedidos = i.getNumeroPedidos();
        this.restaurante = i.getRestaurante();
    }


    // métodos de instância

    /**
     * @return Identificador do indicador
     */
    public UUID getCodIndicador() {
        return codIndicador;
    }

    /**
     * @return Valor da faturação
     */
    public float getFaturacao() {
        return faturacao;
    }

    /**
     * @return Tempo médio de espera
     */
    public int getTempoMedioEspera() {
        return tempoMedioEspera;
    }

    /**
     * @return Numero de Pedidos
     */
    public int getNumeroPedidos() { return numeroPedidos; }

    /**
     * @return Restaurante
     */
    public Restaurante getRestaurante() { return restaurante; }

    /**
     * Atualiza a faturação e a média de espera de forma exata.
     */
    public void registarVenda(float valor, int tempoEsperaPedido) {
        this.faturacao += valor;

        // Fórmula: ((Média Atual * Qtd Anterior) + Novo Valor) / Nova Qtd
        int totalTempoAnterior = this.tempoMedioEspera * this.numeroPedidos;
        this.numeroPedidos++;
        this.tempoMedioEspera = (totalTempoAnterior + tempoEsperaPedido) / this.numeroPedidos;
    }

    /**
     * Cria e devolve uma cópia deste indicador.
     * @return Cópia do objeto Indicador
     */
    @Override
    public Indicador clone() {
        return new Indicador(this);
    }

    /**
     * Verifica a igualdade entre dois indicadores.
     * @param obj Objeto a comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Indicador other = (Indicador) obj;
        return this.codIndicador.equals(other.codIndicador) && this.faturacao == other.faturacao
                && this.tempoMedioEspera == other.tempoMedioEspera && this.numeroPedidos == other.numeroPedidos;
    }

    /**
     * Devolve uma representação textual do indicador.
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Indicador {");
        builder.append("código: ").append(this.codIndicador);
        builder.append(", faturação: ").append(this.faturacao);
        builder.append(", tempo médio espera: ").append(this.tempoMedioEspera);
        builder.append('}');

        return builder.toString();
    }

}