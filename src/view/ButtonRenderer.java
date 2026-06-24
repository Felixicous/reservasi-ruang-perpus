package view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton btnEdit = new JButton();
        JButton btnDelete = new JButton();
        
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
        
        add(btnEdit);
        add(btnDelete);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}
