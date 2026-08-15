package uminho.dss.thegrill;

import uminho.dss.thegrill.business.ITheGrillLN;
import uminho.dss.thegrill.business.TheGrillFacade;
import uminho.dss.thegrill.ui.MenuInicialUI;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        ITheGrillLN model = new TheGrillFacade();

        MenuInicialUI menuInicial = new MenuInicialUI(model, input);
        menuInicial.run();

        input.close();
    }
}