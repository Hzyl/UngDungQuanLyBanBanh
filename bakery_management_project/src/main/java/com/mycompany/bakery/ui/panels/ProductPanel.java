package com.mycompany.bakery.ui.panels;

import com.mycompany.bakery.dao.ProductDAO;
import com.mycompany.bakery.models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {
    private ProductDAO productDAO = new ProductDAO();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtId, txtName, txtDesc, txtCost, txtSell, txtQty, txtSearch;

    public ProductPanel() {
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JLabel header = new JLabel("<html><h2>QUẢN LÝ SẢN PHẨM</h2></html>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel(new Object[]{"Mã","Tên","Mô tả","Giá vốn","Giá bán","Tồn kho"},0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(7,2,8,8));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        
        txtId = new JTextField(); 
        txtName = new JTextField(); 
        txtDesc = new JTextField();
        txtCost = new JTextField(); 
        txtSell = new JTextField(); 
        txtQty = new JTextField();
        
        form.add(new JLabel("Mã sản phẩm (*):"));
        form.add(txtId);
        form.add(new JLabel("Tên sản phẩm (*):"));
        form.add(txtName);
        form.add(new JLabel("Mô tả:"));
        form.add(txtDesc);
        form.add(new JLabel("Giá vốn (VNĐ) (*):"));
        form.add(txtCost);
        form.add(new JLabel("Giá bán (VNĐ) (*):"));
        form.add(txtSell);
        form.add(new JLabel("Số lượng (*):"));
        form.add(txtQty);

        JPanel right = new JPanel(new BorderLayout(8,8));
        right.setPreferredSize(new Dimension(350, getHeight()));
        right.add(form, BorderLayout.NORTH);
        
        JPanel btns = new JPanel(new GridLayout(1,3,5,5));
        JButton add = new JButton("Thêm");
        JButton edit = new JButton("Sửa");
        JButton del = new JButton("Xóa");
        btns.add(add); 
        btns.add(edit); 
        btns.add(del);
        right.add(btns, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
        txtSearch = new JTextField(15); 
        JButton bSearch = new JButton("Tìm"); 
        JButton bClear = new JButton("Làm mới");
        searchPanel.add(new JLabel("Tên:")); 
        searchPanel.add(txtSearch); 
        searchPanel.add(bSearch); 
        searchPanel.add(bClear);
        right.add(searchPanel, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        refreshTable();

        add.addActionListener(e -> {
            if (!validateInput()) return;
            
            String id = txtId.getText().trim();
            if (productDAO.findById(id) != null) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Product p = new Product(
                    id, 
                    txtName.getText().trim(), 
                    txtDesc.getText().trim(),
                    Double.parseDouble(txtCost.getText().trim()), 
                    Double.parseDouble(txtSell.getText().trim()),
                    Integer.parseInt(txtQty.getText().trim())
                );
                
                if (productDAO.insert(p)) { 
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE); 
                    refreshTable(); 
                    clearForm(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(this, "Dữ liệu số không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE); 
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r<0) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            
            if (!validateInput()) return;
            
            try {
                Product p = new Product(
                    txtId.getText().trim(), 
                    txtName.getText().trim(), 
                    txtDesc.getText().trim(),
                    Double.parseDouble(txtCost.getText().trim()), 
                    Double.parseDouble(txtSell.getText().trim()),
                    Integer.parseInt(txtQty.getText().trim())
                );
                
                if (productDAO.update(p)) { 
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE); 
                    refreshTable(); 
                    clearForm(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(this, "Dữ liệu số không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE); 
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r<0) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            
            String id = tableModel.getValueAt(r,0).toString();
            String name = tableModel.getValueAt(r,1).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa sản phẩm:\n" + name + " (" + id + ")?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                if (productDAO.delete(id)) { 
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE); 
                    refreshTable(); 
                    clearForm(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!\nSản phẩm có thể đang được sử dụng trong đơn hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if (r>=0) {
                txtId.setText(tableModel.getValueAt(r,0).toString());
                txtName.setText(tableModel.getValueAt(r,1).toString());
                txtDesc.setText(tableModel.getValueAt(r,2).toString());
                txtCost.setText(tableModel.getValueAt(r,3).toString());
                txtSell.setText(tableModel.getValueAt(r,4).toString());
                txtQty.setText(tableModel.getValueAt(r,5).toString());
                txtId.setEnabled(false);
            }
        });

        bSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim();
            if (q.isEmpty()) { 
                refreshTable(); 
                return; 
            }
            List<Product> list = productDAO.searchByName(q);
            showProducts(list);
        });
        
        bClear.addActionListener(e -> { 
            txtSearch.setText(""); 
            refreshTable(); 
            clearForm();
        });
    }
    
    private boolean validateInput() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String cost = txtCost.getText().trim();
        String sell = txtSell.getText().trim();
        String qty = txtQty.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtId.requestFocus();
            return false;
        }
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        
        if (cost.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá vốn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtCost.requestFocus();
            return false;
        }
        
        if (sell.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá bán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSell.requestFocus();
            return false;
        }
        
        if (qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtQty.requestFocus();
            return false;
        }
        
        try {
            double costPrice = Double.parseDouble(cost);
            double sellPrice = Double.parseDouble(sell);
            int quantity = Integer.parseInt(qty);
            
            if (costPrice < 0) {
                JOptionPane.showMessageDialog(this, "Giá vốn phải >= 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (sellPrice < 0) {
                JOptionPane.showMessageDialog(this, "Giá bán phải >= 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải >= 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (sellPrice < costPrice) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Giá bán thấp hơn giá vốn!\nBạn có chắc muốn tiếp tục?",
                    "Cảnh báo", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                return confirm == JOptionPane.YES_OPTION;
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu số không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void refreshTable() {
        List<Product> list = productDAO.findAll();
        showProducts(list);
    }

    private void showProducts(List<Product> list) {
        tableModel.setRowCount(0);
        for (Product p : list) {
            tableModel.addRow(new Object[]{
                p.getId(), 
                p.getName(), 
                p.getDescription(), 
                String.format("%,.0f", p.getCostPrice()), 
                String.format("%,.0f", p.getSellPrice()), 
                p.getQuantity()
            });
        }
    }

    private void clearForm() {
        txtId.setText(""); 
        txtName.setText(""); 
        txtDesc.setText(""); 
        txtCost.setText(""); 
        txtSell.setText(""); 
        txtQty.setText("");
        txtId.setEnabled(true);
        table.clearSelection();
    }
}
