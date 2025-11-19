package com.mycompany.bakery.dao;

import com.mycompany.bakery.models.Customer;
import com.mycompany.bakery.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY name";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer t = new Customer();
                t.setId(rs.getString("id"));
                t.setName(rs.getString("name"));
                t.setPhone(rs.getString("phone"));
                t.setEmail(rs.getString("email"));
                list.add(t);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public boolean insert(Customer cst) {
        String sql = "INSERT INTO customer (id,name,phone,email) VALUES (?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cst.getId());
            ps.setString(2, cst.getName());
            ps.setString(3, cst.getPhone());
            ps.setString(4, cst.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean update(Customer cst) {
        String sql = "UPDATE customer SET name=?,phone=?,email=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cst.getName());
            ps.setString(2, cst.getPhone());
            ps.setString(3, cst.getEmail());
            ps.setString(4, cst.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM customer WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
            return false; 
        }
    }

    public Customer findById(String id) {
        String sql = "SELECT * FROM customer WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer t = new Customer();
                    t.setId(rs.getString("id"));
                    t.setName(rs.getString("name"));
                    t.setPhone(rs.getString("phone"));
                    t.setEmail(rs.getString("email"));
                    return t;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public boolean hasOrders(String customerId) {
        String sql = "SELECT COUNT(*) FROM orders WHERE customer_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }
}
