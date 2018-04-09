package com.redhat.coolstore.model;

public class Product {
    public String itemId;
    public String name;
    public String description;
    public double price;
    
    public Product() {}
    
    public Product(String itemId, String name, String description, double price) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
