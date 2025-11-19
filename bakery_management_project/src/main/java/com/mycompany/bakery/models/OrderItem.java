package com.mycompany.bakery.models;

public class OrderItem {
    private int id;
    private String orderId;
    private String productId;
    private int quantity;
    private double unitPrice;
    public OrderItem(){}
    public OrderItem(String orderId,String productId,int quantity,double unitPrice){this.orderId=orderId;this.productId=productId;this.quantity=quantity;this.unitPrice=unitPrice;}
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public String getOrderId(){return orderId;} public void setOrderId(String orderId){this.orderId=orderId;}
    public String getProductId(){return productId;} public void setProductId(String productId){this.productId=productId;}
    public int getQuantity(){return quantity;} public void setQuantity(int quantity){this.quantity=quantity;}
    public double getUnitPrice(){return unitPrice;} public void setUnitPrice(double unitPrice){this.unitPrice=unitPrice;}
}
