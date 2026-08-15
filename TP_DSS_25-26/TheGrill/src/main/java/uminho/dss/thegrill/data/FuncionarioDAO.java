package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.ssrestaurantes.*;

import java.sql.*;
import java.util.*;

/**
 * Data Acess Object para a hierarquia de {@link Funcionario}.
 */
public class FuncionarioDAO implements Map<UUID, Funcionario> {

    // variáveis de classe

    /** instância única de FuncionarioDAO */
    private static FuncionarioDAO singleton = null;


    // construtores

    /**
     * Construtor por omissão do Data Acess Object da hierarquia de {@link Funcionario}.
     */
    private FuncionarioDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            // criar tabela de Funcionario
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS funcionario (
                        id CHAR(36) PRIMARY KEY,
                        nome VARCHAR(50) NOT NULL,
                        email VARCHAR(50) NOT NULL UNIQUE,
                        senha VARCHAR(50) NOT NULL,
                        tipo VARCHAR(30) NOT NULL
                            CHECK (tipo IN ('FUNCIONARIO', 'GERENTE', 'COO'))
                    )
                    """;
            stm.executeUpdate(sql);
            // criar tabela de Gerente
            sql =
                   """
                   CREATE TABLE IF NOT EXISTS gerente (
                       funcionario_id CHAR(36) PRIMARY KEY,
                       restaurante_id CHAR(36) NOT NULL,
                       CONSTRAINT fk_gerente_funcionario
                           FOREIGN KEY (funcionario_id)
                           REFERENCES funcionario(id)
                           ON DELETE CASCADE,
                       CONSTRAINT fk_gerente_restaurante
                           FOREIGN KEY (restaurante_id)
                           REFERENCES restaurante(id)
                           ON DELETE CASCADE
                   )
                   """;
            stm.executeUpdate(sql);
            // criar tabela de COO
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS coo (
                        funcionario_id CHAR(36) PRIMARY KEY,
                        CONSTRAINT fk_coo_funcionario
                            FOREIGN KEY (funcionario_id)
                            REFERENCES funcionario(id)
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
     * Devolve a instância única de FuncionarioDAO.
     * @return instância única de FuncionarioDAO
     */
    public static FuncionarioDAO getInstance() {
        if (FuncionarioDAO.singleton == null) {
            FuncionarioDAO.singleton = new FuncionarioDAO();
        }
        return FuncionarioDAO.singleton;
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
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM funcionario")) {
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
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM funcionario WHERE id=?")) {
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
        if (!(value instanceof Funcionario))
            throw new ClassCastException();

        Funcionario f = (Funcionario) value;
        // return this.containsKey(f.getCodFuncionario());
        return false;
    }

    /**
     * Obtém o valor associado à chave indicada.
     * @param key chave da entrada
     * @return valor associado à chave, ou {@code null} se a chave não existir
     */
    @Override
    public Funcionario get(Object key) {
        Funcionario f = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM funcionario WHERE id=?")) {

            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                // chave existe na tabela
                if (rs.next()) {
                    String codFuncionario = rs.getString("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String senha = rs.getString("senha");
                    String tipo = rs.getString("tipo");

                    if (tipo.equalsIgnoreCase("FUNCIONARIO")) {
                        f = new Funcionario(UUID.fromString(codFuncionario), nome, email, senha);

                    } else if (tipo.equalsIgnoreCase("GERENTE")) {
                        try (PreparedStatement gerente_pstm = conn.prepareStatement("SELECT restaurante_id FROM gerente WHERE funcionario_id=?")) {
                            gerente_pstm.setString(1, codFuncionario);
                            try (ResultSet gerente_rs = gerente_pstm.executeQuery()) {
                                if (gerente_rs.next()) {
                                    String restauranteIdStr = gerente_rs.getString("restaurante_id");

                                    Restaurante r = RestauranteDAO.getInstance().get(UUID.fromString(restauranteIdStr));

                                    if (r != null) {
                                        f = new Gerente(UUID.fromString(codFuncionario), nome, email, senha, r);
                                    }
                                }
                            }
                        }
                    } else if (tipo.equalsIgnoreCase("COO")) {
                        f = new COO(UUID.fromString(codFuncionario), nome, email, senha);
                    }
                }
            }
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return f;
    }

    /**
     * Associa o valor indicado à chave indicada.
     * Se a chave já existir, o valor anterior é substituído.
     * @param key chave da entrada
     * @param value valor a associar
     * @return valor anteriormente associado à chave, {@code null} se não existia associação prévia
     */
    @Override
    public Funcionario put(UUID key, Funcionario value) {
        if (key == null || value == null)
            throw new NullPointerException();

        // remover funcionario da base de dados
        Funcionario res = this.remove(key);

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO funcionario (id, nome, email, senha, tipo) VALUES(?, ?, ?, ?, ?)")) {
            pstm.setString(1, key.toString());
            pstm.setString(2, value.getNome());
            pstm.setString(3, value.getEmail());
            pstm.setString(4, value.getSenha());

            if (value instanceof Gerente) {
                pstm.setString(5, "GERENTE");
                pstm.executeUpdate();

                // adicionar Gerente à tabela de gerentes
                Gerente g = (Gerente) value;
                Restaurante r = g.getRestaurante();
                try (PreparedStatement gerente_pstm = conn.prepareStatement("INSERT INTO gerente (funcionario_id, restaurante_id) VALUES(?, ?)")) {
                    gerente_pstm.setString(1, key.toString());
                    if (r != null) {
                        gerente_pstm.setString(2, r.getCodRestaurante().toString());
                    } else {
                        throw new NullPointerException("Gerente sem restaurante associado!");
                    }
                    gerente_pstm.executeUpdate();
                }
            } else if (value instanceof COO) {
                pstm.setString(5, "COO");
                pstm.executeUpdate();

                // adicionar COO à tabela de coo
                try (PreparedStatement coo_pstm = conn.prepareStatement("INSERT INTO coo (funcionario_id) VALUES(?)")) {
                    coo_pstm.setString(1, key.toString());
                    coo_pstm.executeUpdate();
                }

            } else {
                pstm.setString(5, "FUNCIONARIO");
                pstm.executeUpdate();
            }

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
    public Funcionario remove(Object key) {
        Funcionario f = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM funcionario WHERE id=?")) {
            pstm.setString(1, key.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return f;
    }

    /**
     * Copia todas as entradas do mapa fornecido para este mapa.
     * @param map mapa com as entradas a adicionar
     */
    @Override
    public void putAll(Map<? extends UUID, ? extends Funcionario> map) {
        for (Funcionario f : map.values()) {
            // this.put(f.getCodFuncionario(), f);
        }
    }

    /**
     * Remove todas as entradas do mapa.
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE gerente");
            stm.executeUpdate("TRUNCATE coo");
            stm.executeUpdate("TRUNCATE funcionario");
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
             ResultSet rs = stm.executeQuery("SELECT id FROM funcionario")) {
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
    public Collection<Funcionario> values() {
        Collection<Funcionario> res = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM funcionario")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Funcionario f = this.get(UUID.fromString(id));
                res.add(f);
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
    public Set<Entry<UUID, Funcionario>> entrySet() {
        Set<Entry<UUID, Funcionario>> out = new HashSet<>();
        Entry<UUID, Funcionario> et;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM funcionario")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Funcionario f = this.get(UUID.fromString(id));

                et = new AbstractMap.SimpleEntry<>(UUID.fromString(id), f);
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
