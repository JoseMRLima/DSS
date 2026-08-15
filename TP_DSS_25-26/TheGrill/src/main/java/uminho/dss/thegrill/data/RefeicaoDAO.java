package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.ssrefeicoes.*;

import java.sql.*;
import java.util.*;

/**
 * Data Acess Object para a hierarquia de {@link Refeicao}.
 */
public class RefeicaoDAO implements Map<UUID, Refeicao> {

    // variáveis de classe

    /** instância única de RefeicaoDAO */
    private static RefeicaoDAO singleton = null;


    // construtores

    /**
     * Construtor por omissão do Data Acess Object da hierarquia de {@link Refeicao}.
     */
    private RefeicaoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS refeicao (
                        id CHAR(36) PRIMARY KEY,
                        tipo VARCHAR(30) NOT NULL\s
                            CHECK (tipo IN ('ITEM_A_ITEM', 'MENU'))
                    )
                    """;
            stm.executeUpdate(sql);
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS refeicao_item_item (
                        refeicao_id CHAR(36) PRIMARY KEY,
                        CONSTRAINT fk_refeicao_item_item_refeicao
                            FOREIGN KEY (refeicao_id)
                            REFERENCES refeicao(id)
                            ON DELETE CASCADE
                    )
                    """;
            stm.executeUpdate(sql);
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS refeicao_menu (
                        refeicao_id CHAR(36) PRIMARY KEY,
                        prato_id CHAR(36) NOT NULL,
                        bebida_id CHAR(36) NOT NULL,
                        acompanhamento_id CHAR(36) NOT NULL,
                        CONSTRAINT fk_refeicao_menu_refeicao
                            FOREIGN KEY (refeicao_id)
                            REFERENCES refeicao(id)
                            ON DELETE CASCADE,
                        CONSTRAINT fk_refeicao_menu_prato
                            FOREIGN KEY (prato_id)
                            REFERENCES prato(produto_id)
                            ON DELETE CASCADE,
                        CONSTRAINT fk_refeicao_menu_bebida
                            FOREIGN KEY (bebida_id)
                            REFERENCES bebida(produto_id)
                            ON DELETE CASCADE,
                        CONSTRAINT fk_refeicao_menu_acompanhamento
                            FOREIGN KEY (acompanhamento_id)
                            REFERENCES acompanhamento(produto_id)
                            ON DELETE CASCADE
                    )
                    """;
            stm.executeUpdate(sql);
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS refeicao_produto (
                        refeicao_ii_id CHAR(36) NOT NULL,
                        produto_id CHAR(36) NOT NULL,
                        PRIMARY KEY (refeicao_ii_id, produto_id),
                        CONSTRAINT fk_refeicao_produto_refeicao
                            FOREIGN KEY (refeicao_ii_id)
                            REFERENCES refeicao_item_item(refeicao_id)
                            ON DELETE CASCADE,
                        CONSTRAINT fk_refeicao_produto_produto
                            FOREIGN KEY (produto_id)
                            REFERENCES produto(id)
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
     * Devolve a instância única de RefeicaoDAO.
     * @return instância única de RefeicaoDAO
     */
    public static RefeicaoDAO getInstance() {
        if (RefeicaoDAO.singleton == null) {
            RefeicaoDAO.singleton = new RefeicaoDAO();
        }
        return RefeicaoDAO.singleton;
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
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM refeicao")) {
            if (rs.next()) {
                size = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // database Error
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
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM refeicao WHERE id=?")) {
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
        if (!(value instanceof Refeicao))
            throw new ClassCastException();

        Refeicao r = (Refeicao) value;
        // return this.containsKey(r.getCodRefeicao());
        return false;
    }

    /**
     * Obtém o valor associado à chave indicada.
     * @param key chave da entrada
     * @return valor associado à chave, ou {@code null} se a chave não existir
     */
    @Override
    public Refeicao get(Object key) {
        Refeicao r = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM refeicao WHERE id=?")) {

            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                // chave existe na tabela
                if (rs.next()) {
                    String codRefeicao = rs.getString("id");
                    String tipo = rs.getString("tipo");

                    if (tipo.equalsIgnoreCase("menu")) {
                        // REFEICAO MENU

                        try (PreparedStatement menuStm = conn.prepareStatement("SELECT * FROM refeicao_menu WHERE refeicao_id=?")) {
                            menuStm.setString(1, codRefeicao);
                            try (ResultSet rsMenu = menuStm.executeQuery()) {
                                if (rsMenu.next()) {
                                    UUID idPrato = UUID.fromString(rsMenu.getString("prato_id"));
                                    UUID idBebida = UUID.fromString(rsMenu.getString("bebida_id"));
                                    UUID idAcomp = UUID.fromString(rsMenu.getString("acompanhamento_id"));

                                    Prato p = (Prato) ProdutoDAO.getInstance().get(idPrato);
                                    Bebida b = (Bebida) ProdutoDAO.getInstance().get(idBebida);
                                    Acompanhamento a = (Acompanhamento) ProdutoDAO.getInstance().get(idAcomp);

                                    if (p != null && b != null && a != null) {
                                        r = new RefeicaoMenu(UUID.fromString(codRefeicao), p, b, a);
                                    }
                                }
                            }
                        }
                    } else {
                        // REFEICAO ITEM A ITEM

                        List<Produto> produtos = new ArrayList<>();
                        try (PreparedStatement ii_pstm = conn.prepareStatement("SELECT * FROM refeicao_produto WHERE refeicao_ii_id=?")) {
                            ii_pstm.setString(1, codRefeicao);
                            try (ResultSet ii_rs = ii_pstm.executeQuery()) {
                                while (ii_rs.next()) {
                                    String idProd = ii_rs.getString("produto_id");
                                    Produto p = ProdutoDAO.getInstance().get(UUID.fromString(idProd));
                                    if (p != null) {
                                        produtos.add(p);
                                    }
                                }
                            }
                        }
                        r = new RefeicaoItemItem(UUID.fromString(codRefeicao), produtos);
                    }
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
    public Refeicao put(UUID key, Refeicao value) {
        if (key == null || value == null)
            throw new NullPointerException();

        // remover refeicao da base de dados
        Refeicao res = this.remove(key);

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO refeicao (id, tipo) VALUES(?, ?)")) {
            pstm.setString(1, key.toString());

            String tipo = "";
            if (value instanceof RefeicaoMenu) {
                tipo = "MENU";
            } else {
                tipo = "ITEM_A_ITEM";
            }

            pstm.setString(2, tipo);
            pstm.executeUpdate();

            if (value instanceof RefeicaoMenu) {
                RefeicaoMenu m = (RefeicaoMenu) value;

                try (PreparedStatement menu_pstm = conn.prepareStatement("INSERT INTO refeicao_menu (refeicao_id, prato_id, bebida_id, acompanhamento_id) VALUES(?, ?, ?, ?)")) {
                    menu_pstm.setString(1, key.toString());
                    menu_pstm.setString(2, m.getPrato().getCodProduto().toString());
                    menu_pstm.setString(3, m.getBebida().getCodProduto().toString());
                    menu_pstm.setString(4, m.getAcompanhamento().getCodProduto().toString());
                    menu_pstm.executeUpdate();
                }
            } else {
                RefeicaoItemItem ii = (RefeicaoItemItem) value;

                try (PreparedStatement ii_pstm = conn.prepareStatement("INSERT INTO refeicao_item_item (refeicao_id) VALUES(?)")) {
                    ii_pstm.setString(1, key.toString());
                    ii_pstm.executeUpdate();

                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO refeicao_produto (refeicao_ii_id, produto_id) VALUES(?, ?)")) {
                        for (Produto p : ii.getProdutos()) {
                            ps.setString(1, key.toString());
                            ps.setString(2, p.getCodProduto().toString());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }
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
    public Refeicao remove(Object key) {
        Refeicao r = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM refeicao WHERE id=?")) {

            pstm.setString(1, key.toString());
            pstm.executeUpdate();
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
    public void putAll(Map<? extends UUID, ? extends Refeicao> map) {
        for (Refeicao r : map.values()) {
            // this.put(r.getCodRefeicao(), r);
        }
    }

    /**
     * Remove todas as entradas do mapa.
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            stm.executeUpdate("TRUNCATE refeicao");
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
             ResultSet rs = stm.executeQuery("SELECT id FROM refeicao")) {
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
    public Collection<Refeicao> values() {
        Collection<Refeicao> res = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM refeicao")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Refeicao r = this.get(UUID.fromString(id));
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
    public Set<Entry<UUID, Refeicao>> entrySet() {
        Set<Entry<UUID, Refeicao>> out = new HashSet<>();
        Entry<UUID, Refeicao> et;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM refeicao")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Refeicao r = this.get(UUID.fromString(id));

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
