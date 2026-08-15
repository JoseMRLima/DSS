package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.sspedidos.EstadoPedido;
import uminho.dss.thegrill.business.sspedidos.TarefaConfecao;
import uminho.dss.thegrill.business.ssrefeicoes.*;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object para {@link TarefaConfecao}.
 */
public class TarefaDAO implements Map<UUID, TarefaConfecao> {

    // variáveis de classe

    /** instância única de TarefaDAO */
    private static TarefaDAO singleton = null;


    // construtores

    /**
     * Construtor por omissão do Data Access Object de {@link TarefaConfecao}.
     */
    private TarefaDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS tarefa_confecao (
                        id CHAR(36) PRIMARY KEY,
                        pedido_id CHAR(36) NOT NULL,
                        produto_id CHAR(36) NOT NULL,
                        posto VARCHAR(30) NOT NULL,
                        estado VARCHAR(30) NOT NULL
                            CHECK (estado IN ('INICIADO', 'PAGO', 'EM_CONFECAO', 'PRONTO', 'EMPRATADO', 'EMBALADO', 'ENTREGUE', 'CANCELADO')),
                        nota TEXT,
                        CONSTRAINT fk_tarefa_pedido
                            FOREIGN KEY (pedido_id)
                            REFERENCES pedido(id)
                            ON DELETE CASCADE
                    )
                    """;
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    // métodos de classe

    /**
     * Devolve a instância única de TarefaDAO.
     * @return instância única de TarefaDAO
     */
    public static TarefaDAO getInstance() {
        if (TarefaDAO.singleton == null) {
            TarefaDAO.singleton = new TarefaDAO();
        }
        return TarefaDAO.singleton;
    }


    // métodos de instância

    @Override
    public int size() {
        int size = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM tarefa_confecao")) {
            if (rs.next()) size = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        if (!(key instanceof UUID)) throw new ClassCastException();

        boolean result;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM tarefa_confecao WHERE id=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                result = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) throw new NullPointerException();
        if (!(value instanceof TarefaConfecao)) throw new ClassCastException();
        return this.containsKey(((TarefaConfecao) value).getIdTarefa());
    }

    @Override
    public TarefaConfecao get(Object key) {
        TarefaConfecao t = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM tarefa_confecao WHERE id=?")) {

            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    UUID idTarefa = UUID.fromString(rs.getString("id"));
                    UUID idPedido = UUID.fromString(rs.getString("pedido_id"));
                    UUID idProduto = UUID.fromString(rs.getString("produto_id"));
                    String posto = rs.getString("posto");
                    EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
                    String nota = rs.getString("nota");

                    // Recuperar o Produto real usando o ProdutoDAO
                    Produto p = ProdutoDAO.getInstance().get(idProduto);

                    if (p != null) {
                        t = new TarefaConfecao(idTarefa, idPedido, p, posto, estado, nota);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public TarefaConfecao put(UUID key, TarefaConfecao value) {
        if (key == null || value == null) throw new NullPointerException();

        TarefaConfecao res = this.remove(key);

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO tarefa_confecao (id, pedido_id, produto_id, posto, estado, nota) VALUES(?, ?, ?, ?, ?, ?)")) {
            pstm.setString(1, key.toString());
            pstm.setString(2, value.getIdPedido().toString());
            pstm.setString(3, value.getProduto().getCodProduto().toString());
            pstm.setString(4, value.getPosto());
            pstm.setString(5, value.getEstado().name());
            pstm.setString(6, value.getNota());

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public TarefaConfecao remove(Object key) {
        TarefaConfecao t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM tarefa_confecao WHERE id=?")) {
            pstm.setString(1, key.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends UUID, ? extends TarefaConfecao> map) {
        for (TarefaConfecao t : map.values()) {
            this.put(t.getIdTarefa(), t);
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE tarefa_confecao");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<UUID> keySet() {
        Set<UUID> out = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM tarefa_confecao")) {
            while (rs.next()) {
                out.add(UUID.fromString(rs.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return out;
    }

    @Override
    public Collection<TarefaConfecao> values() {
        Collection<TarefaConfecao> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM tarefa_confecao")) {
            while (rs.next()) {
                res.add(this.get(UUID.fromString(rs.getString("id"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Set<Entry<UUID, TarefaConfecao>> entrySet() {
        Set<Entry<UUID, TarefaConfecao>> out = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM tarefa_confecao")) {
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                out.add(new AbstractMap.SimpleEntry<>(id, this.get(id)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return out;
    }

    public List<TarefaConfecao> getTarefasPorPosto(String posto) {
        List<TarefaConfecao> res = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM tarefa_confecao WHERE posto=?")) {
            pstm.setString(1, posto);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    res.add(this.get(UUID.fromString(rs.getString("id"))));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public List<TarefaConfecao> getTarefasPorPedido(UUID idPedido) {
        List<TarefaConfecao> res = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM tarefa_confecao WHERE pedido_id=?")) {
            pstm.setString(1, idPedido.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    res.add(this.get(UUID.fromString(rs.getString("id"))));
                }
            }
        } catch (SQLException e) { /* erro */ }
        return res;
    }
}