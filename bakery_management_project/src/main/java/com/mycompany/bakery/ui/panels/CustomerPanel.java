package com.mycompany.bakery.ui.panels;

import com.mycompany.bakery.dao.CustomerDAO;
import com.mycompany.bakery.models.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerPanel extends JPanel {
    private CustomerDAO dao = new CustomerDAO();
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtId, txtName, txtPhone, txtEmail, txtSearch;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,11}$");

    public CustomerPanel() {
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JLabel header = new JLabel("<html><h2>QUẢN LÝ KHÁCH HÀNG</h2></html>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);
        
        model = new DefaultTableModel(new Object[]{"Mã","Tên","SĐT","Email"},0) { 
            public boolean isCellEditable(int r,int c){return false;} 
        };
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(5,2,8,8));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        
        txtId = new JTextField(); 
        txtName = new JTextField(); 
        txtPhone = new JTextField(); 
        txtEmail = new JTextField();
        
        form.add(new JLabel("Mã khách hàng (*):"));
        form.add(txtId);
        form.add(new JLabel("Tên khách hàng (*):"));
        form.add(txtName);
        form.add(new JLabel("Số điện thoại:"));
        form.add(txtPhone);
        form.add(new JLabel("Email:"));
        form.add(txtEmail);

        JPanel right = new JPanel(new BorderLayout(8,8));
        right.setPreferredSize(new Dimension(350, 0));
        right.add(form, BorderLayout.NORTH);
        
        JPanel btns = new JPanel(new GridLayout(1,3,5,5)); 
        JButton add = new JButton("Thêm");
        JButton edit = new JButton("Sửa");
        JButton del = new JButton("Xóa");
        btns.add(add); 
        btns.add(edit); 
        btns.add(del);
        right.add(btns, BorderLayout.CENTER);

        JPanel searchP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchP.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
        txtSearch = new JTextField(15); 
        JButton bSearch = new JButton("Tìm"); 
        JButton bClear = new JButton("Làm mới");
        searchP.add(new JLabel("Tên:")); 
        searchP.add(txtSearch); 
        searchP.add(bSearch); 
        searchP.add(bClear);
        right.add(searchP, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        refresh();

        add.addActionListener(e -> {
            if (!validateInput()) return;
            
            String id = txtId.getText().trim();
            if (dao.findById(id) != null) {
                JOptionPane.showMessageDialog(this, "Mã khách hàng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Customer c = new Customer(id, txtName.getText().trim(), txtPhone.getText().trim(), txtEmail.getText().trim());
            if (dao.insert(c)) { 
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE); 
                refresh(); 
                clear(); 
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow(); 
            if (r<0) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            
            if (!validateInput()) return;
            
            Customer c = new Customer(txtId.getText().trim(), txtName.getText().trim(), txtPhone.getText().trim(), txtEmail.getText().trim());
            if (dao.update(c)) { 
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE); 
                refresh(); 
                clear(); 
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow(); 
            if (r<0) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            
            String id = model.getValueAt(r,0).toString();
            String name = model.getValueAt(r,1).toString();
            
            if (dao.hasOrders(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa khách hàng này!\nKhách hàng đã có đơn hàng trong hệ thống.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa khách hàng:\n" + name + " (" + id + ")?",
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.delete(id)) { 
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE); 
                    refresh(); 
                    clear(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow(); 
            if (r>=0) {
                txtId.setText(model.getValueAt(r,0).toString());
                txtName.setText(model.getValueAt(r,1).toString());
                txtPhone.setText(model.getValueAt(r,2).toString());
                txtEmail.setText(model.getValueAt(r,3).toString());
                txtId.setEnabled(false);
            }
        });

        bSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim().toLowerCase();
            if (q.isEmpty()) {
                refresh();
                return;
            }
            model.setRowCount(0);
            for (Customer c : dao.findAll()) {
                if (c.getName().toLowerCase().contains(q) || 
                    c.getPhone().contains(q) || 
                    c.getEmail().toLowerCase().contains(q)) {
                    model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail()});
                }
            }
        });
        
        bClear.addActionListener(e -> { 
            txtSearch.setText(""); 
            refresh(); 
            clear();
        });
    }
    
    private boolean validateInput() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtId.requestFocus();
            return false;
        }
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! (10-11 chữ số)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtPhone.requestFocus();
            return false;
        }
        
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }

    private void refresh() {
        model.setRowCount(0);
        for (Customer c : dao.findAll()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail()});
        }
    }
    
    private void clear() { 
        txtId.setText(""); 
        txtName.setText(""); 
        txtPhone.setText(""); 
        txtEmail.setText(""); 
        txtId.setEnabled(true); 
        table.clearSelection();
    }
}
