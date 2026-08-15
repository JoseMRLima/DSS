package uminho.dss.thegrill.business.sspedidos;

import uminho.dss.thegrill.business.ssrefeicoes.Refeicao;

import java.util.UUID;

/**
 * Representa um pedido realizado por um cliente.
 */
public class Pedido {

    // variáveis de instância

    private UUID codPedido;
    private EstadoPedido estado;
    private int tempoEspera;
    private String tipoLevantamento;
    private String nota;
    private Refeicao refeicao;
    private int balcaoEntrega;
    private UUID restauranteId;


    // construtores

    public Pedido() {
        this.codPedido = UUID.randomUUID();
        this.estado = EstadoPedido.INICIADO;
        this.tempoEspera = 0;
        this.tipoLevantamento = "";
        this.nota = "";
        this.refeicao = null;
        this.balcaoEntrega = 0;
        this.restauranteId = null;
    }

    /**
     * Construtor de criação de um pedido.
     * @param refeicao Refeição associada
     * @param restauranteId ID do restaurante
     */
    public Pedido(Refeicao refeicao, UUID restauranteId) {
        this.codPedido = UUID.randomUUID();
        this.estado = EstadoPedido.INICIADO;
        this.tempoEspera = 0;
        this.tipoLevantamento = "";
        this.nota = "";
        this.refeicao = refeicao;
        this.balcaoEntrega = 0;
        this.restauranteId = restauranteId;
    }

    public Pedido(Pedido other) {
        this.codPedido = other.getCodPedido();
        this.estado = other.getEstado();
        this.tempoEspera = other.getTempoEspera();
        this.tipoLevantamento = other.getTipoLevantamento();
        this.nota = other.getNota();
        this.refeicao = other.getRefeicao();
        this.balcaoEntrega = other.getBalcaoEntrega();
        this.restauranteId = other.getRestauranteId();
    }

    public Pedido(UUID codPedido, EstadoPedido estado, int tempoEspera, String tipoLevantamento, String nota, Refeicao refeicao, int balcaoEntrega, UUID restauranteId) {
        this.codPedido = codPedido;
        this.estado = estado;
        this.tempoEspera = tempoEspera;
        this.tipoLevantamento = tipoLevantamento;
        this.nota = nota;
        this.refeicao = refeicao;
        this.balcaoEntrega = balcaoEntrega;
        this.restauranteId = restauranteId;
    }


    // métodos de instância

    public UUID getCodPedido() {
        return codPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public void setTempoEspera(int tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    public String getTipoLevantamento() {
        return tipoLevantamento;
    }

    public void setTipoLevantamento(String tipoLevantamento) {
        this.tipoLevantamento = tipoLevantamento;
    }

    public int getBalcaoEntrega() {
        return balcaoEntrega;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Refeicao getRefeicao() {
        return this.refeicao.clone();
    }

    public void setRefeicao(Refeicao refeicao) {
        this.refeicao = refeicao;
    }

    public UUID getRestauranteId() { return restauranteId; }

    public void setRestauranteId(UUID restauranteId) { this.restauranteId = restauranteId; }

    public float calcularValorPedido() {
        return refeicao.calcularCustoRefeicao();
    }

    public void atrasarPedido(int tempo) {
        this.tempoEspera += tempo;
    }

    public int calcularTempoPreparacao() {
        return this.refeicao.calcularTempoPreparacao();
    }

    public TalaoCaixaPagamento gerarTalaoCaixaPagamento() {
        return new TalaoCaixaPagamento(this.codPedido);
    }

    public TalaoLevantamento gerarTalaoLevantamento() {
        return new TalaoLevantamento();
    }

    @Override
    public Pedido clone() {
        return new Pedido(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Pedido other = (Pedido) obj;
        boolean restauranteEquals = (this.restauranteId == null && other.restauranteId == null) ||
                (this.restauranteId != null && this.restauranteId.equals(other.restauranteId));


        return this.codPedido.equals(other.codPedido) && this.estado == other.estado
                && this.tempoEspera == other.tempoEspera && this.tipoLevantamento.equals(other.tipoLevantamento)
                && this.nota.equals(other.nota) && this.refeicao.equals(other.refeicao)
                && this.balcaoEntrega == other.balcaoEntrega
                && restauranteEquals;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Pedido {");
        builder.append("código: ").append(codPedido);
        builder.append(", estado: ").append(estado);
        builder.append(", tempo estimado: ").append(tempoEspera);
        builder.append(", levantamento: ").append(tipoLevantamento);
        builder.append(", nota: ").append(nota);
        builder.append(", refeicao: ").append(refeicao.getCodRefeicao());
        builder.append(", balcao entrega: ").append(balcaoEntrega);
        builder.append(", restaurante: ").append(restauranteId);
        builder.append("}");

        return builder.toString();
    }

}
