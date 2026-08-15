package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.ssrefeicoes.*;

import java.sql.*;
import java.util.*;

/**
 * Data Acess Object para a hierarquia de {@link Produto}.
 */
public class ProdutoDAO implements Map<UUID, Produto> {

    // variáveis de classe

    /** instância única de FuncionarioDAO */
    private static ProdutoDAO singleton = null;


    // construtores

    /**
     * Construtor por omissão do Data Acess Object da hierarquia de {@link Produto}.
     */
    private ProdutoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS produto (
                        id CHAR(36) PRIMARY KEY,
                        tipo VARCHAR(30) NOT NULL\s
                            CHECK (tipo IN ('BEBIDA', 'PRATO', 'ACOMPANHAMENTO')),
                        designacao VARCHAR(50) NOT NULL,
                        custo DECIMAL(6,2) NOT NULL,
                        tempo_confecao INT NOT NULL
                    )
                    """;
            stm.executeUpdate(sql);
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS prato (
                        produto_id CHAR(36) PRIMARY KEY,
                        CONSTRAINT fk_prato_produto
                            FOREIGN KEY (produto_id)
                            REFERENCES produto(id)
                            ON DELETE CASCADE
                    )
                    """;
            stm.executeUpdate(sql);
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS bebida (
                        produto_id CHAR(36) PRIMARY KEY,
                        porcao VARCHAR(30) NOT NULL\s
                            CHECK (porcao IN ('PEQUENO', 'MEDIO', 'GRANDE')),
                        CONSTRAINT fk_bebida_produto
                            FOREIGN KEY (produto_id)
                            REFERENCES produto(id)
                            ON DELETE CASCADE
                    )
                    """;
            stm.executeUpdate(sql);
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS acompanhamento (
                        produto_id CHAR(36) PRIMARY KEY,
                        porcao VARCHAR(30) NOT NULL
                            CHECK (porcao IN ('PEQUENO', 'MEDIO', 'GRANDE')),
                        CONSTRAINT fk_acompanhamento_produto
                            FOREIGN KEY (produto_id)
                            REFERENCES produto(id)
                            ON DELETE CASCADE
                    )
                    """;
            stm.executeUpdate(sql);
            sql =
                    """
                    CREATE TABLE IF NOT EXISTS produto_ingrediente (
                        produto_id CHAR(36) NOT NULL,
                        ingrediente_id CHAR(36) NOT NULL,
                        tipo VARCHAR(30) NOT NULL
                            CHECK (tipo IN ('OBRIGATORIO', 'ALTERNATIVO', 'SUBSTITUICAO')),
                        CONSTRAINT fk_produto_ingrediente_produto
                            FOREIGN KEY (produto_id)
                            REFERENCES produto(id)
                            ON DELETE CASCADE,
                        CONSTRAINT fk_produto_ingrediente_ingrediente
                            FOREIGN KEY (ingrediente_id)
                            REFERENCES ingrediente(id)
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
     * Devolve a instância única de ProdutoDAO.
     * @return instância única de ProdutoDAO
     */
    public static ProdutoDAO getInstance() {
        if (ProdutoDAO.singleton == null) {
            ProdutoDAO.singleton = new ProdutoDAO();
        }
        return ProdutoDAO.singleton;
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
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM produto")) {
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
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM produto WHERE id=?")) {
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
        if (!(value instanceof Produto))
            throw new ClassCastException();

        Produto p = (Produto) value;
        // return this.containsKey(p.getCodProduto());
        return false;
    }

    /**
     * Obtém o valor associado à chave indicada.
     * @param key chave da entrada
     * @return valor associado à chave, ou {@code null} se a chave não existir
     */
    @Override
    public Produto get(Object key) {
        Produto p = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM produto WHERE id=?")) {

            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                // chave existe na tabela
                if (rs.next()) {

                    String codProduto = rs.getString("id");
                    String tipo = rs.getString("tipo");
                    String designacao = rs.getString("designacao");
                    float custo = rs.getFloat("custo");
                    int tempoConfecao = rs.getInt("tempo_confecao");

                    List<UUID> obrigatorios = new ArrayList<>();
                    List<UUID> alternativos = new ArrayList<>();
                    List<UUID> substituiveis = new ArrayList<>();

                    try (PreparedStatement obrigatorio_pstm = conn.prepareStatement("SELECT ingrediente_id FROM produto_ingrediente WHERE produto_id=? AND tipo='OBRIGATORIO'")) {
                        obrigatorio_pstm.setString(1, codProduto);

                        try (ResultSet ob_rs = obrigatorio_pstm.executeQuery()) {
                            while (ob_rs.next()) {
                                String s = ob_rs.getString("ingrediente_id");
                                obrigatorios.add(UUID.fromString(s));
                            }
                        }
                    }

                    try (PreparedStatement alternativo_pstm = conn.prepareStatement("SELECT ingrediente_id FROM produto_ingrediente WHERE produto_id=? AND tipo='ALTERNATIVO'")) {
                        alternativo_pstm.setString(1, codProduto);

                        try (ResultSet al_rs = alternativo_pstm.executeQuery()) {
                            while (al_rs.next()) {
                                String s = al_rs.getString("ingrediente_id");
                                alternativos.add(UUID.fromString(s));
                            }
                        }
                    }

                    try (PreparedStatement substituicao_pstm = conn.prepareStatement("SELECT ingrediente_id FROM produto_ingrediente WHERE produto_id=? AND tipo='SUBSTITUICAO'")) {
                        substituicao_pstm.setString(1, codProduto);

                        try (ResultSet sb_rs = substituicao_pstm.executeQuery()) {
                            while (sb_rs.next()) {
                                String s = sb_rs.getString("ingrediente_id");
                                substituiveis.add(UUID.fromString(s));
                            }
                        }
                    }

                    if (tipo.equalsIgnoreCase("prato")) {
                        // PRATO PRINCIPAL

                        p = new Prato(UUID.fromString(codProduto), designacao, custo, tempoConfecao, obrigatorios, alternativos, substituiveis);

                    } else if (tipo.equalsIgnoreCase("bebida")) {
                        // BEBIDA
                        String porcaoStr = "";

                        try (PreparedStatement bebida_pstm = conn.prepareStatement("SELECT porcao FROM bebida WHERE produto_id=?")) {
                            bebida_pstm.setString(1, codProduto);
                            try (ResultSet b_rs = bebida_pstm.executeQuery()) {
                                if (b_rs.next()) {
                                    porcaoStr = b_rs.getString("porcao");
                                }
                            }
                        }
                        // CORRIGIDO: Converter String para Enum Porcao
                        Porcao porcao = porcaoStr.isEmpty() ? Porcao.MEDIO : Porcao.valueOf(porcaoStr);
                        p = new Bebida(UUID.fromString(codProduto), designacao, custo, tempoConfecao, obrigatorios, alternativos, substituiveis, porcao);
                    } else {
                        // ACOMPANHAMENTO

                        String porcaoStr = "";
                        try (PreparedStatement acomp_pstm = conn.prepareStatement("SELECT porcao FROM acompanhamento WHERE produto_id=?")) {
                            acomp_pstm.setString(1, codProduto);
                            try (ResultSet a_rs = acomp_pstm.executeQuery()) {
                                if (a_rs.next()) {
                                    porcaoStr = a_rs.getString("porcao");
                                }
                            }
                        }
                        Porcao porcao = porcaoStr.isEmpty() ? Porcao.MEDIO : Porcao.valueOf(porcaoStr);
                        p = new Acompanhamento(UUID.fromString(codProduto), designacao, custo, tempoConfecao, obrigatorios, alternativos, substituiveis, porcao);
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
    public Produto put(UUID key, Produto value) {
        if (key == null || value == null)
            throw new NullPointerException();

        Produto res = this.remove(key);

        try (Connection conn = DriverManager.getConnection(
                DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {

            // ---------- PRODUTO ----------
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO produto (id, tipo, designacao, custo, tempo_confecao) VALUES (?, ?, ?, ?, ?)")) {

                pstm.setString(1, key.toString());
                pstm.setString(3, value.getDesignacao());
                pstm.setFloat(4, value.getCusto());
                pstm.setInt(5, value.getTempoConfecao());

                String tipo;
                if (value instanceof Prato) tipo = "PRATO";
                else if (value instanceof Bebida) tipo = "BEBIDA";
                else tipo = "ACOMPANHAMENTO";

                pstm.setString(2, tipo);
                pstm.executeUpdate();
            }

            // ---------- TIPO ----------
            if (value instanceof Prato) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO prato (produto_id) VALUES (?)")) {
                    ps.setString(1, key.toString());
                    ps.executeUpdate();
                }
            } else if (value instanceof Bebida b) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO bebida (produto_id, porcao) VALUES (?, ?)")) {
                    ps.setString(1, key.toString());
                    ps.setString(2, b.getPorcao().toString());
                    ps.executeUpdate();
                }
            } else if (value instanceof Acompanhamento a) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO acompanhamento (produto_id, porcao) VALUES (?, ?)")) {
                    ps.setString(1, key.toString());
                    ps.setString(2, a.getPorcao().toString());
                    ps.executeUpdate();
                }
            }

            // ---------- OBRIGATÓRIOS ----------
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO produto_ingrediente (produto_id, ingrediente_id, tipo) VALUES (?, ?, ?)")) {

                for (UUID ing : value.getCodsObrigatorios()) {
                    if (!existeAssociacao(conn, key, ing)) {
                        ps.setString(1, key.toString());
                        ps.setString(2, ing.toString());
                        ps.setString(3, "OBRIGATORIO");
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }

            // ---------- ALTERNATIVOS ----------
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO produto_ingrediente (produto_id, ingrediente_id, tipo) VALUES (?, ?, ?)")) {

                for (UUID ing : value.getCodsAlternativos()) {
                    if (!existeAssociacao(conn, key, ing)) {
                        ps.setString(1, key.toString());
                        ps.setString(2, ing.toString());
                        ps.setString(3, "ALTERNATIVO");
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }

            // ---------- SUBSTITUÍVEIS ----------
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO produto_ingrediente (produto_id, ingrediente_id, tipo) VALUES (?, ?, ?)")) {

                for (UUID ing : value.getCodsSubstituiveis()) {
                    if (!existeAssociacao(conn, key, ing)) {
                        ps.setString(1, key.toString());
                        ps.setString(2, ing.toString());
                        ps.setString(3, "SUBSTITUICAO");
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return res;
    }


    /**
     * Remove a entrada associada à chave indicada.
     * @param key chave da entrada a remover
     * @return valor anteriormente associado à chave, {@code null} se a chave não existir
     */
    @Override
    public Produto remove(Object key) {
        Produto p = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM produto WHERE id=?")) {

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
    public void putAll(Map<? extends UUID, ? extends Produto> map) {
        for (Produto p : map.values()) {
            // this.put(p.getCodProduto(), p);
        }
    }

    /**
     * Remove todas as entradas do mapa.
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            stm.executeUpdate("TRUNCATE produto");
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
             ResultSet rs = stm.executeQuery("SELECT id FROM produto")) {
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
    public Collection<Produto> values() {
        Collection<Produto> res = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM produto")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Produto p = this.get(UUID.fromString(id));
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
    public Set<Entry<UUID, Produto>> entrySet() {
        Set<Entry<UUID, Produto>> out = new HashSet<>();
        Entry<UUID, Produto> et;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM produto")) {
            while (rs.next()) {
                String id = rs.getString("id");
                Produto p = this.get(UUID.fromString(id));

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

    private boolean existeAssociacao(Connection conn, UUID produtoId, UUID ingredienteId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM produto_ingrediente WHERE produto_id=? AND ingrediente_id=?")) {
            ps.setString(1, produtoId.toString());
            ps.setString(2, ingredienteId.toString());
            return ps.executeQuery().next();
        }
    }



}
