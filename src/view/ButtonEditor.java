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
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Hapus");
        
        btnEdit.setBackground(new Color(50, 150, 250));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setOpaque(true);
        btnEdit.setBorderPainted(false);
        
        btnDelete.setBackground(new Color(250, 50, 50));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);
        
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
