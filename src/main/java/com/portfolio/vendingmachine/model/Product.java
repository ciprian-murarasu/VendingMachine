package com.portfolio.vendingmachine.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Product name is missing")
    private String name;

    @NotBlank(message = "Unit of quantity is missing")
    private String unitQty;

    @NotNull(message = "Stock is missing")
    @Range(min = 0, message = "Invalid value")
    private Integer stock;

    @NotNull(message = "Price is missing")
    @Range(min = 1, message = "Must be at least 1")
    private Integer price;

    public Product() {
    }

    public Product(String name, String unitQty, Integer price, Integer stock) {
        this.name = name;
        this.unitQty = unitQty;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitQty() {
        return unitQty;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setUnitQty(String unitQty) {
        this.unitQty = unitQty.trim();
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
