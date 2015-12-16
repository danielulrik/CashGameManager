package com.example.rank.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 09/07/2015
 * Time: 09:39
 */
public class Round implements Serializable {

    private long id;
    private Date iniDate;
    private Date endDate;
    private Double tornBuyIn;
    private List<Usuario> users;

    public Round() {
    }

    public Round(Date iniDate, Date endDate, Double tornBuyIn) {
        this.iniDate = iniDate;
        this.endDate = endDate;
        this.tornBuyIn = tornBuyIn;
    }

    public Round(long id, Date iniDate, Date endDate, Double tornBuyIn) {
        this.id = id;
        this.iniDate = iniDate;
        this.endDate = endDate;
        this.tornBuyIn = tornBuyIn;
    }

    public Round(long id, Date iniDate, Date endDate, Double tornBuyIn, List<Usuario> users) {
        this.id = id;
        this.iniDate = iniDate;
        this.endDate = endDate;
        this.tornBuyIn = tornBuyIn;
        this.users = users;
    }

    public Round(Date iniDate, Date endDate, Double tornBuyIn, List<Usuario> users) {
        this.iniDate = iniDate;
        this.endDate = endDate;
        this.tornBuyIn = tornBuyIn;
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getIniDate() {
        return iniDate;
    }

    public void setIniDate(Date iniDate) {
        this.iniDate = iniDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getTornBuyIn() {
        return tornBuyIn;
    }

    public void setTornBuyIn(Double tornBuyIn) {
        this.tornBuyIn = tornBuyIn;
    }

    public List<Usuario> getUsers() {
        return users;
    }

    public void setUsers(List<Usuario> users) {
        this.users = users;
    }

    public double getFirstPlaceValue() {
        // numero fixo de usuarios, 3 taxes 4 usuarios
        // tornBuyIn * nusu * 0.75
        return tornBuyIn * 4 * 0.75;
    }

    public double getSecondPlaceValue() {
        // numero fixo de usuarios
        return tornBuyIn * 4 * 0.25;
    }
}
