package com.example.rank.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 06/07/2015
 * Time: 11:46
 */
public class Usuario implements Serializable {

    private long id;
    private String nome;

    public Usuario() {
    }

    public Usuario(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
