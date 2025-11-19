package com.mycompany.bakery.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import com.mycompany.bakery.ui.panels.*;
import com.mycompany.bakery.models.Employee;

public class MainFrame extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);
    JLabel statusBar = new JLabel("Chào mừng!");
    private Employee currentEmployee;

    public MainFrame(Employee employee) {
        this.currentEmployee = employee;
        setTitle("Hệ thống Quản lý cửa hàng bánh - " + employee.getName() + " (" + employee.getRoleText() + ")");
        setSize(1200,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(15,15,15,15));
        left.setPreferredSize(new Dimension(260, getHeight()));
        left.setBackground(new Color(240, 240, 245));

        JLabel title = new JLabel("<html><div style='text-align:center;'><h2>CỬA HÀNG BÁNH</h2>" + 
                                    "<p style='font-size:10px;'>Xin chào: " + employee.getName() + "</p></div></html>");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(title);
        left.add(Box.createRigidArea(new Dimension(0,15)));

        boolean isAdmin = employee.isAdmin();
        
        String[] btnNames;
        if (isAdmin) {
            btnNames = new String[]{"Tổng quan","Quản lý sản phẩm","Quản lý đơn hàng","Quản lý doanh thu","Quản lý khách hàng","Quản lý nhân viên","Đăng xuất"};
        } else {
            btnNames = new String[]{"Tổng quan","Quản lý sản phẩm","Quản lý đơn hàng","Quản lý khách hàng","Đăng xuất"};
        }
        
        for (String name : btnNames) {
            JButton b = new JButton(name);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            left.add(b);
            left.add(Box.createRigidArea(new Dimension(0,10)));
            b.addActionListener(e -> {
                if ("Đăng xuất".equals(name)) {
                    int c = JOptionPane.showConfirmDialog(this,"Bạn có muốn đăng xuất?","Xác nhận",JOptionPane.YES_NO_OPTION);
                    if (c==JOptionPane.YES_OPTION) { dispose(); System.exit(0); }
                    return;
                }
                cardLayout.show(cardPanel, name);
                statusBar.setText("Đang ở: " + name + " | Người dùng: " + employee.getName() + " (" + employee.getRoleText() + ")");
            });
        }

        cardPanel.add(new DashboardPanel(), "Tổng quan");
        cardPanel.add(new ProductPanel(), "Quản lý sản phẩm");
        cardPanel.add(new OrderPanel(currentEmployee), "Quản lý đơn hàng");
        cardPanel.add(new CustomerPanel(), "Quản lý khách hàng");
        
        if (isAdmin) {
            cardPanel.add(new RevenuePanel(), "Quản lý doanh thu");
            cardPanel.add(new EmployeePanel(), "Quản lý nhân viên");
        }

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, cardPanel);
        split.setDividerLocation(260);
        add(split, BorderLayout.CENTER);

        statusBar.setBorder(new EmptyBorder(8,15,8,15));
        statusBar.setText("Đang ở: Tổng quan | Người dùng: " + employee.getName() + " (" + employee.getRoleText() + ")");
        add(statusBar, BorderLayout.SOUTH);

        cardLayout.show(cardPanel, "Tổng quan");
    }
}
