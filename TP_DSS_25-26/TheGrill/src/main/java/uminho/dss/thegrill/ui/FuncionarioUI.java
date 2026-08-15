package uminho.dss.thegrill.ui;

import uminho.dss.thegrill.business.ITheGrillLN;
import uminho.dss.thegrill.business.sspedidos.EstadoPedido;
import uminho.dss.thegrill.business.sspedidos.Pedido;
import uminho.dss.thegrill.business.sspedidos.TarefaConfecao;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class FuncionarioUI {

    private ITheGrillLN model;
    private Scanner input;
    private String postoAtual = "MONTAGEM"; // Default

    public FuncionarioUI(ITheGrillLN model, Scanner input) {
        this.model = model;
        this.input = input;
    }

    public void run() {
        System.out.println("\n=== LOGIN FUNCIONÁRIO ===");
        System.out.print("Email: ");
        String email = input.next();
        System.out.print("Senha: ");
        String senha = input.next();

        try {
            if (!model.login(email, senha)) {
                System.out.println("Login inválido!");
                return;
            }
        } catch (Exception e) {
            System.out.println("Erro no login: " + e.getMessage());
            return;
        }

        // Escolher Posto ao iniciar
        escolherPostoTrabalho();

        Menu menu = new Menu("Menu Funcionário (" + this.postoAtual + ")", input, new String[]{
                "Ver Tarefas do Posto",
                "Concluir Tarefa (Marcar PRONTO)",
                "Montagem: Embalar Pedido",
                "Montagem: Empratar Pedido",
                "Entrega: Ver Pedidos Prontos",
                "Entrega: Registar Entrega",
                "Mudar de Posto"
        });

        menu.setHandler(1, this::listarTarefas);
        menu.setHandler(2, this::concluirTarefa);
        menu.setHandler(3, this::embalarPedido);
        menu.setHandler(4, this::empratarPedido);
        menu.setHandler(5, this::listarEntregas);
        menu.setHandler(6, this::registarEntrega);
        menu.setHandler(7, this::escolherPostoTrabalho);

        menu.run();
        model.logout();
    }

    private void escolherPostoTrabalho() {
        System.out.println("\nEscolha o posto:");
        System.out.println("1 - GRELHA");
        System.out.println("2 - FRITURA");
        System.out.println("3 - BEBIDAS");
        System.out.println("4 - MONTAGEM");
        System.out.println("5 - ENTREGA");
        System.out.print("Opção: ");
        int op = input.nextInt();
        switch (op) {
            case 1 -> this.postoAtual = "GRELHA";
            case 2 -> this.postoAtual = "FRITURA";
            case 3 -> this.postoAtual = "BEBIDAS";
            case 4 -> this.postoAtual = "MONTAGEM";
            case 5 -> this.postoAtual = "ENTREGA";
            default -> System.out.println("Opção inválida. Mantém-se " + this.postoAtual);
        }
    }

    private void listarTarefas() {
        List<TarefaConfecao> tarefas = model.getTarefasPosto(this.postoAtual);
        if (tarefas.isEmpty()) System.out.println("Sem tarefas pendentes.");
        else {
            for (TarefaConfecao t : tarefas) System.out.println(t);
        }
    }

    private void concluirTarefa() {
        System.out.print("ID da Tarefa: ");
        String idStr = input.next();
        try {
            model.alterarEstadoTarefa(UUID.fromString(idStr), EstadoPedido.PRONTO);
            System.out.println("Tarefa concluída!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void embalarPedido() {
        if(!postoAtual.equals("MONTAGEM")) { System.out.println("Apenas para MONTAGEM"); return; }
        System.out.print("ID do Pedido: ");
        try { model.registarEmbalamento(UUID.fromString(input.next())); System.out.println("Embalado!"); }
        catch (Exception e) { System.out.println("Erro."); }
    }

    private void empratarPedido() {
        if(!postoAtual.equals("MONTAGEM")) { System.out.println("Apenas para MONTAGEM"); return; }
        System.out.print("ID do Pedido: ");
        try { model.registarEmpratamento(UUID.fromString(input.next())); System.out.println("Empratado!"); }
        catch (Exception e) { System.out.println("Erro."); }
    }

    private void listarEntregas() {
        if(!postoAtual.equals("ENTREGA")) { System.out.println("Apenas para ENTREGA"); return; }
        List<Pedido> p = model.listarPedidosParaEntrega();
        if(p.isEmpty()) System.out.println("Nada para entregar.");
        else p.forEach(System.out::println);
    }

    private void registarEntrega() {
        if(!postoAtual.equals("ENTREGA")) { System.out.println("Apenas para ENTREGA"); return; }
        System.out.print("ID do Pedido: ");
        try { model.registarEntrega(UUID.fromString(input.next())); System.out.println("Entregue!"); }
        catch (Exception e) { System.out.println("Erro."); }
    }
}