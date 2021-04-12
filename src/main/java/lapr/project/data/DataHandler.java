package lapr.project.data;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import lapr.project.utils.Configuration;

/**
 * Exemplo de classe cujas instâncias manipulam dados de BD Oracle.
 */
public class DataHandler {
    
    /**
     * O URL da BD.
     */
    private String jdbcUrl;

    /**
     * O nome de utilizador da BD.
     */
    private String username;

    /**
     * A password de utilizador da BD.
     */
    private String password;

    /**
     * A ligação à BD.
     */
    private static Connection connection;

    /**
     * A invocação de "stored procedures".
     */
    private CallableStatement callStmt;

    /**
     * Conjunto de resultados retornados por "stored procedures".
     */
    private ResultSet rSet;

    /**
     * Use connection properties set on file application.properties
     */
    public DataHandler() {
        this.jdbcUrl = Configuration.getDatabaseUrl();
        this.username = Configuration.getDatabaseUsername();
        this.password = Configuration.getDatabasePassword();
    }

    /**
     * Allows running entire scripts
     *
     * @param fileName
     * @throws IOException
     * @throws SQLException
     */
    public void scriptRunner(String fileName) throws IOException, SQLException {

        openConnection();

        ScriptRunner runner = new ScriptRunner(getConnection(), false, false);

        runner.runScript(new BufferedReader(new FileReader(fileName)));

        closeAll();

    }

    /**
     * Estabelece a ligação à BD.
     */
    protected void openConnection() {
        try {
            connection = DriverManager.getConnection(
                    jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fecha os objetos "ResultSet", "CallableStatement" e "Connection", e
     * retorna uma mensagem de erro se alguma dessas operações não for bem
     * sucedida. Caso contrário retorna uma "string" vazia.
     */
    protected String closeAll() {

        StringBuilder message = new StringBuilder();

        if (rSet != null) {
            try {
                rSet.close();
            } catch (SQLException ex) {
                message.append(ex.getMessage());
                message.append("\n");
            }
            rSet = null;
        }

        if (callStmt != null) {
            try {
                callStmt.close();
            } catch (SQLException ex) {
                message.append(ex.getMessage());
                message.append("\n");
            }
            callStmt = null;
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                message.append(ex.getMessage());
                message.append("\n");
            }
            connection = null;
        }
        return message.toString();
    }

    protected Connection getConnection() {
        if (connection == null)
            openConnection();
        return connection;
    }


}
