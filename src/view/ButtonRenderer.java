package view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");
        
        btnEdit.setBackground(new Color(50, 150, 250));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setOpaque(true);
        btnEdit.setBorderPainted(false);
        
        btnDelete.setBackground(new Color(250, 50, 50));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);
        
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
