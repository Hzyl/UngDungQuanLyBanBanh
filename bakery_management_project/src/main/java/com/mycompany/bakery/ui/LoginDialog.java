package com.mycompany.bakery.ui;

import javax.swing.*;
import java.awt.*;
import com.mycompany.bakery.dao.EmployeeDAO;
import com.mycompany.bakery.models.Employee;

public class LoginDialog extends JDialog {
    private boolean succeeded = false;
    private JTextField tfUser;
    private JPasswordField pf;
    private JCheckBox cbShowPassword;
    private Employee loggedInEmployee = null;

    public LoginDialog(Frame parent, boolean modal) {
        super(parent, "Đăng nhập - Hệ thống quản lý cửa hàng bánh", modal);
        setSize(450,220);
        setLocationRelativeTo(parent);

        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        
        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        fields.add(new JLabel("Tên đăng nhập:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        tfUser = new JTextField();
        tfUser.setPreferredSize(new Dimension(250, 30));
        fields.add(tfUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        fields.add(new JLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        pf = new JPasswordField();
        pf.setPreferredSize(new Dimension(250, 30));
        fields.add(pf, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        cbShowPassword = new JCheckBox("Hiện mật khẩu");
        cbShowPassword.addActionListener(e -> {
            if (cbShowPassword.isSelected()) {
                pf.setEchoChar((char) 0);
            } else {
                pf.setEchoChar('●');
            }
        });
        fields.add(cbShowPassword, gbc);

        p.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton login = new JButton("Đăng nhập");
        login.setPreferredSize(new Dimension(120, 35));
        JButton cancel = new JButton("Thoát");
        cancel.setPreferredSize(new Dimension(120, 35));
        btns.add(login);
        btns.add(cancel);

        p.add(btns, BorderLayout.SOUTH);
        add(p);

        login.addActionListener(e -> {
            String u = tfUser.getText().trim();
            String pw = new String(pf.getPassword());
            
            if (u.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên đăng nhập và mật khẩu",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                EmployeeDAO dao = new EmployeeDAO();
                Employee emp = dao.authenticate(u, pw);
                
                if (emp != null) {
                    loggedInEmployee = emp;
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Sai tên đăng nhập hoặc mật khẩu!",
                        "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                    pf.setText("");
                    tfUser.requestFocus();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối database!\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cancel.addActionListener(e -> { succeeded = false; dispose(); });
        
        getRootPane().setDefaultButton(login);
    }

    public boolean isSucceeded(){
        return succeeded;
    }
    
    public Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }
}
