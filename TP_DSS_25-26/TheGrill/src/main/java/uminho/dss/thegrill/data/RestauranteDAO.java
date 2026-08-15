package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.ssrestaurantes.Restaurante;

import java.sql.*;
import java.util.*;

/**
 * Data Acess Object para {@link Restaurante}.
 */
public class RestauranteDAO implements Map<UUID, Restaurante> {

    // variáveis de classe

    /** instância única de RestauranteDAO */
    private static RestauranteDAO singleton = null;


    // construtores

    /**
     * Construtor por omissão do Data Acess Object de {@link Restaurante}.
     */
    private RestauranteDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS restaurante (
                        id CHAR(36) PRIMARY KEY,
                        morada VARCHAR(100) NOT NULL
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
     * Devolve a instância única de RestauranteDAO.
     * @return instância única de RestauranteDAO
     */
    public static RestauranteDAO getInstance() {
        if (RestauranteDAO.singleton == null) {
            RestauranteDAO.singleton = new RestauranteDAO();
        }
        return RestauranteDAO.singleton;
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
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM restaurante")) {
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
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM restaurante WHERE id=?")) {
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
        if (!(value instanceof Restaurante))
            throw new ClassCastException();

        Restaurante r = (Restaurante) value;
        // return this.containsKey(r.getCodRestaurante());
        return false;
    }

    /**
     * Obtém o valor associado à chave indicada.
     * @param key chave da entrada
     * @return valor associado à chave, ou {@code null} se a chave não existir
     */
    @Override
    public Restaurante get(Object key) {
        Restaurante r = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM restaurante WHERE id=?")) {

            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                // chave existe na tabela
                if (rs.next()) {
                    String codRestaurante = rs.getString("id");
                    String morada = rs.getString("morada");

                    r = new Restaurante(UUID.fromString(codRestaurante), morada);
                }
            }
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return r;
    }

    /**
     * Associa o valor indicado à chave indicada.
     * Se a chave já existir, o valor anterior é substituído.
     * @param key chave da entrada
     * @param value valor a associar
     * @return valor anteriormente associado à chave, {@code null} se não existia associação prévia
     */
    @Override
    public Restaurante put(UUID key, Restaurante value) {
        if (key == null || value == null)
            throw new NullPointerException();

        // remover restaurante da base de dados
        Restaurante res = this.remove(key);

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO restaurante (id, morada) VALUES(?, ?)")) {
            pstm.setString(1, key.toString());
            pstm.setString(2, value.getMorada());

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
    public Restaurante remove(Object key) {
        Restaurante r = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM restaurante WHERE id=?")) {

            // ON CASCADE elimina os gerentes automaticamente, mas é necessário remover os funcionarios

            List<String> gerentes = new ArrayList<>();

            // obter ids de funcionarios que são gerentes e têm este restaurante como chave estrangeira
            try (PreparedStatement gerente_pstm = conn.prepareStatement("SELECT funcionario_id FROM gerente WHERE restaurante_id=?")) {
                gerente_pstm.setString(1, key.toString());
                try (ResultSet gerentes_rs = gerente_pstm.executeQuery()) {
                    while (gerentes_rs.next()) {
                        gerentes.add(gerentes_rs.getString("funcionario_id"));
                    }
                }
            }

            // remover restaurante
            pstm.setString(1, key.toString());
            pstm.executeUpdate();

            // remover funcionarios
            for (String s : gerentes) {
                try (PreparedStatement ger_pstm = conn.prepareStatement("DELETE FROM funcionario WHERE id=?")) {
                    ger_pstm.setString(1, s);
                    ger_pstm.executeUpdate();
                }
            }

        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return r;
    }

    /**
     * Copia todas as entradas do mapa fornecido para este mapa.
     * @param map mapa com as entradas a adicionar
     */
    @Override
    public void putAll(Map<? extends UUID, ? extends Restaurante> map) {
        for (Restaurante r : map.values()) {
            // this.put(r.getCodRestaurante(), r);
        }
    }

    /**
     * Remove todas as entradas do mapa.
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement gerentes_stm = conn.createStatement();
             PreparedStatement funcionarios_pstm = conn.prepareStatement("DELETE FROM funcionario WHERE tipo=?");
             Statement stm = conn.createStatement()) {

            // apagar entradas da tabela de gerentes
            gerentes_stm.executeUpdate("TRUNCATE gerente");

            // apagar gerentes da tabela de funcionarios
            funcionarios_pstm.setString(1, "GERENTE");
            funcionarios_pstm.executeUpdate();

            // apagar restaurantes
            stm.executeUpdate("TRUNCATE restaurante");
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
             ResultSet rs = stm.executeQuery("SELECT id FROM restaurante")) {
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
    public Collection<Restaurante> values() {
        Collection<Restaurante> res = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM restaurante")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Restaurante r = this.get(UUID.fromString(id));
                res.add(r);
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
    public Set<Entry<UUID, Restaurante>> entrySet() {
        Set<Entry<UUID, Restaurante>> out = new HashSet<>();
        Entry<UUID, Restaurante> et;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM restaurante")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Restaurante r = this.get(UUID.fromString(id));

                et = new AbstractMap.SimpleEntry<>(UUID.fromString(id), r);
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
