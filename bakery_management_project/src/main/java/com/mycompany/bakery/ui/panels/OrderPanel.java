package com.mycompany.bakery.ui.panels;

import com.mycompany.bakery.dao.OrderDAO;
import com.mycompany.bakery.dao.ProductDAO;
import com.mycompany.bakery.dao.CustomerDAO;
import com.mycompany.bakery.dao.EmployeeDAO;
import com.mycompany.bakery.models.Order;
import com.mycompany.bakery.models.OrderItem;
import com.mycompany.bakery.models.Product;
import com.mycompany.bakery.models.Customer;
import com.mycompany.bakery.models.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderPanel extends JPanel {
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    private DefaultTableModel orderModel;
    private JTable orderTable;
    private DefaultTableModel itemModel;
    private JTable itemTable;

    private JComboBox<String> cbCustomer, cbProduct;
    private JTextField txtQty, txtOrderId;

    private Order currentOrder = null;
    private Employee currentEmployee;

    public OrderPanel(Employee employee) {
        this.currentEmployee = employee;
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        orderModel = new DefaultTableModel(new Object[]{"Mã đơn","Khách hàng","Nhân viên","Ngày","Trạng thái"},0) { 
            public boolean isCellEditable(int r,int c){return false;} 
        };
        orderTable = new JTable(orderModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(6,6));
        JPanel topForm = new JPanel(new GridLayout(4,2,6,6));
        cbCustomer = new JComboBox<>();
        
        topForm.add(new JLabel("Mã đơn:")); 
        txtOrderId = new JTextField();
        topForm.add(txtOrderId);
        topForm.add(new JLabel("Khách hàng:")); 
        topForm.add(cbCustomer);
        topForm.add(new JLabel("Nhân viên:")); 
        topForm.add(new JLabel(currentEmployee.getName() + " (" + currentEmployee.getId() + ")"));
        
        JButton btnNew = new JButton("Tạo đơn mới"); 
        topForm.add(new JLabel()); 
        topForm.add(btnNew);
        right.add(topForm, BorderLayout.NORTH);

        itemModel = new DefaultTableModel(new Object[]{"Mã SP","Tên","Số lượng","Đơn giá","Thành tiền"},0) { 
            public boolean isCellEditable(int r,int c){return false;} 
        };
        itemTable = new JTable(itemModel);
        right.add(new JScrollPane(itemTable), BorderLayout.CENTER);

        JPanel addItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbProduct = new JComboBox<>(); 
        txtQty = new JTextField(4); 
        JButton btnAddItem = new JButton("Thêm SP");
        JButton btnRemoveItem = new JButton("Xóa SP");
        addItem.add(new JLabel("Sản phẩm:")); 
        addItem.add(cbProduct); 
        addItem.add(new JLabel("Số lượng:")); 
        addItem.add(txtQty); 
        addItem.add(btnAddItem);
        addItem.add(btnRemoveItem);
        right.add(addItem, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        JPanel bottom = new JPanel();
        JButton btnComplete = new JButton("Hoàn thành đơn"), btnCancel = new JButton("Hủy đơn");
        bottom.add(btnComplete); 
        bottom.add(btnCancel);
        add(bottom, BorderLayout.SOUTH);

        reloadCombos();
        refreshOrders();

        btnNew.addActionListener(e -> {
            String oid = txtOrderId.getText().trim();
            if (oid.isEmpty()) { JOptionPane.showMessageDialog(this, "Nhập mã đơn"); return; }
            String cid = (String) cbCustomer.getSelectedItem();
            if (cid == null) { JOptionPane.showMessageDialog(this, "Chọn khách hàng"); return; }
            
            currentOrder = new Order(oid, cid, currentEmployee.getId());
            itemModel.setRowCount(0);
            refreshOrders();
            JOptionPane.showMessageDialog(this, "Đã tạo đơn: " + oid + "\nNhân viên: " + currentEmployee.getName());
        });

        btnAddItem.addActionListener(e -> {
            if (currentOrder == null) { JOptionPane.showMessageDialog(this, "Tạo đơn trước"); return; }
            String pid = (String) cbProduct.getSelectedItem();
            if (pid == null) { JOptionPane.showMessageDialog(this, "Chọn sản phẩm"); return; }
            try {
                int q = Integer.parseInt(txtQty.getText().trim());
                if (q <= 0) { JOptionPane.showMessageDialog(this, "Số lượng phải > 0"); return; }
                
                Product p = productDAO.findById(pid);
                if (p == null) { JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại"); return; }
                if (p.getQuantity() < q) { 
                    JOptionPane.showMessageDialog(this, "Không đủ tồn kho\nTồn kho hiện tại: " + p.getQuantity()); 
                    return; 
                }
                
                OrderItem it = new OrderItem(currentOrder.getId(), pid, q, p.getSellPrice());
                currentOrder.getItems().add(it);
                itemModel.addRow(new Object[]{
                    p.getId(), 
                    p.getName(), 
                    q, 
                    String.format("%.0f", p.getSellPrice()), 
                    String.format("%.0f", p.getSellPrice()*q)
                });
                txtQty.setText("");
            } catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ"); 
            }
        });

        btnRemoveItem.addActionListener(e -> {
            int r = itemTable.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn sản phẩm trong đơn để xóa"); return; }
            if (currentOrder != null && currentOrder.getItems().size() > r) {
                currentOrder.getItems().remove(r);
                itemModel.removeRow(r);
            }
        });

        btnComplete.addActionListener(e -> {
            if (currentOrder == null) { JOptionPane.showMessageDialog(this, "Chọn hoặc tạo đơn trước"); return; }
            if (currentOrder.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Đơn hàng phải có ít nhất 1 sản phẩm");
                return;
            }
            
            currentOrder.setStatus("Completed");
            boolean ok = orderDAO.insertOrder(currentOrder);
            if (ok) { 
                JOptionPane.showMessageDialog(this, "Đơn hàng hoàn thành và đã lưu vào cơ sở dữ liệu"); 
                currentOrder = null; 
                itemModel.setRowCount(0); 
                refreshOrders(); 
                reloadCombos();
                txtOrderId.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Lưu đơn thất bại\n(Kiểm tra tồn kho hoặc mã đơn trùng)");
            }
        });

        btnCancel.addActionListener(e -> {
            if (currentOrder == null) { 
                JOptionPane.showMessageDialog(this, "Không có đơn hàng để hủy"); 
                return; 
            }
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn hủy đơn hàng này?", 
                "Xác nhận hủy", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                currentOrder = null; 
                itemModel.setRowCount(0); 
                txtOrderId.setText("");
                JOptionPane.showMessageDialog(this, "Đã hủy đơn hàng");
            }
        });

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            int r = orderTable.getSelectedRow();
            if (r>=0) {
                String oid = orderModel.getValueAt(r,0).toString();
                // Can load order details here if needed
            }
        });
    }

    private void reloadCombos() {
        cbCustomer.removeAllItems(); 
        cbProduct.removeAllItems();
        for (Customer c : customerDAO.findAll()) {
            cbCustomer.addItem(c.getId());
        }
        for (Product p : productDAO.findAll()) {
            cbProduct.addItem(p.getId());
        }
    }

    private void refreshOrders() {
        orderModel.setRowCount(0);
        for (Order o : orderDAO.findAll()) {
            orderModel.addRow(new Object[]{
                o.getId(), 
                o.getCustomerId(), 
                o.getEmployeeId(), 
                o.getOrderDate(), 
                o.getStatus()
            });
        }
    }
}
