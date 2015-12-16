package com.example.rank.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 09/07/2015
 * Time: 09:39
 */
public class Lancamento implements Serializable {

    private int id;
    private Date data;
    private Double valor;
    private Usuario usuario;
    private Round round;

    private double in;
    private double out;

    public Lancamento() {
    }

    public Lancamento(int id, Date data, Double valor, double in, double out, Usuario usuario, Round round) {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.usuario = usuario;
        this.round = round;
        this.in = in;
        this.out = out;
    }

    public Lancamento(Date data, Double valor, Usuario usuario, Round round, double in, double out) {
        this.data = data;
        this.valor = valor;
        this.usuario = usuario;
        this.round = round;
        this.in = in;
        this.out = out;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getOut() {
        return out;
    }

    public void setOut(double out) {
        this.out = out;
    }
}
