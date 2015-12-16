package com.example.rank.model;

import com.example.rank.screens.FragmentRank;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 28/07/2015
 * Time: 09:39
 */
public class UserValue implements Comparable<UserValue> {
    private String name;
    private Double value;

    public UserValue(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValor() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public int compareTo(UserValue another) {
        return new Double(value).compareTo(another.value) > 0 ? -1 : 1;
    }

    @Override
    public String toString() {
        return "name: " + name + ", value: " + value;
    }
}
