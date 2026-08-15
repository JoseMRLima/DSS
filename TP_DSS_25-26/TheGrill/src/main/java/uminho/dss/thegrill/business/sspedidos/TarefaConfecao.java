package uminho.dss.thegrill.business.sspedidos;

import uminho.dss.thegrill.business.ssrefeicoes.Produto;
import java.util.UUID;

/**
 * Representa uma tarefa de confeção individual de um item de um pedido.
 * Cada tarefa é atribuída a um posto de trabalho específico (Grelha, Fritura, etc.).
 */
public class TarefaConfecao {

    // variáveis de instância

    /** Identificador único da tarefa */
    private UUID idTarefa;
    /** Identificador do pedido a que esta tarefa pertence */
    private UUID idPedido;
    /** Produto a ser confecionado */
    private Produto produto;
    /** Posto de trabalho (GRELHA, FRITURA, BEBIDAS, MONTAGEM, ENTREGA) */
    private String posto;
    /** Estado da tarefa (PENDENTE, EM_CONFECAO, PRONTO) */
    private EstadoPedido estado;
    /** Nota do pedido copiada para orientação do cozinheiro */
    private String nota;


    // construtores

    /**
     * Construtor por omissão.
     * Inicializa a tarefa com valores nulos ou vazios.
     */
    public TarefaConfecao() {
        this.idTarefa = UUID.randomUUID();
        this.idPedido = null;
        this.produto = null;
        this.posto = "";
        this.estado = EstadoPedido.INICIADO;
        this.nota = "";
    }

    /**
     * Construtor de cópia.
     * @param t Tarefa a copiar
     */
    public TarefaConfecao(TarefaConfecao t) {
        this.idTarefa = t.getIdTarefa();
        this.idPedido = t.getIdPedido();
        this.produto = t.getProduto();
        this.posto = t.getPosto();
        this.estado = t.getEstado();
        this.nota = t.getNota();
    }

    /**
     * Construtor parametrizado para criar uma nova tarefa.
     * @param idPedido Identificador do pedido pai
     * @param produto  Produto a confecionar
     * @param posto    Posto de destino
     * @param estado   EstadoPedido
     * @param nota     Nota geral do pedido
     */
    public TarefaConfecao(UUID idTarefa, UUID idPedido, Produto produto, String posto, EstadoPedido estado, String nota) {
        this.idTarefa = idTarefa;
        this.idPedido = idPedido;
        this.produto = produto;
        this.posto = posto;
        this.estado = estado;
        this.nota = nota;
    }

    public TarefaConfecao(UUID idPedido, Produto produto, String posto, EstadoPedido estado, String nota) {
        this.idTarefa = UUID.randomUUID(); // Gera novo ID
        this.idPedido = idPedido;
        this.produto = produto;
        this.posto = posto;
        this.estado = estado;
        this.nota = nota;
    }

    // métodos de instância

    /**
     * @return O identificador da tarefa
     */
    public UUID getIdTarefa() {
        return idTarefa;
    }

    /**
     * @return O identificador do pedido associado
     */
    public UUID getIdPedido() {
        return idPedido;
    }

    /**
     * @return O produto a confecionar
     */
    public Produto getProduto() {
        return produto.clone();
    }

    /**
     * @return O posto de trabalho atribuído
     */
    public String getPosto() {
        return posto;
    }

    /**
     * @return O estado atual da tarefa
     */
    public EstadoPedido getEstado() {
        return estado;
    }

    /**
     * @param estado Novo estado da tarefa
     */
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    /**
     * @return A nota informativa da tarefa
     */
    public String getNota() {
        return nota;
    }

    /**
     * @param nota Nova nota informativa
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * Cria uma cópia da tarefa.
     * @return Cópia do objeto
     */
    @Override
    public TarefaConfecao clone() {
        return new TarefaConfecao(this);
    }

    /**
     * Compara esta tarefa com outro objeto.
     * @param obj objeto a comparar
     * @return {@code true} se forem iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        TarefaConfecao other = (TarefaConfecao) obj;
        return this.idTarefa.equals(other.idTarefa)
                && this.idPedido.equals(other.idPedido)
                && (this.produto != null && this.produto.equals(other.produto))
                && this.posto.equals(other.posto)
                && this.estado == other.estado
                && this.nota.equals(other.nota);
    }

    /**
     * Devolve uma representação textual da tarefa.
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("TarefaConfecao {");
        builder.append("id tarefa: ").append(idTarefa);
        builder.append(", id pedido: ").append(idPedido);
        builder.append(", produto: ").append(produto != null ? produto.getDesignacao() : "N/A");
        builder.append(", posto: ").append(posto);
        builder.append(", estado: ").append(estado);
        builder.append(", nota: ").append(nota);
        builder.append("}");

        return builder.toString();
    }
}