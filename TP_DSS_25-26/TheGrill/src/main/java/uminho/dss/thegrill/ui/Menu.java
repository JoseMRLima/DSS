package uminho.dss.thegrill.ui;

import java.util.*;

public class Menu {

    public interface Handler {
        void execute();
    }

    private Scanner input;
    private String nome;
    private List<String> opcoes;
    private List<Handler> handlers;

    public Menu(String nome, Scanner input, String[] opcoes) {
        this.nome = nome;
        this.input = input;
        this.opcoes = Arrays.asList(opcoes);
        this.handlers = new ArrayList<>();
        for (int i = 0; i < opcoes.length; i++) {
            handlers.add(() -> {});
        }
    }

    public void setHandler(int i, Handler h) {
        handlers.set(i - 1, h);
    }

    public void run() {
        int op;
        do {
            show();
            op = readOption();
            if (op > 0 && op <= handlers.size()) {
                handlers.get(op - 1).execute();
            }
        } while (op != 0);
    }

    private void show() {
        System.out.println("\n" + nome);
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i + 1) + " - " + opcoes.get(i));
        }
        System.out.println("0 - Voltar");
    }

    private int readOption() {
        System.out.print("Opção: ");
        try {
            return input.nextInt();
        } catch (Exception e) {
            input.nextLine();
            return -1;
        }
    }
}