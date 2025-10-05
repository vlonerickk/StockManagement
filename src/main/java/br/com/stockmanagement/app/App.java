package br.com.stockmanagement.app;

import br.com.stockmanagement.model.Produto;
import br.com.stockmanagement.dao.ProdutoDao;
import br.com.stockmanagement.dao.ProdutoDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class App {

    private final ProdutoDao produtoDao = new ProdutoDaoImpl();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        App app = new App();
        app.iniciar();
    }


    public void iniciar() {
        int opcao = -1;
        do {
            try {
                exibirMenu();
                opcao = lerInteiro("Escolha uma opção: ");

                switch (opcao) {
                    case 1:
                        cadastrarProduto();
                        break;
                    case 2:
                        listarProdutos();
                        break;
                    case 3:
                        pesquisarPorId();
                        break;
                    case 4:
                        atualizarProduto();
                        break;
                    case 5:
                        removerProduto();
                        break;
                    case 6:
                        pesquisarPorNome();
                        break;
                    case 0:
                        System.out.println("\n----------------------------------------------------");
                        System.out.println("Obrigado por usar o Sistema de Gerenciamento de Produtos!");
                        System.out.println("----------------------------------------------------");
                        break;
                    default:
                        System.out.println("\n[ERRO] Opção inválida. Tente novamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\n[ERRO] Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                opcao = -1;
            } catch (SQLException e) {
                System.err.println("\n[ERRO SQL/DB] Ocorreu um erro no banco de dados: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\n[ERRO GERAL] Ocorreu um erro inesperado: " + e.getMessage());
            }
            if (opcao != 0) {
                pressioneEnterParaContinuar();
            }
        } while (opcao != 0);
        scanner.close();
    }


    private void exibirMenu() {
        System.out.println("\n====================================================");
        System.out.println("       SISTEMA DE GERENCIAMENTO DE PRODUTOS");
        System.out.println("====================================================");
        System.out.println(" 1 - Cadastrar Novo Produto");
        System.out.println(" 2 - Listar Todos os Produtos");
        System.out.println(" 3 - Pesquisar Produto por ID");
        System.out.println(" 4 - Atualizar Produto Existente");
        System.out.println(" 5 - Remover Produto por ID");
        System.out.println(" 6 - Pesquisar Produto por Nome (Pesquisa por Atributo)");
        System.out.println(" 0 - Sair");
        System.out.println("====================================================");
    }

    //Métodos de Leitura e Validação

    private String lerString(String prompt) {
        System.out.print(prompt);
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return scanner.next().trim();
    }

    private Long lerLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                long valor = scanner.nextLong();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("[ERRO] Por favor, digite um ID válido (número inteiro).");
                scanner.nextLine();
            }
        }
    }

    private int lerInteiro(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("[ERRO] Por favor, digite um número inteiro.");
                scanner.nextLine();
            }
        }
    }

    private double lerDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("[ERRO] Por favor, digite um valor numérico válido (ex: 1500.50).");
                scanner.nextLine();
            }
        }
    }

    private void pressioneEnterParaContinuar() {
        try {
            System.out.print("\nPressione ENTER para continuar...");
            System.in.read();
        } catch (IOException e) {
        }
    }



    private void cadastrarProduto() throws SQLException {
        System.out.println("\n--- CADASTRO DE NOVO PRODUTO ---");
        String nome = lerString("Nome: ");
        double preco = lerDouble("Preço: R$ ");
        int estoque = lerInteiro("Quantidade em Estoque: ");

        if (nome.isEmpty() || preco <= 0 || estoque < 0) {
            System.out.println("[ERRO] Dados inválidos. Nome não pode ser vazio, Preço deve ser positivo e Estoque não negativo.");
            return;
        }

        Produto novoProduto = new Produto(nome, preco, estoque);
        Produto produtoCadastrado = produtoDao.cadastrar(novoProduto);

        System.out.println("\n[SUCESSO] Produto cadastrado com ID: " + produtoCadastrado.getId());
        System.out.println(produtoCadastrado);
    }

    private void listarProdutos() throws SQLException {
        System.out.println("\n--- LISTAGEM DE TODOS OS PRODUTOS ---");
        List<Produto> produtos = produtoDao.listar();

        if (produtos.isEmpty()) {
            System.out.println("[INFO] Não há produtos cadastrados.");
            return;
        }

        exibirListaFormatada(produtos);
    }

    private void pesquisarPorId() throws SQLException {
        System.out.println("\n--- PESQUISAR PRODUTO POR ID ---");
        Long id = lerLong("Digite o ID do produto: ");

        Produto produto = produtoDao.pesquisarPorId(id);

        if (produto != null) {
            System.out.println("\n[SUCESSO] Produto encontrado:");
            System.out.println(produto);
        } else {
            System.out.println("\n[INFO] Produto com ID " + id + " não encontrado.");
        }
    }

    private void atualizarProduto() throws SQLException {
        System.out.println("\n--- ATUALIZAR PRODUTO ---");
        Long id = lerLong("Digite o ID do produto a ser atualizado: ");

        Produto produtoExistente = produtoDao.pesquisarPorId(id);

        if (produtoExistente == null) {
            System.out.println("\n[INFO] Produto com ID " + id + " não encontrado para atualização.");
            return;
        }

        System.out.println("\nProduto atual: " + produtoExistente);

        System.out.println("\nInforme os novos dados (deixe vazio para manter o original):");

        String novoNome = lerString("Novo Nome (" + produtoExistente.getNome() + "): ");
        if (!novoNome.isEmpty()) {
            produtoExistente.setNome(novoNome);
        }

        double novoPreco = lerDouble("Novo Preço (R$ " + produtoExistente.getPreco() + "): ");
        if (novoPreco > 0) {
            produtoExistente.setPreco(novoPreco);
        }

        int novoEstoque = lerInteiro("Nova Qtde. Estoque (" + produtoExistente.getQuantidadeEstoque() + "): ");
        if (novoEstoque >= 0) {
            produtoExistente.setQuantidadeEstoque(novoEstoque);
        }

        boolean sucesso = produtoDao.atualizar(produtoExistente);

        if (sucesso) {
            System.out.println("\n[SUCESSO] Produto ID " + id + " atualizado.");
            System.out.println("Novo registro: " + produtoExistente);
        } else {
            System.out.println("\n[ERRO] Falha ao atualizar o produto. Verifique o ID e tente novamente.");
        }
    }

    private void removerProduto() throws SQLException {
        System.out.println("\n--- REMOVER PRODUTO ---");
        Long id = lerLong("Digite o ID do produto a ser removido: ");

        Produto produto = produtoDao.pesquisarPorId(id);

        if (produto == null) {
            System.out.println("\n[INFO] Produto com ID " + id + " não encontrado para remoção.");
            return;
        }

        // Confirmação básica (sem usar confirm(), conforme instrução)
        System.out.println("\nTem certeza que deseja remover o produto: " + produto.getNome() + "? (S/N)");
        String confirmacao = lerString("Confirmação: ").toLowerCase();

        if (!confirmacao.equals("s")) {
            System.out.println("\n[INFO] Remoção cancelada.");
            return;
        }

        boolean sucesso = produtoDao.remover(id);

        if (sucesso) {
            System.out.println("\n[SUCESSO] Produto ID " + id + " removido.");
        } else {
            System.out.println("\n[ERRO] Falha ao remover o produto. O produto pode não existir.");
        }
    }

    private void pesquisarPorNome() throws SQLException {
        System.out.println("\n--- PESQUISAR PRODUTO POR NOME ---");
        String termo = lerString("Digite parte ou todo o nome do produto: ");

        if (termo.isEmpty()) {
            System.out.println("[ERRO] O termo de pesquisa não pode ser vazio.");
            return;
        }

        List<Produto> produtos = produtoDao.pesquisarPorNome(termo);

        if (produtos.isEmpty()) {
            System.out.println("\n[INFO] Nenhum produto encontrado com o nome '" + termo + "'.");
        } else {
            System.out.println("\n[SUCESSO] Produtos encontrados (Total: " + produtos.size() + "):");
            exibirListaFormatada(produtos);
        }
    }

    private void exibirListaFormatada(List<Produto> produtos) {
        System.out.println("+------+--------------------------------+------------+---------+");
        System.out.println("| ID   | Nome                           | Preço      | Estoque |");
        System.out.println("+------+--------------------------------+------------+---------+");
        for (Produto p : produtos) {
            System.out.println(p);
        }
        System.out.println("+------+--------------------------------+------------+---------+");
    }
}
