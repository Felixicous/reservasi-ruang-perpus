package view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.BookingRiwayat;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private BookingRiwayat currentRiwayat;
    private MainDashboard dashboard;

    public ButtonEditor(MainDashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnEdit = new JButton();
        btnDelete = new JButton();
        
        try {
            ImageIcon iconEdit = new ImageIcon("src/assets/icon_edit.png");
            Image imgEdit = iconEdit.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnEdit.setIcon(new ImageIcon(imgEdit));
            
            ImageIcon iconDelete = new ImageIcon("src/assets/icon_delete.png");
            Image imgDelete = iconDelete.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnDelete.setIcon(new ImageIcon(imgDelete));
        } catch (Exception e) {}

        btnEdit.setPreferredSize(new Dimension(32, 32));
        btnEdit.setBackground(Color.WHITE);
        btnEdit.setContentAreaFilled(false);
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnDelete.setPreferredSize(new Dimension(32, 32));
        btnDelete.setBackground(Color.WHITE);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                if (currentRiwayat != null) {
                    BookingForm form = new BookingForm(dashboard, currentRiwayat);
                    form.setVisible(true);
                    dashboard.loadDataRuangan();
                }
            }
        });
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                if (currentRiwayat != null) {
                    int confirm = JOptionPane.showConfirmDialog(dashboard, "Yakin ingin menghapus riwayat ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = dashboard.getBookingController().deleteBooking(currentRiwayat.getIdBooking());
                        if (success) {
                            JOptionPane.showMessageDialog(dashboard, "Berhasil dihapus!");
                            dashboard.loadDataRuangan();
                        } else {
                            JOptionPane.showMessageDialog(dashboard, "Gagal menghapus!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        
        panel.add(btnEdit);
        panel.add(btnDelete);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof BookingRiwayat) {
            currentRiwayat = (BookingRiwayat) value;
        }
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentRiwayat;
    }
}
