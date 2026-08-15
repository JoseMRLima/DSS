package uminho.dss.thegrill.business.ssrefeicoes;

import java.util.List;
import java.util.UUID;

/**
 * Classe que representa um Prato principal.
 * Um Prato é um tipo de {@link Produto} que não possui atributos adicionais,
 * herdando todo o seu comportamento da classe Produto.
 */
public class Prato extends Produto {

    // Construtores

    /**
     * Construtor por omissão de Prato.
     */
    public Prato() {
        super();
    }

    /**
     * Construtor parametrizado de Prato.
     *
     * @param codProduto código identificador do produto
     * @param designacao designação do prato
     * @param custo custo do prato
     * @param tempoConfecao tempo de confeção do prato
     * @param obrigatorios lista de códigos de ingredientes obrigatórios
     * @param alternativos lista de códigos de ingredientes alternativos
     * @param substituiveis lista de códigos de ingredientes substituíveis
     */
    public Prato(UUID codProduto,
                 String designacao,
                 float custo,
                 int tempoConfecao,
                 List<UUID> obrigatorios,
                 List<UUID> alternativos,
                 List<UUID> substituiveis) {
        super(codProduto, designacao, custo, tempoConfecao,
                obrigatorios, alternativos, substituiveis);
    }

    /**
     * Construtor de cópia de Prato.
     * @param other prato a copiar
     */
    public Prato(Prato other) {
        super(other);
    }

    // Métodos de instância

    /**
     * Cria uma cópia do prato.
     * @return cópia do prato
     */
    @Override
    public Prato clone() {
        return new Prato(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Prato Principal {");
        builder.append("codProduto=").append(getCodProduto());
        builder.append(", designacao=").append(getDesignacao());
        builder.append(", custo=").append(getCusto());
        builder.append(", tempoConfecao=").append(getTempoConfecao());
        builder.append("}");

        return builder.toString();
    }

}
