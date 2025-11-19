package com.mycompany.bakery.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private String employeeId;
    private Date orderDate;
    private String status;
    private List<OrderItem> items = new ArrayList<>();

    public Order(){}
    public Order(String id,String customerId,String employeeId){this.id=id;this.customerId=customerId;this.employeeId=employeeId;this.orderDate=new Date();this.status="Pending";}
    public String getId(){return id;} public void setId(String id){this.id=id;}
    public String getCustomerId(){return customerId;} public void setCustomerId(String customerId){this.customerId=customerId;}
    public String getEmployeeId(){return employeeId;} public void setEmployeeId(String employeeId){this.employeeId=employeeId;}
    public Date getOrderDate(){return orderDate;} public void setOrderDate(Date orderDate){this.orderDate=orderDate;}
    public String getStatus(){return status;} public void setStatus(String status){this.status=status;}
    public List<OrderItem> getItems(){return items;} public void setItems(List<OrderItem> items){this.items=items;}
}
