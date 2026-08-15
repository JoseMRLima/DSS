package uminho.dss.thegrill.business.sspedidos;

import uminho.dss.thegrill.business.sspedidos.exceptions.PagamentoInexistenteException;
import uminho.dss.thegrill.business.sspedidos.exceptions.PedidoInexistenteException;
import uminho.dss.thegrill.business.ssrefeicoes.*;
import uminho.dss.thegrill.data.*;
import uminho.dss.thegrill.business.ssrestaurantes.Indicador;

import java.util.*;

/**
 * Facade do subsistema de Pedidos.
 */
public class SSPedidosFacade implements ISSPedidos {

    // variáveis de instância

    private Map<UUID, Pedido> pedidos;
    private Map<UUID, Pagamento> pagamentos;
    private List<UUID> codsPedidos;
    private List<UUID> pedidosPorConfecionar;
    private List<UUID> codsPagamento;
    private Map<UUID, TarefaConfecao> tarefas;


    // construtores

    public SSPedidosFacade() {
        this.pedidos = PedidoDAO.getInstance();
        this.pagamentos = PagamentoDAO.getInstance();
        this.tarefas = TarefaDAO.getInstance();

        this.codsPedidos = new ArrayList<>();
        this.pedidosPorConfecionar = new ArrayList<>();
        this.codsPagamento = new ArrayList<>();

        for (Map.Entry<UUID, Pedido> entry : this.pedidos.entrySet()) {
            Pedido p = entry.getValue();

            if (p == null) continue;

            this.codsPedidos.add(entry.getKey());

            if (p.getEstado() == EstadoPedido.PAGO) {
                this.pedidosPorConfecionar.add(p.getCodPedido());
            }
        }
    }



    // métodos de instância

    @Override
    public void adicionarNotaPedido(UUID codPedido, String nota) throws PedidoInexistenteException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");

        ped.setNota(nota);
        this.pedidos.put(codPedido, ped);
    }

    @Override
    public void adicionarPedido(UUID codPedido) throws PedidoInexistenteException {
        boolean exists = this.pedidos.containsKey(codPedido);
        if (!exists)
            throw new PedidoInexistenteException("Pedido inexistente");
        this.pedidosPorConfecionar.add(codPedido);
    }

    @Override
    public void alterarEstadoPedido(UUID codPedido, EstadoPedido estado) throws PedidoInexistenteException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");

        ped.setEstado(estado);
        this.pedidos.put(codPedido, ped);
    }

    /**
     * Atrasa um pedido dado um número de minutos
     *
     * @param codPedido Identificador do pedido
     * @param tempo     número de minutos a adicionar
     */
    @Override
    public void atrasarPedido(UUID codPedido, int tempo) {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");

        ped.atrasarPedido(tempo);
        this.pedidos.put(codPedido, ped);
    }

    /**
     * Calcula o tempo estimado de preparação de um pedido.
     *
     * @param codPedido Identificador do pedido
     * @return Tempo estimado em minutos
     */
    @Override
    public int calcularTempoPreparacao(UUID codPedido) {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");

        return ped.calcularTempoPreparacao();
    }

    /**
     * Devolve o tempo estimado que o cliente terá de esperar pelo pedido
     *
     * @param codPedido itentificador do Pedido
     * @return tempo de espera em minutos
     */
    @Override
    public int getTempoEspera(UUID codPedido) {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");

        return ped.getTempoEspera();
    }

    @Override
    public float calcularValorPedido(UUID codPedido) throws PedidoInexistenteException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");
        return ped.calcularValorPedido();
    }

    @Override
    public void cancelarPedido(UUID codPedido) throws PedidoInexistenteException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");

        ped.setEstado(EstadoPedido.CANCELADO);
        this.pedidos.put(codPedido, ped);
    }

    @Override
    public FaturaPagamento gerarFaturaPagamento(UUID codPagamento) throws PagamentoInexistenteException {
        Pagamento pag = this.pagamentos.get(codPagamento);
        if (pag == null)
            throw new PagamentoInexistenteException("Pagamento Inexistente");
        return pag.gerarFaturaPagamento();
    }

    @Override
    public TalaoCaixaPagamento gerarTalaoCaixaPagamento(UUID codPedido) throws PedidoInexistenteException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");
        return ped.gerarTalaoCaixaPagamento();
    }

    @Override
    public TalaoLevantamento gerarTalaoLevantamento(UUID codPedido) throws PedidoInexistenteException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null)
            throw new PedidoInexistenteException("Pedido inexistente");
        return ped.gerarTalaoLevantamento();
    }

    @Override
    public List<Pedido> listarPedidosAConfecionar() {
        List<Pedido> out = new ArrayList<>();
        for (UUID codP : this.pedidosPorConfecionar) {
            out.add(this.pedidos.get(codP));
        }
        return out;
    }

    @Override
    public int obterBalcaoEntrega(UUID codPedido) throws PedidoInexistenteException {
        Pedido p = this.pedidos.get(codPedido);
        if (p == null)
            throw new PedidoInexistenteException("Pedido inexistente");
        return p.getBalcaoEntrega();
    }

    @Override
    public void registarEmbalamento(UUID codPedido) throws IllegalArgumentException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped != null) {
            ped.setEstado(EstadoPedido.EMBALADO);
            this.pedidos.put(codPedido, ped);
        }
    }

    @Override
    public void registarEmpratamento(UUID codPedido) throws IllegalArgumentException {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped != null) {
            ped.setEstado(EstadoPedido.EMPRATADO);
            this.pedidos.put(codPedido, ped);
        }
    }

    /**
     * Regista que o pedido foi efetivamente entregue no balcão ao cliente.
     */
    @Override
    public void registarEntrega(UUID codPedido) {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped != null) {
            ped.setEstado(EstadoPedido.ENTREGUE);
            this.pedidos.put(codPedido, ped);
        }
    }

    /**
     * Lista os pedidos que já estão finalizados pela cozinha (Embalados ou Empratados)
     * mas que ainda não foram entregues ao balcão.
     */
    public List<Pedido> listarPedidosParaEntrega() {
        List<Pedido> out = new ArrayList<>();
        for (Pedido p : this.pedidos.values()) {
            EstadoPedido est = p.getEstado();
            if (est == EstadoPedido.EMBALADO || est == EstadoPedido.EMPRATADO) {
                out.add(p.clone());
            }
        }
        return out;
    }

    @Override
    public UUID registarPagamento(UUID codPedido, String tipoPagamento) {
        Pedido ped = this.pedidos.get(codPedido);
        if (ped == null) throw new PedidoInexistenteException("Pedido inexistente");

        float total = ped.calcularValorPedido();
        Pagamento pag = new Pagamento(codPedido, total, tipoPagamento);
        UUID codPagamento = pag.getCodPagamento();
        this.pagamentos.put(codPagamento, pag);

        // Atualizar Indicador
        UUID restId = ped.getRestauranteId();
        for (Indicador ind : IndicadorDAO.getInstance().values()) {
            if (ind.getRestaurante().getCodRestaurante().equals(restId)) {
                ind.registarVenda(total, ped.getTempoEspera());
                IndicadorDAO.getInstance().put(ind.getCodIndicador(), ind);
                break;
            }
        }

        ped.setEstado(EstadoPedido.PAGO);
        dividirPedidoEmTarefas(ped);
        this.pedidosPorConfecionar.add(codPedido);
        this.pedidos.put(codPedido, ped);

        return codPagamento;
    }

    @Override
    public UUID registarPedido(Refeicao refeicao, UUID restauranteId)  {
        Pedido ped = new Pedido(refeicao, restauranteId);
        UUID codPedido = ped.getCodPedido();

        this.codsPedidos.add(codPedido);
        this.pedidos.put(codPedido, ped);
        return codPedido;
    }

    private void dividirPedidoEmTarefas(Pedido ped) {
        List<Produto> produtosParaProcessar = new ArrayList<>();
        Refeicao ref = ped.getRefeicao();

        if (ref instanceof RefeicaoMenu) {
            RefeicaoMenu menu = (RefeicaoMenu) ref;
            if (menu.getPrato() != null) produtosParaProcessar.add(menu.getPrato());
            if (menu.getAcompanhamento() != null) produtosParaProcessar.add(menu.getAcompanhamento());
            if (menu.getBebida() != null) produtosParaProcessar.add(menu.getBebida());
        } else if (ref instanceof RefeicaoItemItem) {
            RefeicaoItemItem itemItem = (RefeicaoItemItem) ref;
            produtosParaProcessar.addAll(itemItem.getProdutos());
        }

        // 2. Criar e guardar tarefas para cada produto
        for (Produto p : produtosParaProcessar) {
            String posto = determinarPosto(p);
            TarefaConfecao tarefa = new TarefaConfecao(ped.getCodPedido(), p, posto, EstadoPedido.PAGO, ped.getNota()
            );

            this.tarefas.put(tarefa.getIdTarefa(), tarefa);
        }
    }

    private String determinarPosto(Produto p) {
        if (p instanceof Prato) return "GRELHA";
        if (p instanceof Acompanhamento) return "FRITURA";
        if (p instanceof Bebida) return "BEBIDAS";
        return "MONTAGEM";
    }

    @Override
    public List<TarefaConfecao> getTarefasPosto(String posto) {
        // Cast necessário se a variável tarefas estiver tipada como Map genérico
        return ((TarefaDAO) this.tarefas).getTarefasPorPosto(posto);
    }

    @Override
    public void alterarEstadoTarefa(UUID idTarefa, EstadoPedido novoEstado) {
        TarefaConfecao tarefa = this.tarefas.get(idTarefa);
        if (tarefa == null) return;

        tarefa.setEstado(novoEstado);
        this.tarefas.put(idTarefa, tarefa);

        UUID idPedido = tarefa.getIdPedido();
        Pedido pedido = this.pedidos.get(idPedido);
        if (pedido == null) return;

        // Lógica de Sincronização
        if (novoEstado == EstadoPedido.EM_CONFECAO && pedido.getEstado() == EstadoPedido.PAGO) {
            // Se a primeira tarefa começar, o pedido passa a EM_CONFECAO
            pedido.setEstado(EstadoPedido.EM_CONFECAO);
            this.pedidos.put(idPedido, pedido);
        }
        else if (novoEstado == EstadoPedido.PRONTO) {
            // Se uma tarefa termina, verificamos se TODAS as outras do pedido estão prontas
            verificarEAtualizarEstadoPedido(idPedido);
        }
    }

    /**
     * Verifica se todas as tarefas de um pedido estão concluídas.
     */
    private void verificarEAtualizarEstadoPedido(UUID idPedido) {
        List<TarefaConfecao> tarefasPedido = ((TarefaDAO)this.tarefas).getTarefasPorPedido(idPedido);
        boolean todasProntas = true;
        for (TarefaConfecao t : tarefasPedido) {
            if (t.getEstado() != EstadoPedido.PRONTO) {
                todasProntas = false;
                break;
            }
        }

        if (todasProntas) {
            Pedido ped = this.pedidos.get(idPedido);
            ped.setEstado(EstadoPedido.PRONTO);
            this.pedidos.put(idPedido, ped);
        }
    }

    @Override
    public Pedido removerProximoPedido() {
        if (this.pedidosPorConfecionar.isEmpty()) {
            return null;
        }
        UUID codPedido = this.pedidosPorConfecionar.remove(0);
        Pedido ped = this.pedidos.get(codPedido);
        if (ped != null) {
            ped.setEstado(EstadoPedido.EM_CONFECAO);
            this.pedidos.put(codPedido, ped);
        }
        return ped;
    }

    @Override
    public void reordenarListaPedidos() {
        // Ordena a lista baseada no tempo de preparação (os mais rápidos passam para a frente)
        this.pedidosPorConfecionar.sort((id1, id2) -> {
            Pedido p1 = this.pedidos.get(id1);
            Pedido p2 = this.pedidos.get(id2);
            if (p1 == null || p2 == null) return 0;
            return Integer.compare(p1.calcularTempoPreparacao(), p2.calcularTempoPreparacao());
        });
    }

}
