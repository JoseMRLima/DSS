package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.ssrefeicoes.*;

import java.util.List;
import java.util.UUID;

public class DataLoader {

    private static final UUID CARNE     = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID FRANGO    = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID QUEIJO    = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID BACON     = UUID.fromString("00000000-0000-0000-0000-000000000004");
    private static final UUID ALFACE    = UUID.fromString("00000000-0000-0000-0000-000000000005");
    private static final UUID TOMATE    = UUID.fromString("00000000-0000-0000-0000-000000000006");
    private static final UUID CEBOLA    = UUID.fromString("00000000-0000-0000-0000-000000000007");
    private static final UUID ALHO      = UUID.fromString("00000000-0000-0000-0000-000000000008");
    private static final UUID SAL       = UUID.fromString("00000000-0000-0000-0000-000000000009");
    private static final UUID PIMENTA   = UUID.fromString("00000000-0000-0000-0000-000000000010");
    private static final UUID MANTEIGA  = UUID.fromString("00000000-0000-0000-0000-000000000011");
    private static final UUID ARROZ     = UUID.fromString("00000000-0000-0000-0000-000000000012");
    private static final UUID BATATA    = UUID.fromString("00000000-0000-0000-0000-000000000013");
    private static final UUID AGUA      = UUID.fromString("00000000-0000-0000-0000-000000000014");
    private static final UUID GAS       = UUID.fromString("00000000-0000-0000-0000-000000000015");
    private static final UUID ACUCAR    = UUID.fromString("00000000-0000-0000-0000-000000000016");
    private static final UUID LARANJA   = UUID.fromString("00000000-0000-0000-0000-000000000017");
    private static final UUID GELO      = UUID.fromString("00000000-0000-0000-0000-000000000018");

    public static void carregar() {

        // LIMPAR BD
        PedidoDAO.getInstance().clear();
        RefeicaoDAO.getInstance().clear();
        ProdutoDAO.getInstance().clear();
        IngredienteDAO.getInstance().clear();

        IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();
        ProdutoDAO produtoDAO = ProdutoDAO.getInstance();

        // ===== INGREDIENTES =====
        ingredienteDAO.put(CARNE,    new Ingrediente(CARNE, "Carne", false, 200, 0));
        ingredienteDAO.put(FRANGO,   new Ingrediente(FRANGO, "Frango", false, 200, 0));
        ingredienteDAO.put(QUEIJO,   new Ingrediente(QUEIJO, "Queijo", true, 150, 0));
        ingredienteDAO.put(BACON,    new Ingrediente(BACON, "Bacon", false, 150, 0));
        ingredienteDAO.put(ALFACE,   new Ingrediente(ALFACE, "Alface", false, 100, 0));
        ingredienteDAO.put(TOMATE,   new Ingrediente(TOMATE, "Tomate", false, 100, 0));
        ingredienteDAO.put(CEBOLA,   new Ingrediente(CEBOLA, "Cebola", false, 100, 0));
        ingredienteDAO.put(ALHO,     new Ingrediente(ALHO, "Alho", false, 200, 0));
        ingredienteDAO.put(SAL,      new Ingrediente(SAL, "Sal", false, 500, 0));
        ingredienteDAO.put(PIMENTA,  new Ingrediente(PIMENTA, "Pimenta", false, 300, 0));
        ingredienteDAO.put(MANTEIGA, new Ingrediente(MANTEIGA, "Manteiga", true, 150, 0));
        ingredienteDAO.put(ARROZ,    new Ingrediente(ARROZ, "Arroz", false, 300, 0));
        ingredienteDAO.put(BATATA,   new Ingrediente(BATATA, "Batata", false, 300, 0));
        ingredienteDAO.put(AGUA,     new Ingrediente(AGUA, "Água", false, 500, 0));
        ingredienteDAO.put(GAS,      new Ingrediente(GAS, "Gás", false, 300, 0));
        ingredienteDAO.put(ACUCAR,   new Ingrediente(ACUCAR, "Açúcar", false, 400, 0));
        ingredienteDAO.put(LARANJA,  new Ingrediente(LARANJA, "Laranja", false, 200, 0));
        ingredienteDAO.put(GELO,     new Ingrediente(GELO, "Gelo", false, 500, 0));

        // ===== PRATOS =====
        produtoDAO.put(
                UUID.fromString("10000000-0000-0000-0000-000000000001"),
                new Prato(
                        UUID.fromString("10000000-0000-0000-0000-000000000001"),
                        "Hambúrguer",
                        7.50f,
                        15,
                        List.of(CARNE, QUEIJO, SAL),
                        List.of(BACON, ALFACE, TOMATE, CEBOLA),
                        List.of(BACON)
                )
        );

        produtoDAO.put(
                UUID.fromString("10000000-0000-0000-0000-000000000002"),
                new Prato(
                        UUID.fromString("10000000-0000-0000-0000-000000000002"),
                        "Frango Grelhado",
                        8.00f,
                        12,
                        List.of(FRANGO, SAL),
                        List.of(ALHO, PIMENTA, CEBOLA),
                        List.of(ALHO)
                )
        );

        System.out.println(">>> DataLoader: dados carregados corretamente.");
    }
}
