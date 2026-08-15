package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.ssrestaurantes.Indicador;
import uminho.dss.thegrill.business.ssrestaurantes.Restaurante;

import java.sql.*;
import java.util.*;

/**
 * Data Acess Object para {@link Indicador}.
 */
public class IndicadorDAO implements Map<UUID, Indicador> {

    // variáveis de classe

    /** instância única de FuncionarioDAO */
    private static IndicadorDAO singleton = null;


    // construtores

    /**
     * Construtor por omissão do Data Acess Object de {@link Indicador}.
     */
    private IndicadorDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS indicador (
                        id CHAR(36) PRIMARY KEY,
                        faturacao DECIMAL(10,2) NOT NULL,
                        tempo_medio_espera INT NOT NULL,
                        numero_pedidos INT NOT NULL DEFAULT 0,
                        restaurante_id CHAR(36) NOT NULL,
                        CONSTRAINT fk_indicador_restaurante
                            FOREIGN KEY (restaurante_id)
                            REFERENCES restaurante(id)
                            ON DELETE CASCADE
                    )
                    """;
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    // métodos de classe

    /**
     * Devolve a instância única de IndicadorDAO.
     * @return instância única de IndicadorDAO
     */
    public static IndicadorDAO getInstance() {
        if (IndicadorDAO.singleton == null) {
            IndicadorDAO.singleton = new IndicadorDAO();
        }
        return IndicadorDAO.singleton;
    }


    // métodos de instância

    /**
     * Devolve o número de pares chave–valor presentes no mapa.
     * @return número de entradas no mapa
     */
    @Override
    public int size() {
        int size = 0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM indicador")) {
            if (rs.next()) {
                size = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return size;
    }

    /**
     * Indica se o mapa não contém entradas.
     * @return {@code true} se estiver vazio, {@code false} caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Verifica se existe uma entrada associada à chave indicada.
     * @param key chave a verificar
     * @return {@code true} se a chave existir, {@code false} caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        if (key == null)
            throw new NullPointerException();
        if (!(key instanceof UUID))
            throw new ClassCastException();

        boolean result;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM indicador WHERE id=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                result = rs.next();
            }
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return result;
    }

    /**
     * Verifica se existe pelo menos uma entrada com o valor indicado.
     * @param value valor a verificar
     * @return {@code true} se o valor existir, {@code false} caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        if (value == null)
            throw new NullPointerException();
        if (!(value instanceof Indicador))
            throw new ClassCastException();

        Indicador i = (Indicador) value;
        // return this.containsKey(i.getCodIndicador());
        return false;
    }

    /**
     * Obtém o valor associado à chave indicada.
     * @param key chave da entrada
     * @return valor associado à chave, ou {@code null} se a chave não existir
     */
    @Override
    public Indicador get(Object key) {
        Indicador i = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM indicador WHERE id=?")) {

            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                // chave existe na tabela
                if (rs.next()) {
                    String codIndicador = rs.getString("id");
                    float faturacao = rs.getFloat("faturacao");
                    int tempoMedioEspera = rs.getInt("tempo_medio_espera");
                    int numPedidos = rs.getInt("numero_pedidos");
                    String codRestaurante = rs.getString("restaurante_id");

                    Restaurante r = RestauranteDAO.getInstance().get(UUID.fromString(codRestaurante));

                    if (r != null) {
                        i = new Indicador(UUID.fromString(codIndicador), faturacao, tempoMedioEspera,numPedidos, r);
                    }
                }
            }
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return i;
    }

    /**
     * Associa o valor indicado à chave indicada.
     * Se a chave já existir, o valor anterior é substituído.
     * @param key chave da entrada
     * @param value valor a associar
     * @return valor anteriormente associado à chave, {@code null} se não existia associação prévia
     */
    @Override
    public Indicador put(UUID key, Indicador value) {
        if (key == null || value == null)
            throw new NullPointerException();

        // remover indicador da base de dados
        Indicador res = this.remove(key);

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO indicador (id, faturacao, tempo_medio_espera, numero_pedidos, restaurante_id) VALUES(?, ?, ?, ?, ?)")) {

            pstm.setString(1, key.toString());
            pstm.setFloat(2, value.getFaturacao());
            pstm.setFloat(3, value.getTempoMedioEspera());
            pstm.setInt(4, value.getNumeroPedidos());
            Restaurante r = value.getRestaurante();
            pstm.setString(5, r.getCodRestaurante().toString());

            pstm.executeUpdate();

        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return res;
    }

    /**
     * Remove a entrada associada à chave indicada.
     * @param key chave da entrada a remover
     * @return valor anteriormente associado à chave, {@code null} se a chave não existir
     */
    @Override
    public Indicador remove(Object key) {
        Indicador i = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM indicador WHERE id=?")) {

            pstm.setString(1, key.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return i;
    }

    /**
     * Copia todas as entradas do mapa fornecido para este mapa.
     * @param map mapa com as entradas a adicionar
     */
    @Override
    public void putAll(Map<? extends UUID, ? extends Indicador> map) {
        for (Indicador i : map.values()) {
            // this.put(i.getCodIndicador(), i);
        }
    }

    /**
     * Remove todas as entradas do mapa.
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE indicador");
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Obtém o conjunto de todas as chaves presentes no mapa.
     * @return conjunto de chaves
     */
    @Override
    public Set<UUID> keySet() {
        Set<UUID> out = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM indicador")) {
            while (rs.next()) {
                String id = rs.getString(1);
                out.add(UUID.fromString(id));
            }
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return out;
    }

    /**
     * Obtém uma coleção com todos os valores presentes no mapa.
     * @return coleção de valores
     */
    @Override
    public Collection<Indicador> values() {
        Collection<Indicador> res = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM indicador")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Indicador i = this.get(UUID.fromString(id));
                res.add(i);
            }
        } catch (Exception e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return res;
    }

    /**
     * Obtém o conjunto de todas as entradas (chave–valor) do mapa.
     * @return conjunto de pares chave–valor
     */
    @Override
    public Set<Entry<UUID, Indicador>> entrySet() {
        Set<Entry<UUID, Indicador>> out = new HashSet<>();
        Entry<UUID, Indicador> et;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM indicador")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Indicador i = this.get(UUID.fromString(id));

                et = new AbstractMap.SimpleEntry<>(UUID.fromString(id), i);
                out.add(et);
            }
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return out;
    }

}
