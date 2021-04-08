package com.cdsm.pizzaman2;

import java.io.Serializable;

public class Pizza implements Serializable {

    private String idPizza;
    private String namePizza;
    private double price;

    /**
     * empty constructor
     */
    public Pizza() {
    }

    /**
     * constructor
      * @param idPizza
     * @param namePizza
     * @param price
     */
    public Pizza(String idPizza, String namePizza, double price) {
        this.idPizza = idPizza;
        this.namePizza = namePizza;
        this.price = price;
    }

    /**
     * Getters and Setters
     *
     */
    public String getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(String idPizza) {
        this.idPizza = idPizza;
    }

    public String getNamePizza() {
        return namePizza;
    }

    public void setNamePizza(String namePizza) {
        this.namePizza = namePizza;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

