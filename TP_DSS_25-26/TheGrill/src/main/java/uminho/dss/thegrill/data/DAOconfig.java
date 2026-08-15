package uminho.dss.thegrill.data;

/**
 * Classe que representa a configuração da base de dados TheGrill.
 */
public class DAOconfig {

    static final String USERNAME = "me";

    static final String PASSWORD = "mypass";

    private static final String DATABASE = "thegrill";

    // private static final String DRIVER = "jdbc:mariadb";
    private static final String DRIVER = "jdbc:mysql";

    private static final String PORT = "3306";

    static final String URL = DRIVER + "://localhost:" + PORT + "/" + DATABASE;

}
