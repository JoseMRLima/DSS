package uminho.dss.thegrill.ui;

import uminho.dss.thegrill.business.ITheGrillLN;

import java.util.Scanner;

public class MenuInicialUI {

    private final ITheGrillLN model;
    private final Scanner input;

    public MenuInicialUI(ITheGrillLN model, Scanner input) {
        this.model = model;
        this.input = input;
    }

    public void run() {

        while (true) {

            Menu menu = new Menu("Menu Inicial", input,
                    new String[]{"Cliente", "Funcionário", "Sair"});

            menu.setHandler(1, () -> {
                ClienteUI clienteUI = new ClienteUI(model, input);
                clienteUI.run();
            });

            menu.setHandler(2, () -> {
                FuncionarioUI funcionarioUI = new FuncionarioUI(model, input);
                funcionarioUI.run();
            });

            menu.setHandler(3, () -> {
                System.out.println("Até breve!");
                return;
            });

            menu.run();

            break;
        }
    }
}