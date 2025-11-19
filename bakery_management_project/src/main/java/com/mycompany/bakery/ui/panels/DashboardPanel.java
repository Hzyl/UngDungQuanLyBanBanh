package com.mycompany.bakery.ui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.mycompany.bakery.dao.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(20,20,20,20));
        
        JLabel header = new JLabel("<html><h1>TỔNG QUAN HỆ THỐNG</h1></html>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);
        
        JPanel top = new JPanel(new GridLayout(1,4,15,15));
        top.setBorder(new EmptyBorder(20,10,20,10));
        
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        int totalProducts = productDAO.findAll().size();
        int totalOrders = orderDAO.findAll().size();
        int totalCustomers = customerDAO.findAll().size();
        int totalEmployees = employeeDAO.findAll().size();
        
        top.add(infoCard("Tổng sản phẩm", String.valueOf(totalProducts), new Color(52, 152, 219)));
        top.add(infoCard("Tổng đơn hàng", String.valueOf(totalOrders), new Color(46, 204, 113)));
        top.add(infoCard("Tổng khách hàng", String.valueOf(totalCustomers), new Color(155, 89, 182)));
        top.add(infoCard("Tổng nhân viên", String.valueOf(totalEmployees), new Color(241, 196, 15)));
        
        add(top, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new EmptyBorder(10,10,10,10));
        
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ta.setText("HỆ THỐNG QUẢN LÝ CỬA HÀNG BÁNH (NHÓM 02 - DCT123C4)\n\n" +
                   "Thành viên: \n" +
                   "\t+ Nguyễn Quốc Huy - 3123411120\n" +
                   "\t+ Văn Thành An - 3123411010\n" +
                   "\t+ Võ Trần My - 3123411193\n" +
                   "\t+ Hà Trần Gia Nguyên - 3123411199\n" +
                   
                   "Đồ án môn học: Phân tích thiết kế hướng đối tượng\n" +
                   "- Giảng viên hướng dẫn: Thầy Hoàng Mạnh Hà");
        bottom.add(new JScrollPane(ta), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
    
    private JPanel infoCard(String title, String value, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(color);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            new EmptyBorder(15,15,15,15)
        ));
        
        JLabel t = new JLabel("<html><div style='text-align:center; color:white;'><b>" + title + "</b></div></html>");
        t.setHorizontalAlignment(SwingConstants.CENTER);
        t.setForeground(Color.WHITE);
        
        JLabel v = new JLabel("<html><div style='text-align:center; color:white;'><h1>" + value + "</h1></div></html>");
        v.setHorizontalAlignment(SwingConstants.CENTER);
        v.setForeground(Color.WHITE);
        
        p.add(t, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }
}
