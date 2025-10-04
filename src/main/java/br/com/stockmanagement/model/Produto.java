package br.com.stockmanagement.model;

import java.io.Serializable;
public class Produto {

    private Long id;
    private String nome;
    private double preco;
    private int quantidadeEstoque;

    public Produto() {
    }

    public Produto(String nome, double preco, int quantidadeEstoque) {
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Produto(Long id, String nome, double preco, int quantidadeEstoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }


    @Override
    public String toString() {
        return String.format(
                "| ID: %-4d | Nome: %-30s | Preço: R$ %-8.2f | Estoque: %-5d |",
                id, nome, preco, quantidadeEstoque
        );
    }
}

