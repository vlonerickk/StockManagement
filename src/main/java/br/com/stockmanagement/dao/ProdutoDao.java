package br.com.stockmanagement.dao;

import br.com.stockmanagement.model.Produto;
import java.util.List;
import java.sql.SQLException;
public interface ProdutoDao {


    Produto cadastrar(Produto produto) throws SQLException;


    Produto pesquisarPorId(Long id) throws SQLException;


    List<Produto> listar() throws SQLException;

    boolean atualizar(Produto produto) throws SQLException;

    boolean remover(Long id) throws SQLException;

    List<Produto> pesquisarPorNome(String nome) throws SQLException;
}
