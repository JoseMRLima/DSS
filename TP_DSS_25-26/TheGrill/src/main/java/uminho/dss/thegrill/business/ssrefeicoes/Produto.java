package uminho.dss.thegrill.business.ssrefeicoes;

import uminho.dss.thegrill.data.IngredienteDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Classe abstrata que representa um Produto.
 * Um produto pode ser um Prato, Bebida ou Acompanhamento.
 */
public abstract class Produto {

    // variáveis de instância

    /** Código único do produto */
    private UUID codProduto;

    /** Designação do produto */
    private String designacao;

    /** Custo base do produto */
    private float custo;

    /** Tempo de confeção em minutos */
    private int tempoConfecao;

    /** Ingredientes obrigatórios */
    protected List<UUID> ingrObrigatorios;

    /** Ingredientes alternativos */
    protected List<UUID> ingrAlternativos;

    /** Ingredientes substituíveis */
    protected List<UUID> ingrSubstituiveis;

    /** DAO de acesso aos ingredientes */
    protected Map<UUID, Ingrediente> ingredientes;


    // Construtores

    /**
     * Construtor por omissão de Produto.
     */
    public Produto() {
        this.codProduto = UUID.randomUUID();
        this.designacao = "";
        this.custo = 0;
        this.tempoConfecao = 0;
        this.ingrObrigatorios = new ArrayList<>();
        this.ingrAlternativos = new ArrayList<>();
        this.ingrSubstituiveis = new ArrayList<>();
        this.ingredientes = IngredienteDAO.getInstance();
    }

    /**
     * Construtor parametrizado de Produto.
     *
     * @param codProduto    código do produto
     * @param designacao    designação do produto
     * @param custo         custo base
     * @param tempoConfecao tempo de confeção
     * @param obrigatorios  ingredientes obrigatórios
     * @param alternativos  ingredientes alternativos
     * @param substituiveis ingredientes substituíveis
     */
    public Produto(UUID codProduto,
                   String designacao,
                   float custo,
                   int tempoConfecao,
                   List<UUID> obrigatorios,
                   List<UUID> alternativos,
                   List<UUID> substituiveis) {

        this.codProduto = codProduto;
        this.designacao = designacao;
        this.custo = custo;
        this.tempoConfecao = tempoConfecao;
        this.ingrObrigatorios = new ArrayList<>(obrigatorios);
        this.ingrAlternativos = new ArrayList<>(alternativos);
        this.ingrSubstituiveis = new ArrayList<>(substituiveis);
        this.ingredientes = IngredienteDAO.getInstance();
    }

    /**
     * Construtor de cópia de Produto.
     *
     * @param other produto a copiar
     */
    public Produto(Produto other) {
        this.codProduto = other.codProduto;
        this.designacao = other.designacao;
        this.custo = other.custo;
        this.tempoConfecao = other.tempoConfecao;
        this.ingrObrigatorios = new ArrayList<>(other.ingrObrigatorios);
        this.ingrAlternativos = new ArrayList<>(other.ingrAlternativos);
        this.ingrSubstituiveis = new ArrayList<>(other.ingrSubstituiveis);
        this.ingredientes = IngredienteDAO.getInstance();
    }


    // Getters e Setters

    /**
     * @return código do produto
     */
    public UUID getCodProduto() {
        return codProduto;
    }

    /**
     * @return designação do produto
     */
    public String getDesignacao() {
        return designacao;
    }

    /**
     * Altera a designação do produto.
     *
     * @param designacao nova designação
     */
    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    /**
     * @return custo do produto
     */
    public float getCusto() {
        return custo;
    }

    /**
     * Altera o custo do produto.
     *
     * @param custo novo custo
     */
    public void setCusto(float custo) {
        this.custo = custo;
    }

    /**
     * @return tempo de confeção
     */
    public int getTempoConfecao() {
        return tempoConfecao;
    }

    /**
     * Altera o tempo de confeção.
     *
     * @param tempoConfecao novo tempo
     */
    public void setTempoConfecao(int tempoConfecao) {
        this.tempoConfecao = tempoConfecao;
    }

    // Métodos de instância

    /**
     * Devolve a lista de alergénicos associados ao produto.
     * Um ingrediente é considerado alergénico se for instância de Alergenico.
     *
     * @return lista de alergénicos do produto
     */
    public List<Alergenico> listarAlergenicos() {
        List<Alergenico> res = new ArrayList<>();

        // Ingredientes obrigatórios
        for (UUID codIng : ingrObrigatorios) {
            Ingrediente ing = ingredientes.get(codIng);
            if (ing instanceof Alergenico) {
                res.add((Alergenico) ing);
            }
        }

        // Ingredientes alternativos
        for (UUID codIng : ingrAlternativos) {
            Ingrediente ing = ingredientes.get(codIng);
            if (ing instanceof Alergenico) {
                res.add((Alergenico) ing);
            }
        }

        return res;
    }

    /**
     * Devolve a lista de ingredientes alternativos do produto.
     *
     * @return lista de ingredientes alternativos
     */
    public List<Ingrediente> listarIngredientesAlternativos() {
        List<Ingrediente> res = new ArrayList<>();

        for (UUID codIng : ingrAlternativos) {
            Ingrediente ing = ingredientes.get(codIng);
            if (ing != null) {
                res.add(ing);
            }
        }

        return res;
    }

    /**
     * Substitui um ingrediente substituivel por outro ingrediente.
     * A substituição só ocorre se o ingrediente de entrada existir
     * na lista de ingredientes alternativos.
     *
     * @param codIngSaida ingrediente a remover
     * @param codIngEntr ingrediente a adicionar
     */
    public void substituirIngrediente(UUID codIngSaida, UUID codIngEntr) {
        this.removerIngrediente(codIngSaida);
        this.adicionarIngrediente(codIngEntr);
    }

    /**
     * Adiciona um ingrediente obrigatório ao produto.
     * O ingrediente só é adicionado se existir na lista
     * de ingredientes alternativos ou obrigatorios(pode ter mais do que 1).
     *
     * @param codIngrediente código do ingrediente a adicionar
     */
    public void adicionarIngrediente(UUID codIngrediente) {
        if (this.ingrAlternativos.contains(codIngrediente)||this.ingrObrigatorios.contains(codIngrediente)) {
            this.ingrSubstituiveis.add(codIngrediente);
        }
    }

    /**
     * Remove um ingrediente do produto.
     * Só permite remover se estiver nos Substituíveis.
     * Os Obrigatórios nunca são tocados.
     *
     * @param codIngrediente código do ingrediente a remover
     */
    public void removerIngrediente(UUID codIngrediente) {
        // Remove apenas a primeira ocorrência encontrada na lista dinâmica
        this.ingrSubstituiveis.remove(codIngrediente);
    }

    /**
     * Retorna a lista FINAL de ingredientes necessários para confecionar este produto específico.
     * Junta os Obrigatórios (que nunca mudam) + Substituíveis (que têm as alterações do cliente).
     */
    public List<UUID> getCodsFinais() {
        List<UUID> finais = new ArrayList<>();
        finais.addAll(this.ingrObrigatorios);
        finais.addAll(this.ingrSubstituiveis);
        return finais;
    }

    /**
     * Verifica se dois produtos são iguais.
     * Dois produtos são considerados iguais se tiverem o mesmo código.
     *
     * @param obj objeto a comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Produto other = (Produto) obj;
        return codProduto.equals(other.codProduto);
    }

    /**
     * Devolve a lista de IDs dos ingredientes obrigatórios.
     * @return Lista de UUIDs
     */
    public List<UUID> getCodsObrigatorios() {
        return new ArrayList<>(this.ingrObrigatorios);
    }

    /**
     * Devolve a lista de IDs dos ingredientes alternativos.
     * @return Lista de UUIDs
     */
    public List<UUID> getCodsAlternativos() {
        return new ArrayList<>(this.ingrAlternativos);
    }

    /**
     * Devolve a lista de IDs dos ingredientes de substituição.
     * @return Lista de UUIDs
     */
    public List<UUID> getCodsSubstituiveis() {
        return new ArrayList<>(this.ingrSubstituiveis);
    }

    /**
     * Devolve a representação textual do produto.
     * @return string representativa do produto
     */
    @Override
    public abstract String toString();

    /**
     * Cria uma cópia do produto.
     * @return cópia do produto
     */
    @Override
    public abstract Produto clone();


}

