package uminho.dss.thegrill.data;

import uminho.dss.thegrill.business.ssrefeicoes.Alergenico;
import uminho.dss.thegrill.business.ssrefeicoes.Ingrediente;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object para Ingrediente
 */
public class IngredienteDAO implements Map<UUID, Ingrediente> {

    private static IngredienteDAO singleton = null;

    private IngredienteDAO() {
        try (Connection conn = DriverManager.getConnection(
                DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // ---------- TABELA INGREDIENTE ----------
            String sql = """
                CREATE TABLE IF NOT EXISTS ingrediente (
                    id CHAR(36) PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    alergenico BOOLEAN NOT NULL,
                    stock INT NOT NULL,
                    tempo_solicitacao INT NOT NULL
                )
            """;
            stm.executeUpdate(sql);

            // ---------- TABELA ALERGÉNICO ----------
            sql = """
                CREATE TABLE IF NOT EXISTS alergenico (
                    ingrediente_id CHAR(36) PRIMARY KEY,
                    tipo_alergia VARCHAR(50) NOT NULL,
                    nivel_risco VARCHAR(50) NOT NULL,
                    CONSTRAINT fk_alergenico_ingrediente
                        FOREIGN KEY (ingrediente_id)
                        REFERENCES ingrediente(id)
                        ON DELETE CASCADE
                )
            """;
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- SINGLETON ----------
    public static IngredienteDAO getInstance() {
        if (singleton == null)
            singleton = new IngredienteDAO();
        return singleton;
    }

    // ---------- MAP METHODS ----------

    @Override
    public int size() {
        try (Connection conn = DriverManager.getConnection(
                DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM ingrediente")) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        try (Connection conn = DriverManager.getConnection(
                DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id FROM ingrediente WHERE id=?")) {
            ps.setString(1, key.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ingrediente get(Object key) {
        try (Connection conn = DriverManager.getConnection(
                DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM ingrediente WHERE id=?")) {

            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            UUID id = UUID.fromString(rs.getString("id"));
            String nome = rs.getString("nome");
            boolean alerg = rs.getBoolean("alergenico");
            int stock = rs.getInt("stock");
            int tempo = rs.getInt("tempo_solicitacao");

            if (alerg) {
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "SELECT * FROM alergenico WHERE ingrediente_id=?")) {
                    ps2.setString(1, id.toString());
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        return new Alergenico(
                                id, nome, stock, tempo,
                                rs2.getString("tipo_alergia"),
                                rs2.getString("nivel_risco"));
                    }
                }
            }

            return new Ingrediente(id, nome, false, stock, tempo);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ingrediente put(UUID key, Ingrediente value) {
        remove(key);
        try (Connection conn = DriverManager.getConnection(
                DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO ingrediente VALUES (?,?,?,?,?)")) {

            ps.setString(1, key.toString());
            ps.setString(2, value.getNome());
            ps.setBoolean(3, value instanceof Alergenico);
            ps.setInt(4, value.getStock());
            ps.setInt(5, value.getTempoSolicitacao());
            ps.executeUpdate();

            return value;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ingrediente remove(Object key) {
        Ingrediente i = get(key);
        try (Connection conn = DriverManager.getConnection(
                DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM ingrediente WHERE id=?")) {
            ps.setString(1, key.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    // ---------- NÃO USADOS ----------
    @Override public void clear() {}
    @Override public boolean containsValue(Object value) { return false; }
    @Override public void putAll(Map<? extends UUID, ? extends Ingrediente> m) {}
    @Override public Set<UUID> keySet() { return Set.of(); }
    @Override public Collection<Ingrediente> values() { return List.of(); }
    @Override public Set<Entry<UUID, Ingrediente>> entrySet() { return Set.of(); }
}
