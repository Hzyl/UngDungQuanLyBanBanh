package com.mycompany.bakery;

import javax.swing.*;
import java.sql.Connection;

import com.mycompany.bakery.ui.MainFrame;
import com.mycompany.bakery.ui.LoginDialog;
import com.mycompany.bakery.models.Employee;
import com.mycompany.bakery.util.DBUtil;

public class Main {
    public static void main(String[] args) {
        // Test kết nối database trước
        try {
            Connection conn = DBUtil.getConnection();
            System.out.println("✓ Kết nối database thành công!");
            conn.close();
        } catch (Exception e) {
            System.err.println("✗ Lỗi kết nối database:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Không thể kết nối database!\nKiểm tra:\n" +
                "1. MySQL đã chạy chưa?\n" +
                "2. Database 'bakerydb' đã tạo chưa?\n" +
                "3. Mật khẩu MySQL có đúng không?\n\n" +
                "Lỗi: " + e.getMessage(),
                "Lỗi Database", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            LoginDialog login = new LoginDialog(null, true);
            login.setVisible(true);
            if (login.isSucceeded()) {
                Employee employee = login.getLoggedInEmployee();
                System.out.println("Đăng nhập thành công: " + employee.getName() + " - Role: " + employee.getRoleText());
                try {
                    MainFrame frame = new MainFrame(employee);
                    frame.setVisible(true);
                    System.out.println("Đã mở MainFrame");
                } catch (Exception ex) {
                    System.err.println("Lỗi khi mở MainFrame:");
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Lỗi khi mở giao diện chính!\n" + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Hủy đăng nhập");
                System.exit(0);
            }
        });
    }
}
