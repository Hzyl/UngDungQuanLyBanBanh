package com.mycompany.bakery.dao;

import com.mycompany.bakery.models.Order;
import com.mycompany.bakery.models.OrderItem;
import com.mycompany.bakery.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public boolean insertOrder(Order o) {
        String sqlOrder = "INSERT INTO orders (id, customer_id, employee_id, order_date, status) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO order_item (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String sqlUpdateProduct = "UPDATE product SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {
                psOrder.setString(1, o.getId());
                psOrder.setString(2, o.getCustomerId());
                psOrder.setString(3, o.getEmployeeId());
                psOrder.setTimestamp(4, new Timestamp(o.getOrderDate().getTime()));
                psOrder.setString(5, o.getStatus());
                psOrder.executeUpdate();
            }

            for (OrderItem it : o.getItems()) {
                try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                    psItem.setString(1, o.getId());
                    psItem.setString(2, it.getProductId());
                    psItem.setInt(3, it.getQuantity());
                    psItem.setDouble(4, it.getUnitPrice());
                    psItem.executeUpdate();
                }
                try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdateProduct)) {
                    psUpd.setInt(1, it.getQuantity());
                    psUpd.setString(2, it.getProductId());
                    psUpd.setInt(3, it.getQuantity());
                    int affected = psUpd.executeUpdate();
                    if (affected == 0) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            return false;
        } finally {
            try { 
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close(); 
                }
            } catch (SQLException e) { /* ignore */ }
        }
    }

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getString("id"));
                o.setCustomerId(rs.getString("customer_id"));
                o.setEmployeeId(rs.getString("employee_id"));
                o.setStatus(rs.getString("status"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                list.add(o);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public Order findById(String orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setId(rs.getString("id"));
                    o.setCustomerId(rs.getString("customer_id"));
                    o.setEmployeeId(rs.getString("employee_id"));
                    o.setStatus(rs.getString("status"));
                    o.setOrderDate(rs.getTimestamp("order_date"));
                    o.setItems(getOrderItems(orderId));
                    return o;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public List<OrderItem> getOrderItems(String orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getString("order_id"));
                    item.setProductId(rs.getString("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    items.add(item);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return items;
    }

    public boolean delete(String orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
            return false; 
        }
    }

    public List<Order> findCompletedOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = 'Completed' ORDER BY order_date DESC";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getString("id"));
                o.setCustomerId(rs.getString("customer_id"));
                o.setEmployeeId(rs.getString("employee_id"));
                o.setStatus(rs.getString("status"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setItems(getOrderItems(o.getId()));
                list.add(o);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }
}
