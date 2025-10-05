package br.com.stockmanagement.dao;

import br.com.stockmanagement.model.Produto;
import java.util.List;
import java.sql.SQLException;
public interface ProdutoDao {


    Produto cadastrar(Produto produto) throws SQLException;

    /**
     * Busca um produto pelo seu ID único.
     * @param id O ID do produto.
     * @return O Produto encontrado ou null se não existir.
     * @throws SQLException Em caso de erro no acesso ao banco.
     */
    Produto pesquisarPorId(Long id) throws SQLException;

    /**
     * Lista todos os produtos cadastrados no banco de dados.
     * @return Uma lista de todos os Produtos.
     * @throws SQLException Em caso de erro no acesso ao banco.
     */
    List<Produto> listar() throws SQLException;

    /**
     * Atualiza os dados de um produto existente.
     * @param produto O objeto Produto com os dados atualizados (o ID deve estar preenchido).
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     * @throws SQLException Em caso de erro no acesso ao banco.
     */
    boolean atualizar(Produto produto) throws SQLException;

    /**
     * Remove um produto do banco de dados pelo seu ID.
     * @param id O ID do produto a ser removido.
     * @return true se a remoção foi bem-sucedida, false caso contrário.
     * @throws SQLException Em caso de erro no acesso ao banco.
     */
    boolean remover(Long id) throws SQLException;

    /**
     * Pesquisa produtos pelo nome (ou parte do nome). Pesquisa obrigatória por atributo.
     * @param nome O termo de pesquisa (usa LIKE '%nome%').
     * @return Uma lista de Produtos que correspondem ao critério.
     * @throws SQLException Em caso de erro no acesso ao banco.
     */
    List<Produto> pesquisarPorNome(String nome) throws SQLException;
}
