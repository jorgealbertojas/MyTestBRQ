package com.example.jorge.mytestbrq.data.source.cloud.cars.model;

/**
 * Created by jorge on 19/03/2018.
 * Model for support json http://desafiobrq.herokuapp.com/v1/carro/
 */

public class Cars {
    private int id;
    private String nome;
    private String marca;
    private int preco;
    private String imagem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
