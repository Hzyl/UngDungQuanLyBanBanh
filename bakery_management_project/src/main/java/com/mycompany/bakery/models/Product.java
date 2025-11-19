package com.mycompany.bakery.models;

public class Product {
    private String id;
    private String name;
    private String description;
    private double costPrice;
    private double sellPrice;
    private int quantity;

    public Product() {}

    public Product(String id, String name, String description, double costPrice, double sellPrice, int quantity) {
        this.id = id; this.name = name; this.description = description;
        this.costPrice = costPrice; this.sellPrice = sellPrice; this.quantity = quantity;
    }

    public String getId(){return id;} public void setId(String id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public double getCostPrice(){return costPrice;} public void setCostPrice(double costPrice){this.costPrice=costPrice;}
    public double getSellPrice(){return sellPrice;} public void setSellPrice(double sellPrice){this.sellPrice=sellPrice;}
    public int getQuantity(){return quantity;} public void setQuantity(int quantity){this.quantity=quantity;}
}
