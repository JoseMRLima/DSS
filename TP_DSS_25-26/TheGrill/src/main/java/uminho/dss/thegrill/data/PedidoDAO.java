package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.sspedidos.*;
import uminho.dss.thegrill.business.ssrefeicoes.Refeicao;

import java.sql.*;
import java.util.*;

/**
 * Data Acess Object para {@link Pedido}.
 */
public class PedidoDAO implements Map<UUID, Pedido> {

    // variáveis de classe

    /** instância única de PedidoDAO */
    private static PedidoDAO singleton = null;


    // construtores

    /**
     * Construtor por omissão do Data Acess Object de {@link Pedido}.
     */
    private PedidoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS pedido (
                        id CHAR(36) PRIMARY KEY,
                        estado VARCHAR(30) NOT NULL
                            CHECK (estado IN ('INICIADO', 'PAGO', 'EM_CONFECAO', 'PRONTO', 'EMPRATADO', 'EMBALADO', 'ENTREGUE', 'CANCELADO')),
                        tipo_levantamento VARCHAR(30)
                            CHECK (tipo_levantamento IN ('EMPRATAMENTO', 'EMBALAMENTO')),
                        nota VARCHAR(255),
                        tempo_espera INT,
                        balcao_entrega INT,
                        refeicao_id CHAR(36) NOT NULL,
                        restaurante_id CHAR(36),
                        CONSTRAINT fk_pedido_refeicao
                            FOREIGN KEY (refeicao_id)
                            REFERENCES refeicao(id)
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
     * Devolve a instância única de PedidoDAO
     * @return instância única de PedidoDAO
     */
    public static PedidoDAO getInstance() {
        if (PedidoDAO.singleton == null)
            PedidoDAO.singleton = new PedidoDAO();
        return PedidoDAO.singleton;
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
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM pedido")) {
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
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM pedido WHERE id=?")) {
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
        if (!(value instanceof Pedido))
            throw new ClassCastException();

        Pedido p = (Pedido) value;
        // return this.containsKey(p.getCodPedido());
        return false;
    }

    /**
     * Obtém o valor associado à chave indicada.
     * @param key chave da entrada
     * @return valor associado à chave, ou {@code null} se a chave não existir
     */
    @Override
    public Pedido get(Object key) {
        Pedido p = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM pedido WHERE id=?")) {

            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                // chave existe na tabela
                if (rs.next()) {
                    String codPedido = rs.getString("id");
                    String estadoStr = rs.getString("estado");
                    EstadoPedido estado = EstadoPedido.valueOf(estadoStr);
                    String tipoLevantamento = rs.getString("tipo_levantamento");
                    String nota = rs.getString("nota");
                    int tempoEspera = rs.getInt("tempo_espera");
                    int balcao = rs.getInt("balcao_entrega");
                    String refeicaoId = rs.getString("refeicao_id");
                    String restIdStr = rs.getString("restaurante_id");


                    Refeicao r = RefeicaoDAO.getInstance().get(UUID.fromString(refeicaoId));
                    UUID restId = (restIdStr != null) ? UUID.fromString(restIdStr) : null;

                    if (r != null) {
                        p = new Pedido(UUID.fromString(codPedido), estado, tempoEspera, tipoLevantamento, nota, r, balcao, restId);
                    }
                }
            }
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return p;
    }

    /**
     * Associa o valor indicado à chave indicada.
     * Se a chave já existir, o valor anterior é substituído.
     * @param key chave da entrada
     * @param value valor a associar
     * @return valor anteriormente associado à chave, {@code null} se não existia associação prévia
     */
    @Override
    public Pedido put(UUID key, Pedido value) {
        if (key == null || value == null)
            throw new NullPointerException();

        // remover pedido da base de dados
        Pedido res = this.remove(key);

        String sql = "INSERT INTO pedido (id, estado, tipo_levantamento, nota, tempo_espera, balcao_entrega, refeicao_id, restaurante_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, key.toString());
            pstm.setString(2, value.getEstado().name());
            pstm.setString(3, value.getTipoLevantamento());
            pstm.setString(4, value.getNota());
            pstm.setInt(5, value.getTempoEspera());
            pstm.setInt(6, value.getBalcaoEntrega());
            pstm.setString(7, value.getRefeicao().getCodRefeicao().toString());

            UUID rId = value.getRestauranteId();
            pstm.setString(8, (rId != null) ? rId.toString() : null);

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
    public Pedido remove(Object key) {
        Pedido p = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM pedido WHERE id=?")) {

            pstm.setString(1, key.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            // database error
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return p;
    }

    /**
     * Copia todas as entradas do mapa fornecido para este mapa.
     * @param map mapa com as entradas a adicionar
     */
    @Override
    public void putAll(Map<? extends UUID, ? extends Pedido> map) {
        for (Pedido p : map.values()) {
            // this.put(p.getCodPedido(), p);
        }
    }

    /**
     * Remove todas as entradas do mapa.
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            stm.executeUpdate("TRUNCATE pagamento");
            stm.executeUpdate("TRUNCATE pedido");
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
             ResultSet rs = stm.executeQuery("SELECT id FROM pedido")) {
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
    public Collection<Pedido> values() {
        Collection<Pedido> res = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM pedido")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Pedido p = this.get(UUID.fromString(id));
                res.add(p);
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
    public Set<Entry<UUID, Pedido>> entrySet() {
        Set<Entry<UUID, Pedido>> out = new HashSet<>();
        Entry<UUID, Pedido> et;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM pedido")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Pedido p = this.get(UUID.fromString(id));

                et = new AbstractMap.SimpleEntry<>(UUID.fromString(id), p);
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
