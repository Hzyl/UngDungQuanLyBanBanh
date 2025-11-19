package com.mycompany.bakery.ui.panels;

import com.mycompany.bakery.dao.EmployeeDAO;
import com.mycompany.bakery.models.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeePanel extends JPanel {
    private EmployeeDAO dao = new EmployeeDAO();
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtId, txtName, txtUsername, txtPassword, txtSalary, txtSearch;
    private JComboBox<String> cboRole;

    public EmployeePanel() {
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        model = new DefaultTableModel(new Object[]{"Mã","Tên","Username","Vai trò","Lương"},0) { 
            public boolean isCellEditable(int r,int c){return false;} 
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(7,2,6,6));
        txtId = new JTextField(); 
        txtName = new JTextField(); 
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cboRole = new JComboBox<>(new String[]{"Admin", "Nhân viên"});
        cboRole.setSelectedIndex(1);
        txtSalary = new JTextField();
        
        form.add(new JLabel("Mã NV:")); form.add(txtId);
        form.add(new JLabel("Tên:")); form.add(txtName);
        form.add(new JLabel("Username:")); form.add(txtUsername);
        form.add(new JLabel("Password:")); form.add(txtPassword);
        form.add(new JLabel("Vai trò:")); form.add(cboRole);
        form.add(new JLabel("Lương:")); form.add(txtSalary);

        JPanel right = new JPanel(new BorderLayout(6,6));
        right.add(form, BorderLayout.NORTH);
        JPanel btns = new JPanel(); 
        JButton add = new JButton("Thêm"), edit = new JButton("Sửa"), del = new JButton("Xóa");
        btns.add(add); btns.add(edit); btns.add(del);
        right.add(btns, BorderLayout.CENTER);

        JPanel searchP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(12); 
        JButton bSearch = new JButton("Tìm"); 
        JButton bClear = new JButton("Làm mới");
        searchP.add(new JLabel("Tìm theo tên:")); 
        searchP.add(txtSearch); 
        searchP.add(bSearch); 
        searchP.add(bClear);
        right.add(searchP, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        refresh();

        add.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Nhập mã nhân viên"); return; }
            try {
                int roleValue = cboRole.getSelectedIndex(); // 0=Admin, 1=Nhân viên
                Employee em = new Employee(
                    id, 
                    txtName.getText().trim(), 
                    txtUsername.getText().trim(),
                    txtPassword.getText().trim(),
                    roleValue, 
                    Double.parseDouble(txtSalary.getText().trim())
                );
                if (dao.insert(em)) { 
                    JOptionPane.showMessageDialog(this, "Thêm thành công"); 
                    refresh(); 
                    clear(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Thất bại (Username có thể đã tồn tại)");
                }
            } catch (Exception ex){ 
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage()); 
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow(); 
            if (r<0) { JOptionPane.showMessageDialog(this, "Chọn nhân viên"); return; }
            try {
                int roleValue = cboRole.getSelectedIndex();
                Employee em = new Employee(
                    txtId.getText().trim(), 
                    txtName.getText().trim(), 
                    txtUsername.getText().trim(),
                    txtPassword.getText().trim(),
                    roleValue, 
                    Double.parseDouble(txtSalary.getText().trim())
                );
                if (dao.update(em)) { 
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công"); 
                    refresh(); 
                    clear(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Thất bại");
                }
            } catch (Exception ex){ 
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage()); 
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow(); 
            if (r<0) { JOptionPane.showMessageDialog(this, "Chọn nhân viên"); return; }
            String id = model.getValueAt(r,0).toString();
            if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + id + "?","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if (dao.delete(id)) { 
                    JOptionPane.showMessageDialog(this, "Xóa thành công"); 
                    refresh(); 
                    clear(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Thất bại");
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow(); 
            if (r>=0) {
                txtId.setText(model.getValueAt(r,0).toString());
                txtName.setText(model.getValueAt(r,1).toString());
                txtUsername.setText(model.getValueAt(r,2).toString());
                txtPassword.setText("");
                String roleText = model.getValueAt(r,3).toString();
                cboRole.setSelectedIndex(roleText.equals("Admin") ? 0 : 1);
                txtSalary.setText(model.getValueAt(r,4).toString());
                txtId.setEnabled(false);
                txtUsername.setEnabled(false);
            }
        });

        bSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim().toLowerCase();
            model.setRowCount(0);
            for (Employee em : dao.findAll()) {
                if (em.getName().toLowerCase().contains(q)) {
                    model.addRow(new Object[]{em.getId(), em.getName(), em.getUsername(), em.getRoleText(), em.getSalary()});
                }
            }
        });
        bClear.addActionListener(e -> { 
            txtSearch.setText(""); 
            refresh(); 
            txtId.setEnabled(true); 
            txtUsername.setEnabled(true);
        });
    }

    private void refresh() {
        model.setRowCount(0);
        for (Employee e : dao.findAll()) {
            model.addRow(new Object[]{e.getId(), e.getName(), e.getUsername(), e.getRoleText(), e.getSalary()});
        }
    }
    
    private void clear(){ 
        txtId.setText(""); 
        txtName.setText(""); 
        txtUsername.setText("");
        txtPassword.setText("");
        cboRole.setSelectedIndex(1);
        txtSalary.setText(""); 
        txtId.setEnabled(true); 
        txtUsername.setEnabled(true);
    }
}
