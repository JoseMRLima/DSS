package uminho.dss.thegrill.business.sspedidos;

import uminho.dss.thegrill.business.ssrefeicoes.*;

public class ItemPedido {

    private Produto produto;
    private Porcao porcao; // null para prato

    public ItemPedido(Produto produto, Porcao porcao) {
        this.produto = produto;
        this.porcao = porcao;
    }

    public Produto getProduto() {
        return produto;
    }

    public Porcao getPorcao() {
        return porcao;
    }

    public float getPrecoFinal() {
        float base = produto.getCusto();

        if (porcao == null) return base;

        return switch (porcao) {
            case PEQUENO -> base * 0.8f;
            case MEDIO -> base;
            case GRANDE -> base * 1.3f;
        };
    }
}