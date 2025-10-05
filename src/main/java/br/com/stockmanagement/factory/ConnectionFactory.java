package br.com.stockmanagement.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {


    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
    private static final String USER = "rm566127";
    private static final String PASSWORD = "030307";

    // SQL DDL para criar a tabela de produtos
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE produto (" +
                    "   id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "   nome VARCHAR(255) NOT NULL," +
                    "   preco DECIMAL(10, 2) NOT NULL," +
                    "   quantidade_estoque INT NOT NULL" +
                    ");";


    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        criaTabela(connection);
        return connection;
    }


    private static void criaTabela(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // Executa o DDL
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela 'produto'. Verifique a sintaxe SQL.");
            e.printStackTrace();
        }
    }


    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    // Método para fechar PreparedStatement e ResultSet (simplificado, mas útil)
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o statement: " + e.getMessage());
            }
        }
    }
}
