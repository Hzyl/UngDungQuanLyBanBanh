package com.mycompany.bakery.dao;

import com.mycompany.bakery.models.Employee;
import com.mycompany.bakery.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee t = new Employee();
                t.setId(rs.getString("id"));
                t.setName(rs.getString("name"));
                t.setUsername(rs.getString("username"));
                t.setPassword(rs.getString("password"));
                t.setRole(rs.getInt("role"));
                t.setSalary(rs.getDouble("salary"));
                list.add(t);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public boolean insert(Employee em) {
        String sql = "INSERT INTO employee (id,name,username,password,role,salary) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, em.getId());
            ps.setString(2, em.getName());
            ps.setString(3, em.getUsername());
            ps.setString(4, em.getPassword());
            ps.setInt(5, em.getRole());
            ps.setDouble(6, em.getSalary());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean update(Employee em) {
        String sql = "UPDATE employee SET name=?,username=?,password=?,role=?,salary=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, em.getName());
            ps.setString(2, em.getUsername());
            ps.setString(3, em.getPassword());
            ps.setInt(4, em.getRole());
            ps.setDouble(5, em.getSalary());
            ps.setString(6, em.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM employee WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public Employee authenticate(String username, String password) {
        String sql = "SELECT * FROM employee WHERE username=? AND password=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee em = new Employee();
                    em.setId(rs.getString("id"));
                    em.setName(rs.getString("name"));
                    em.setUsername(rs.getString("username"));
                    em.setPassword(rs.getString("password"));
                    em.setRole(rs.getInt("role"));
                    em.setSalary(rs.getDouble("salary"));
                    return em;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }
}
