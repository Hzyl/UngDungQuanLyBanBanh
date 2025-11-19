package com.mycompany.bakery.models;

public class Employee {
    private String id,name,username,password;
    private int role; // 0=Admin, 1=Nhân viên
    private double salary;
    public Employee(){}
    public Employee(String id,String name,String username,String password,int role,double salary){
        this.id=id;this.name=name;this.username=username;this.password=password;this.role=role;this.salary=salary;
    }
    public String getId(){return id;} public void setId(String id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getUsername(){return username;} public void setUsername(String username){this.username=username;}
    public String getPassword(){return password;} public void setPassword(String password){this.password=password;}
    public int getRole(){return role;} public void setRole(int role){this.role=role;}
    public String getRoleText(){return role==0?"Admin":"Nhân viên";}
    public boolean isAdmin(){return role==0;}
    public double getSalary(){return salary;} public void setSalary(double salary){this.salary=salary;}
}
