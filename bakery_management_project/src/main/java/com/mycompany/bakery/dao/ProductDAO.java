package com.mycompany.bakery.dao;

import com.mycompany.bakery.models.Product;
import com.mycompany.bakery.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCostPrice(rs.getDouble("cost_price"));
                p.setSellPrice(rs.getDouble("sell_price"));
                p.setQuantity(rs.getInt("quantity"));
                list.add(p);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public boolean insert(Product p) {
        String sql = "INSERT INTO product (id,name,description,cost_price,sell_price,quantity) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getCostPrice());
            ps.setDouble(5, p.getSellPrice());
            ps.setInt(6, p.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean update(Product p) {
        String sql = "UPDATE product SET name=?,description=?,cost_price=?,sell_price=?,quantity=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getCostPrice());
            ps.setDouble(4, p.getSellPrice());
            ps.setInt(5, p.getQuantity());
            ps.setString(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM product WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public Product findById(String id) {
        String sql = "SELECT * FROM product WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getString("id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setCostPrice(rs.getDouble("cost_price"));
                    p.setSellPrice(rs.getDouble("sell_price"));
                    p.setQuantity(rs.getInt("quantity"));
                    return p;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public List<Product> searchByName(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE name LIKE ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getString("id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setCostPrice(rs.getDouble("cost_price"));
                    p.setSellPrice(rs.getDouble("sell_price"));
                    p.setQuantity(rs.getInt("quantity"));
                    list.add(p);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }
}
