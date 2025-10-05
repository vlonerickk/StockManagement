package br.com.stockmanagement.dao;

import br.com.stockmanagement.model.Produto;
import br.com.stockmanagement.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDaoImpl implements ProdutoDao {

    //Comandos SQL
    private static final String SQL_INSERT = "INSERT INTO produto (nome, preco, quantidade_estoque) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT id, nome, preco, quantidade_estoque FROM produto ORDER BY id";
    private static final String SQL_SELECT_BY_ID = "SELECT id, nome, preco, quantidade_estoque FROM produto WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE produto SET nome = ?, preco = ?, quantidade_estoque = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM produto WHERE id = ?";
    private static final String SQL_SELECT_BY_NAME = "SELECT id, nome, preco, quantidade_estoque FROM produto WHERE nome LIKE ?";


    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        return produto;
    }

    @Override
    public Produto cadastrar(Produto produto) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            //Use RETURN_GENERATED_KEYS para obter o ID gerado pelo banco
            pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidadeEstoque());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao cadastrar produto, nenhuma linha afetada.");
            }

            //Obtém o ID gerado e seta no objeto
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                produto.setId(rs.getLong(1));
            }
            return produto;

        } finally {
            //Fecha recursos
            ConnectionFactory.closeStatement(pstmt);
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public Produto pesquisarPorId(Long id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProduto(rs);
            }
            return null;
        } finally {
            ConnectionFactory.closeStatement(pstmt);
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Produto> listar() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(SQL_SELECT_ALL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }
            return produtos;
        } finally {
            ConnectionFactory.closeStatement(pstmt);
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public boolean atualizar(Produto produto) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(SQL_UPDATE);
            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidadeEstoque());
            pstmt.setLong(4, produto.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            ConnectionFactory.closeStatement(pstmt);
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public boolean remover(Long id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(SQL_DELETE);
            pstmt.setLong(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            ConnectionFactory.closeStatement(pstmt);
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Produto> pesquisarPorNome(String nome) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(SQL_SELECT_BY_NAME);
            // Uso de LIKE com coringas para pesquisa parcial
            pstmt.setString(1, "%" + nome + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }
            return produtos;
        } finally {
            ConnectionFactory.closeStatement(pstmt);
            ConnectionFactory.closeConnection(conn);
        }
    }
}
