package view;

import controller.BookingController;
import controller.RuangDiskusiController;
import model.RuangDiskusi;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class BookingForm extends JDialog {
    private JTextField txtNIM;
    private JTextField txtNama;
    private JFormattedTextField txtTanggal;
    private JComboBox<String> cbRuang;
    private JSpinner spinnerJamMulai;
    private JSpinner spinnerJamSelesai;
    
    private BookingController bookingController;
    private RuangDiskusiController ruangController;
    private List<RuangDiskusi> allRuangan;
    
    private boolean isEditMode = false;
    private int editIdBooking = -1;

    public BookingForm(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        initController();
        initUI();
    }

    public BookingForm(JFrame parent, model.BookingRiwayat bookingToEdit) {
        super(parent, true);
        setUndecorated(true);
        isEditMode = true;
        editIdBooking = bookingToEdit.getIdBooking();
        initController();
        initUI();
        prefillData(bookingToEdit);
    }

    private void initController() {
        bookingController = new BookingController();
        ruangController = new RuangDiskusiController();
        allRuangan = ruangController.getAllRuangan();
    }

    private void initUI() {
        setSize(480, 600);
        setLocationRelativeTo(getParent());
        
        // Main wrapper
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCCCCCC), 1),
            BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));
        
        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("Form Pemesanan Ruang Diskusi");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0x1E1E1E));
        
        JLabel lblClose = new JLabel("X");
        lblClose.setFont(new Font("Arial", Font.PLAIN, 24));
        lblClose.setForeground(new Color(0x1E1E1E));
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblClose, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        
        // --- Form Panel ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        // 1. NIM
        formPanel.add(createLabel("NIM"));
        txtNIM = createTextField("Masukkan NIM Anda");
        formPanel.add(txtNIM);
        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        // 2. Nama Lengkap
        formPanel.add(createLabel("Nama Lengkap"));
        txtNama = createTextField("Masukkan Nama Lengkap Anda");
        formPanel.add(txtNama);
        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        // 3. Tanggal Pemesanan
        formPanel.add(createLabel("Tanggal Pemesanan"));
        try {
            javax.swing.text.MaskFormatter dateMask = new javax.swing.text.MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            txtTanggal = new JFormattedTextField(dateMask);
        } catch (java.text.ParseException e) {
            txtTanggal = new JFormattedTextField();
        }
        txtTanggal.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTanggal.setPreferredSize(new Dimension(0, 40));
        txtTanggal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTanggal.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtTanggal.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(4, new Color(0xCCCCCC)),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        txtTanggal.setForeground(new Color(0x1E1E1E));
        txtTanggal.setValue(LocalDate.now().toString());
        formPanel.add(txtTanggal);
        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        // 4 & 5. Jam Mulai & Selesai (Side by side)
        JPanel timePanel = new JPanel(new GridLayout(1, 2, 16, 0));
        timePanel.setBackground(Color.WHITE);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        
        JPanel pnlMulai = new JPanel();
        pnlMulai.setLayout(new BoxLayout(pnlMulai, BoxLayout.Y_AXIS));
        pnlMulai.setBackground(Color.WHITE);
        pnlMulai.add(createLabel("Jam Mulai"));
        spinnerJamMulai = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(spinnerJamMulai, "HH:mm");
        spinnerJamMulai.setEditor(timeEditor1);
        styleSpinner(spinnerJamMulai);
        pnlMulai.add(spinnerJamMulai);
        timePanel.add(pnlMulai);
        
        JPanel pnlSelesai = new JPanel();
        pnlSelesai.setLayout(new BoxLayout(pnlSelesai, BoxLayout.Y_AXIS));
        pnlSelesai.setBackground(Color.WHITE);
        pnlSelesai.add(createLabel("Jam Selesai"));
        spinnerJamSelesai = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(spinnerJamSelesai, "HH:mm");
        spinnerJamSelesai.setEditor(timeEditor2);
        spinnerJamSelesai.setEnabled(false);
        styleSpinner(spinnerJamSelesai);
        pnlSelesai.add(spinnerJamSelesai);
        timePanel.add(pnlSelesai);
        
        spinnerJamMulai.addChangeListener(e -> updateJamSelesai());
        updateJamSelesai();
        
        formPanel.add(timePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        // 6. Pilihan Ruangan
        formPanel.add(createLabel("Pilihan Ruangan"));
        cbRuang = new JComboBox<>();
        cbRuang.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbRuang.setBackground(Color.WHITE);
        cbRuang.setFont(new Font("Arial", Font.PLAIN, 14));
        cbRuang.setBorder(new RoundedBorder(4, new Color(0xCCCCCC)));
        cbRuang.setPreferredSize(new Dimension(0, 40));
        cbRuang.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        for (RuangDiskusi r : allRuangan) {
            cbRuang.addItem(r.getIdRuang() + " - " + r.getNamaRuang());
        }
        formPanel.add(cbRuang);
        
        // --- Footer Panel ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0xEEEEEE));
        
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 16));
        btnWrapper.setBackground(Color.WHITE);
        
        JButton btnBatal = new JButton("Batal");
        btnBatal.setFont(new Font("Arial", Font.BOLD, 14));
        btnBatal.setBackground(Color.WHITE);
        btnBatal.setForeground(new Color(0x1E1E1E));
        btnBatal.setFocusPainted(false);
        btnBatal.setBorder(new RoundedBorder(4, new Color(0xCCCCCC)));
        btnBatal.setPreferredSize(new Dimension(100, 40));
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBatal.addActionListener(e -> dispose());
        
        JButton btnSimpan = new JButton("Simpan") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); // radius 4 = diameter 8
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 14));
        btnSimpan.setBackground(new Color(0x478E54));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorderPainted(false);
        btnSimpan.setContentAreaFilled(false);
        btnSimpan.setPreferredSize(new Dimension(100, 40));
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> prosesPemesanan(txtTanggal.getText()));
        
        btnWrapper.add(btnBatal);
        btnWrapper.add(btnSimpan);
        
        footerPanel.add(separator, BorderLayout.NORTH);
        footerPanel.add(btnWrapper, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(0x1E1E1E));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Arial", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(0, 40));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(4, new Color(0xCCCCCC)),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        tf.setForeground(new Color(0x1E1E1E));
        return tf;
    }
    

    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Arial", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(0, 40));
        spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        spinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinner.setBorder(new RoundedBorder(4, new Color(0xCCCCCC)));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(Color.WHITE);
        editor.getTextField().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    private void prefillData(model.BookingRiwayat b) {
        txtNIM.setText(b.getNim());
        txtNama.setText(b.getNamaMahasiswa());
        txtTanggal.setValue(b.getTanggalBooking().toString());
        
        for (RuangDiskusi r : allRuangan) {
            if (r.getIdRuang().equals(b.getIdRuang())) {
                cbRuang.setSelectedItem(r.getIdRuang() + " - " + r.getNamaRuang());
                break;
            }
        }
        
        java.util.Calendar calMulai = java.util.Calendar.getInstance();
        calMulai.setTime(b.getJamMulai());
        spinnerJamMulai.setValue(calMulai.getTime());
    }

    private void updateJamSelesai() {
        java.util.Date dateMulai = (java.util.Date) spinnerJamMulai.getValue();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(dateMulai);
        cal.add(java.util.Calendar.HOUR_OF_DAY, 2);
        spinnerJamSelesai.setValue(cal.getTime());
    }

    private void prosesPemesanan(String tglString) {
        String nim = txtNIM.getText().trim();
        String nama = txtNama.getText().trim();
        String selectedRuang = (String) cbRuang.getSelectedItem();
        
        if (nim.isEmpty() || nama.isEmpty() || selectedRuang == null) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idRuang = selectedRuang.split(" - ")[0];
        Date tanggal;
        try {
            tanggal = Date.valueOf(tglString);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah! Gunakan YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        java.util.Date utilJamMulai = (java.util.Date) spinnerJamMulai.getValue();
        java.util.Date utilJamSelesai = (java.util.Date) spinnerJamSelesai.getValue();
        
        Time jamMulai = new Time(utilJamMulai.getTime());
        Time jamSelesai = new Time(utilJamSelesai.getTime());

        String hasil;
        if (isEditMode) {
            hasil = bookingController.updateBooking(editIdBooking, nim, nama, idRuang, tanggal, jamMulai, jamSelesai);
        } else {
            hasil = bookingController.prosesBooking(nim, nama, idRuang, tanggal, jamMulai, jamSelesai);
        }

        if (hasil.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, isEditMode ? "Perubahan berhasil disimpan!" : "Pemesanan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            Container parent = getParent();
            if (parent instanceof MainDashboard) {
                ((MainDashboard) parent).switchToRiwayatTab();
            }
            
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, hasil, isEditMode ? "Edit Gagal" : "Pemesanan Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class RoundedBorder extends AbstractBorder {
    private int radius;
    private Color color;
    
    public RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius * 2, radius * 2);
        g2.dispose();
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius/2, radius/2, radius/2, radius/2);
    }
    
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = insets.bottom = insets.top = radius/2;
        return insets;
    }
}
