package uminho.dss.thegrill.ui;

import uminho.dss.thegrill.business.ITheGrillLN;
import java.util.Scanner;

public class TheGrillApp {

    private ITheGrillLN model;
    private Scanner input;
    private ClienteUI clienteUI;
    private FuncionarioUI funcionarioUI;

    public TheGrillApp(ITheGrillLN model, Scanner input) {
        this.model = model;
        this.input = input;
        this.clienteUI = new ClienteUI(model, input);
        this.funcionarioUI = new FuncionarioUI(model, input);
    }

    public void run() {
        Menu menu = new Menu("Menu Inicial", input, new String[] {
                "Cliente",
                "Funcionário",
                "Sair"
        });

        menu.setHandler(1, clienteUI::run);
        menu.setHandler(2, funcionarioUI::run);
        menu.setHandler(3, () -> System.exit(0));

        menu.run();
    }
}