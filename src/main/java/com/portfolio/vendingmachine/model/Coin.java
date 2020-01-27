package com.portfolio.vendingmachine.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "coins")
public class Coin implements Comparable<Coin> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Value is missing")
    @Range(min = 1, message = "Must be at least 1")
    private Integer value;

    @NotNull(message = "Stock is missing")
    @Range(min = 0, message = "Invalid value")
    private Integer stock;

    public Coin() {
    }

    public Coin(Integer value, Integer stock) {
        this.value = value;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getStock() {
        return stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public int compareTo(Coin o1) {
        return o1.getValue() - this.value;
    }
}
