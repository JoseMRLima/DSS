package uminho.dss.thegrill.ui;

import uminho.dss.thegrill.business.ITheGrillLN;
import uminho.dss.thegrill.business.sspedidos.TalaoLevantamento;
import uminho.dss.thegrill.business.ssrefeicoes.*;
import uminho.dss.thegrill.business.ssrefeicoes.Refeicao; // Importar explicitamente se necessário
import uminho.dss.thegrill.data.IngredienteDAO;

import java.util.*;
import java.util.stream.Collectors;

public class ClienteUI {

    private ITheGrillLN model;
    private Scanner input;

    private boolean pedidoConcluido = false;

    private List<Produto> produtosPedidoAtual = new ArrayList<>();
    private UUID codRefeicaoAtual = null;
    private UUID codPedidoAtual = null; // Guardamos o pedido após registar

    private IngredienteDAO ingDAO = IngredienteDAO.getInstance();

    private Random random = new Random();

    public ClienteUI(ITheGrillLN model, Scanner input) {
        this.model = model;
        this.input = input;
    }

    // MENU CLIENTE

    public void run() {
        Menu menu = new Menu("Menu Cliente", input, new String[]{
                "Criar Pedido",
                "Consultar Pedido"
        });

        menu.setHandler(1, this::criarPedido);
        menu.setHandler(2, () ->
                System.out.println("Consultar pedido (fora do âmbito do TP)"));

        menu.run();
    }

    // CRIAR PEDIDO

    private void criarPedido() {
        Menu menu = new Menu("Criar Pedido", input, new String[]{
                "Refeição Menu",
                "Refeição Item-a-Item"
        });

        menu.setHandler(1, this::criarRefeicaoMenu);
        menu.setHandler(2, this::criarRefeicaoItemItem);

        menu.run();
    }

    // REFEIÇÃO MENU

    private void criarRefeicaoMenu() {
        produtosPedidoAtual.clear();
        pedidoConcluido = false;
        codPedidoAtual = null;

        Prato pratoBase = escolherProduto("Prato Principal", model.listarPratosPrincipais());
        if (pratoBase == null) return;

        Bebida bebidaBase = escolherProduto("Bebida", model.listarBebidas());
        if (bebidaBase == null) return;

        escolherPorcao(); // Apenas visual, para simular escolha

        Acompanhamento acompBase = escolherProduto("Acompanhamento", model.listarAcompanhamentos());
        if (acompBase == null) return;

        escolherPorcao();

        // Guardamos clones para manipulação local antes de enviar
        produtosPedidoAtual.add(pratoBase.clone());
        produtosPedidoAtual.add(bebidaBase.clone());
        produtosPedidoAtual.add(acompBase.clone());

        // Cria a refeição no sistema
        codRefeicaoAtual = model.criarRefeicaoMenu(
                (Prato) produtosPedidoAtual.get(0),
                (Bebida) produtosPedidoAtual.get(1),
                (Acompanhamento) produtosPedidoAtual.get(2)
        );

        while (!mostrarResumoPedido());
    }

    // ITEM-A-ITEM

    private void criarRefeicaoItemItem() {
        produtosPedidoAtual.clear();
        pedidoConcluido = false;
        codPedidoAtual = null;

        while (true) {
            System.out.println("\n=== Refeição Item-a-Item ===");

            if (produtosPedidoAtual.isEmpty())
                System.out.println("(nenhum produto)");
            else
                produtosPedidoAtual.forEach(p -> System.out.println("- " + p.getDesignacao()));

            System.out.println("\n1 - Adicionar Produto");
            System.out.println("2 - Finalizar");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            int op = -1;
            try {
                op = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e) {
                input.nextLine();
            }

            switch (op) {
                case 1 -> adicionarProdutoItemItem();
                case 2 -> {
                    if (!produtosPedidoAtual.isEmpty()) {
                        codRefeicaoAtual = model.criarRefeicaoItemItem(produtosPedidoAtual);
                        while (!mostrarResumoPedido());
                        return;
                    } else {
                        System.out.println("Adicione pelo menos um produto.");
                    }
                }
                case 0 -> {
                    produtosPedidoAtual.clear();
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void adicionarProdutoItemItem() {
        // Filtra para não mostrar produtos já escolhidos (opcional)
        List<Produto> disponiveis = model.listarProdutos();

        Produto escolhido = escolherProduto("Adicionar Produto", disponiveis);
        if (escolhido != null)
            produtosPedidoAtual.add(escolhido.clone());
    }

    // RESUMO

    private boolean mostrarResumoPedido() {
        System.out.println("\n=== Resumo ===");
        produtosPedidoAtual.forEach(p -> System.out.println("- " + p.getDesignacao()));

        Menu menu = new Menu("Opções", input,
                new String[]{"Alterar Produto", "Confirmar Pedido", "Cancelar"});

        menu.setHandler(1, this::alterarProduto);
        menu.setHandler(2, this::confirmarPedido);
        menu.setHandler(3, this::cancelarPedido);

        menu.run();

        return pedidoConcluido || produtosPedidoAtual.isEmpty();
    }

    // ALTERAR PRODUTO

    private void alterarProduto() {
        if (produtosPedidoAtual.isEmpty()) return;

        String[] opcoes = produtosPedidoAtual.stream()
                .map(Produto::getDesignacao)
                .toArray(String[]::new);

        Menu menu = new Menu("Alterar Produto", input, opcoes);

        for (int i = 0; i < produtosPedidoAtual.size(); i++) {
            Produto produto = produtosPedidoAtual.get(i);
            menu.setHandler(i + 1, () -> alterarIngredientes(produto));
        }

        menu.run();
    }

    // ALTERAR INGREDIENTES

    private void alterarIngredientes(Produto produto) {

        IngredienteDAO ingDAO = IngredienteDAO.getInstance();
        List<UUID> alternativosFixos =
                new ArrayList<>(produto.getCodsAlternativos());

        while (true) {
            System.out.println("\n=== " + produto.getDesignacao() + " ===");

            System.out.println("\nObrigatórios:");
            for (UUID id : produto.getCodsObrigatorios()) {
                Ingrediente ing = ingDAO.get(id);
                if (ing != null)
                    System.out.println(" - " + ing.getNome());
                else
                    System.out.println(" - [Ingrediente inexistente]");
            }

            List<UUID> subs = produto.getCodsSubstituiveis();
            int idx = 1;

            System.out.println("\nSubstituíveis (Removíveis):");
            for (UUID s : subs) {
                Ingrediente ing = ingDAO.get(s);
                if (ing != null)
                    System.out.println((idx++) + " - " + ing.getNome());
                else
                    System.out.println((idx++) + " - [Ingrediente inexistente]");
            }

            System.out.println("\nAlternativos (Adicionáveis):");
            for (UUID a : alternativosFixos) {
                Ingrediente ing = ingDAO.get(a);
                if (ing != null)
                    System.out.println((idx++) + " - " + ing.getNome());
                else
                    System.out.println((idx++) + " - [Ingrediente inexistente]");
            }

            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            int op = input.nextInt();
            input.nextLine();

            if (op == 0) return;

            if (op <= subs.size()) {
                gerirIngredienteSubstituivel(produto, subs.get(op - 1));
            } else {
                int indexAlt = op - subs.size() - 1;

                if (indexAlt >= 0 && indexAlt < alternativosFixos.size()) {
                    UUID ingrediente = alternativosFixos.get(indexAlt);

                    if (produto.getCodsObrigatorios().contains(ingrediente)
                            || produto.getCodsSubstituiveis().contains(ingrediente)) {
                        System.out.println("Este ingrediente já está no produto.");
                    } else {
                        model.adicionarIngrediente(produto.getCodProduto(), ingrediente);
                        System.out.println("Ingrediente adicionado!");
                    }
                }
            }
        }
    }


    private void gerirIngredienteSubstituivel(Produto produto, UUID codIng) {
        Ingrediente ing = ingDAO.get(codIng);
        String nomeIng = (ing != null) ? ing.toString() : "Ingrediente";

        Menu menu = new Menu("Ingrediente: " + nomeIng, input,
                new String[]{"Substituir", "Remover"});

        menu.setHandler(1, () -> substituirIngrediente(produto, codIng));
        menu.setHandler(2, () -> {
            // CORREÇÃO: Chamar a Facade
            model.removerIngrediente(produto.getCodProduto(), codIng);
            System.out.println("Ingrediente removido.");
        });

        menu.run();
    }

    private void substituirIngrediente(Produto produto, UUID codSaida) {
        // CORREÇÃO: getCodsAlternativos
        List<UUID> alts = produto.getCodsAlternativos();

        if (alts.isEmpty()) {
            System.out.println("Não há ingredientes para substituição.");
            return;
        }

        System.out.println("\nSubstituir por:");
        for (int i = 0; i < alts.size(); i++) {
            Ingrediente ing = ingDAO.get(alts.get(i));
            System.out.println((i + 1) + " - " + (ing != null ? ing.toString() : alts.get(i)));
        }

        System.out.print("Opção: ");
        int op = input.nextInt();
        input.nextLine();

        if (op >= 1 && op <= alts.size()) {
            // CORREÇÃO: Chamar a Facade
            model.substituirIngrediente(produto.getCodProduto(), alts.get(op - 1), codSaida);
            System.out.println("Ingrediente substituído.");
        }
    }

    // PAGAMENTO E CONFIRMAÇÃO

    private void confirmarPedido() {
        if (codRefeicaoAtual == null) {
            System.out.println("Erro: Nenhuma refeição criada.");
            return;
        }

        Refeicao refDummy = new RefeicaoItemItem(); // Apenas para transportar o ID

        UUID restauranteId = UUID.randomUUID();

        Refeicao proxyRefeicao;
        if (produtosPedidoAtual.size() == 3 && produtosPedidoAtual.get(0) instanceof Prato) {
            proxyRefeicao = new RefeicaoMenu((Prato)produtosPedidoAtual.get(0), (Bebida)produtosPedidoAtual.get(1), (Acompanhamento)produtosPedidoAtual.get(2));
        } else {
            proxyRefeicao = new RefeicaoItemItem(codRefeicaoAtual, produtosPedidoAtual);
        }

        codPedidoAtual = model.registarPedido(proxyRefeicao, restauranteId);

        System.out.println("\nPedido registado com sucesso! (ID: " + codPedidoAtual + ")");

        // Calcular e mostrar preço
        float total = model.calcularValorPedido(codPedidoAtual);
        System.out.printf("Total a pagar: %.2f €\n", total);

        menuPagamento();
        pedidoConcluido = true;
    }

    private void menuPagamento() {
        Menu menu = new Menu("Pagamento", input, new String[]{"MBWay", "Dinheiro", "Cancelar"});

        // Passamos o tipo de pagamento como String
        menu.setHandler(1, () -> processarPagamento("MBWay"));
        menu.setHandler(2, () -> processarPagamento("Dinheiro"));
        menu.setHandler(3, this::cancelarPedido);

        menu.run();
    }

    private void processarPagamento(String tipoPagamento) {
        // Registar o pagamento na Facade para mudar o estado para PAGO e enviar para cozinha
        model.registarPagamento(codPedidoAtual, tipoPagamento);

        System.out.println("Pagamento aceite (" + tipoPagamento + ").");

        // Mostrar tempo de espera
        int tempo = model.getTempoEspera(codPedidoAtual);
        System.out.println("Tempo estimado de espera: " + tempo + " min.");

        // Emitir Senha
        TalaoLevantamento talao = model.gerarTalaoLevantamento(codPedidoAtual);
        int balcao = model.obterBalcaoEntrega(codPedidoAtual);
        System.out.println("\n=== TALÃO DE LEVANTAMENTO ===");
        System.out.println("ID Senha: " + talao.getCodTalaoLev());
        System.out.println("Dirija-se ao Balcão: " + (balcao == 0 ? "A definir" : balcao));
        System.out.println("=============================");

        menuFatura();
    }

    private void menuFatura() {
        Menu menu = new Menu("Deseja fatura com NIF?", input, new String[]{"Sim", "Não"});

        menu.setHandler(1, () -> {
            System.out.print("Introduza o NIF: ");
            String nif = input.next();
            System.out.println("NIF " + nif + " registado.");
            finalizarFluxo();
        });

        menu.setHandler(2, this::finalizarFluxo);

        menu.run();
    }

    private void finalizarPagamento() {
        // Método antigo, substituído por processarPagamento
    }

    private void finalizarFluxo() {
        System.out.println("\nObrigado! O seu pedido está a ser preparado.");
        produtosPedidoAtual.clear();
        codRefeicaoAtual = null;
        codPedidoAtual = null;
    }

    private void cancelarPedido() {
        if (codPedidoAtual != null) {
            model.cancelarPedido(codPedidoAtual);
        }
        produtosPedidoAtual.clear();
        codRefeicaoAtual = null;
        codPedidoAtual = null;
        System.out.println("Pedido cancelado.");
    }

    private <T extends Produto> T escolherProduto(String titulo, List<T> lista) {
        if (lista.isEmpty()) return null;

        System.out.println("\n=== " + titulo + " ===");
        for (int i = 0; i < lista.size(); i++)
            System.out.println((i + 1) + " - " + lista.get(i).getDesignacao());

        System.out.println("0 - Voltar");
        System.out.print("Opção: ");

        int op = -1;
        try {
            op = input.nextInt();
            input.nextLine();
        } catch(InputMismatchException e) {
            input.nextLine();
        }

        return (op > 0 && op <= lista.size()) ? lista.get(op - 1) : null;
    }

    private void escolherPorcao() {
        System.out.println("\nEscolher porção (Meramente ilustrativo):");
        System.out.println("1 - PEQUENO");
        System.out.println("2 - MEDIO");
        System.out.println("3 - GRANDE");
        System.out.print("Opção: ");
        try { input.nextInt(); input.nextLine(); } catch(Exception e) { input.nextLine(); }
    }
}