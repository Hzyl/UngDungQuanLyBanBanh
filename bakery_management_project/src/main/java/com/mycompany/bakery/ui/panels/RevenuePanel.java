package com.mycompany.bakery.ui.panels;

import com.mycompany.bakery.dao.OrderDAO;
import com.mycompany.bakery.dao.ProductDAO;
import com.mycompany.bakery.models.Order;
import com.mycompany.bakery.models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class RevenuePanel extends JPanel {
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblTotalRevenue, lblTotalOrders, lblAvgOrderValue;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public RevenuePanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        
        JLabel header = new JLabel("<html><h1>BÁO CÁO DOANH THU</h1></html>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(1,3,15,15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        
        lblTotalRevenue = new JLabel("0 VNĐ");
        lblTotalOrders = new JLabel("0");
        lblAvgOrderValue = new JLabel("0 VNĐ");
        
        statsPanel.add(createStatCard("Tổng doanh thu", lblTotalRevenue, new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Số đơn hoàn thành", lblTotalOrders, new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Giá trị TB/đơn", lblAvgOrderValue, new Color(155, 89, 182)));
        
        add(statsPanel, BorderLayout.NORTH);
        
        model = new DefaultTableModel(new Object[]{"Mã đơn","Ngày","Khách hàng","Nhân viên","Số SP","Tổng tiền"},0) { 
            public boolean isCellEditable(int r,int c){return false;} 
        };
        table = new JTable(model);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết đơn hàng đã hoàn thành"));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnExport = new JButton("Xuất báo cáo");
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnExport);
        add(bottomPanel, BorderLayout.SOUTH);
        
        btnRefresh.addActionListener(e -> loadData());
        btnExport.addActionListener(e -> exportReport());
        
        loadData();
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(color);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(15,15,15,15)
        ));
        
        JLabel titleLabel = new JLabel("<html><div style='text-align:center; color:white;'><b>" + title + "</b></div></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        p.add(titleLabel, BorderLayout.NORTH);
        p.add(valueLabel, BorderLayout.CENTER);
        return p;
    }
    
    private void loadData() {
        model.setRowCount(0);
        double totalRevenue = 0;
        int totalOrders = 0;
        
        for (Order o : orderDAO.findCompletedOrders()) {
            double orderTotal = 0;
            int itemCount = 0;
            
            for (var item : o.getItems()) {
                orderTotal += item.getQuantity() * item.getUnitPrice();
                itemCount += item.getQuantity();
            }
            
            model.addRow(new Object[]{
                o.getId(), 
                String.format("%tF %<tR", o.getOrderDate()),
                o.getCustomerId(), 
                o.getEmployeeId(),
                itemCount,
                String.format("%,.0f VNĐ", orderTotal)
            });
            
            totalRevenue += orderTotal;
            totalOrders++;
        }
        
        lblTotalRevenue.setText(String.format("%,.0f VNĐ", totalRevenue));
        lblTotalOrders.setText(String.valueOf(totalOrders));
        lblAvgOrderValue.setText(totalOrders > 0 ? String.format("%,.0f VNĐ", totalRevenue / totalOrders) : "0 VNĐ");
    }
    
    private void exportReport() {
        StringBuilder report = new StringBuilder();
        report.append("BÁO CÁO DOANH THU CỬA HÀNG BÁNH\n");
        report.append("=====================================\n\n");
        report.append("Tổng doanh thu: ").append(lblTotalRevenue.getText()).append("\n");
        report.append("Tổng đơn hàng: ").append(lblTotalOrders.getText()).append("\n");
        report.append("Giá trị TB/đơn: ").append(lblAvgOrderValue.getText()).append("\n\n");
        report.append("Chi tiết:\n");
        report.append("Mã đơn\tNgày\tKhách\tNV\tSố SP\tTổng tiền\n");
        
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                report.append(model.getValueAt(i, j)).append("\t");
            }
            report.append("\n");
        }
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Báo cáo doanh thu", JOptionPane.INFORMATION_MESSAGE);
    }
}
