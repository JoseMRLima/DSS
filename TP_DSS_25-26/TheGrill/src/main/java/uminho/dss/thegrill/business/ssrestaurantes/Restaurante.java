package uminho.dss.thegrill.business.ssrestaurantes;

import uminho.dss.thegrill.data.IndicadorDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Representa um restaurante da cadeia TheGrill.
 */
public class Restaurante {

    // variáveis de instância

    private UUID codRestaurante;
    private String morada;
    private List<UUID> codsIndicadores;
    private Map<UUID, Indicador> indicadores;


    // construtores

    /**
     * Construtor por omissão
     */
    public Restaurante() {
        this.codRestaurante = UUID.randomUUID();
        this.morada = "";
        this.codsIndicadores = new ArrayList<>();
        this.indicadores = IndicadorDAO.getInstance();
    }

    /**
     * Construtor parametrizado de Restaurante
     * @param morada Morada do restaurante
     */
    public Restaurante(String morada, List<UUID> codsIndicadores) {
        this.codRestaurante = UUID.randomUUID();
        this.morada = morada;
        this.codsIndicadores = new ArrayList<>(codsIndicadores);
        this.indicadores = IndicadorDAO.getInstance();
    }

    /**
     * Construtor de cópia
     * @param r O restaurante a ser copiado
     */
    public Restaurante(Restaurante r) {
        this.codRestaurante = r.getCodRestaurante();
        this.morada = r.getMorada();
        this.codsIndicadores = r.getCodsIndicadores();
        this.indicadores = IndicadorDAO.getInstance();
    }

    public Restaurante(UUID codRestaurante, String morada) {
        this.codRestaurante = codRestaurante;
        this.morada = morada;
        this.codsIndicadores = new ArrayList<>();
        this.indicadores = IndicadorDAO.getInstance();
    }


    // métodos de instância

    /**
     * @return Identificador do restaurante
     */
    public UUID getCodRestaurante() {
        return codRestaurante;
    }

    /**
     * @return Morada do restaurante
     */
    public String getMorada() {
        return morada;
    }

    /**
     * Devolve a lista de Indicadores do Restaurante
     * @return lista de Indicadores
     */
    public List<Indicador> listarIndicadores() {
        List<Indicador> resultado = new ArrayList<>();

        for (UUID id : this.codsIndicadores) {
            Indicador ind = this.indicadores.get(id);
            resultado.add(ind);
        }
        return resultado;
    }

    public List<UUID> getCodsIndicadores() {
        return new ArrayList<>(this.codsIndicadores);
    }

    /**
     * Cria uma cópia de um Restaurante
     * @return cópia do Restaurante
     */
    @Override
    public Restaurante clone() {
        return new Restaurante(this);
    }

    /**
     * Verifica a igualdade entre dois restaurantes.
     * @param obj Objeto a comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Restaurante other = (Restaurante) obj;
        return this.codRestaurante.equals(other.codRestaurante) && this.morada.equals(other.morada)
                && this.codsIndicadores.equals(other.codsIndicadores);
    }

    /**
     * Devolve uma representação textual do restaurante.
     * @return representação textual
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Restaurante {");
        builder.append("código: ").append(this.codRestaurante);
        builder.append(", morada:").append(this.morada);
        builder.append(", indicadores: ").append(this.codsIndicadores);
        builder.append('}');

        return builder.toString();
    }

}