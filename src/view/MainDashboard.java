package view;

import controller.RuangDiskusiController;
import model.RuangDiskusi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainDashboard extends JFrame {
    
    private JTable tableRuangan;
    private DefaultTableModel tableModel;
    private JTable tableRiwayat;
    private DefaultTableModel modelRiwayat;
    
    private JTable tableRuanganFull;
    private JTable tableRiwayatFull;
    
    private CardLayout dashboardHistoryLayout;
    private JPanel dashboardHistoryContainer;
    
    private CardLayout fullHistoryLayout;
    private JPanel fullHistoryContainer;
    
    private JPanel mainCardPanel;
    private CardLayout cardLayout;
    
    private controller.BookingController bookingController;
    private RuangDiskusiController controller;

    public MainDashboard() {
        controller = new RuangDiskusiController();
        bookingController = new controller.BookingController();

        setTitle("Sistem Reservasi Ruang Diskusi Perpustakaan UPN");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        // Main Layout
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34)); // Forest Green
        JLabel titleLabel = new JLabel("Dashboard Ketersediaan Ruang Diskusi");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Card Layout Area
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        
        // ==========================================
        // CARD 1: DASHBOARD (Tampilan Saat Ini)
        // ==========================================
        JPanel dashboardCard = new JPanel(new BorderLayout());
        dashboardCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Setup Table
        String[] columns = {"ID Ruang", "Nama Ruang", "Tipe", "Lantai", "Kapasitas (Kursi)", "Fasilitas", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        tableRuangan = new JTable(tableModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                int height = getRowHeight() * (getRowCount() == 0 ? 1 : getRowCount());
                return new Dimension(super.getPreferredSize().width, Math.min(height, 350)); // Max height 350px
            }
        };
        tableRuangan.setRowHeight(30);
        tableRuangan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(tableRuangan);
        
        JLabel labelDaftarRuangan = new JLabel("Daftar Ruangan");
        labelDaftarRuangan.setFont(new Font("Arial", Font.BOLD, 18));
        labelDaftarRuangan.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JButton btnBookRoomBottom = new JButton("Booking Ruangan") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(getBackground().brighter());
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnBookRoomBottom.setFont(new Font("Arial", Font.BOLD, 14));
        btnBookRoomBottom.setPreferredSize(new Dimension(0, 40));
        btnBookRoomBottom.setBackground(new Color(34, 139, 34)); // Hijau
        btnBookRoomBottom.setForeground(Color.WHITE); // Putih
        btnBookRoomBottom.setFocusPainted(false);
        btnBookRoomBottom.setBorderPainted(false);
        btnBookRoomBottom.setContentAreaFilled(false);
        
        JPanel bottomBtnContainer = new JPanel(new BorderLayout());
        bottomBtnContainer.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0)); // Jarak 24px dari tabel
        bottomBtnContainer.add(btnBookRoomBottom, BorderLayout.CENTER);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.add(labelDaftarRuangan, BorderLayout.NORTH);
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        tableWrapper.add(bottomBtnContainer, BorderLayout.SOUTH);
        
        dashboardCard.add(tableWrapper, BorderLayout.NORTH);
        
        // SETUP TABEL RIWAYAT DI DASHBOARD
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel labelRiwayat = new JLabel("Riwayat Booking");
        labelRiwayat.setFont(new Font("Arial", Font.BOLD, 18));
        labelRiwayat.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        historyPanel.add(labelRiwayat, BorderLayout.NORTH);
        
        String[] columnsRiwayat = {"Ruangan", "Peminjam", "Tanggal", "Jam Mulai", "Jam Selesai", "Status", "Aksi"};
        modelRiwayat = new DefaultTableModel(columnsRiwayat, 0);
        tableRiwayat = new JTable(modelRiwayat);
        tableRiwayat.setFillsViewportHeight(true);
        tableRiwayat.setRowHeight(35);
        tableRiwayat.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        tableRiwayat.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tableRiwayat.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(this));
        tableRiwayat.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        JScrollPane scrollRiwayat = new JScrollPane(tableRiwayat);
        
        JLabel emptyRiwayatDashboard = new JLabel("Belum ada yang memesan ruangan", SwingConstants.CENTER);
        emptyRiwayatDashboard.setFont(new Font("Arial", Font.BOLD, 16));
        emptyRiwayatDashboard.setForeground(Color.DARK_GRAY);
        
        dashboardHistoryLayout = new CardLayout();
        dashboardHistoryContainer = new JPanel(dashboardHistoryLayout);
        dashboardHistoryContainer.add(scrollRiwayat, "TABLE");
        dashboardHistoryContainer.add(emptyRiwayatDashboard, "EMPTY");

        historyPanel.add(dashboardHistoryContainer, BorderLayout.CENTER);
        
        dashboardCard.add(historyPanel, BorderLayout.CENTER);
        
        // Menambahkan card dashboard
        mainCardPanel.add(dashboardCard, "Dashboard");

        // ==========================================
        // CARD 2: DAFTAR RUANGAN (Full)
        // ==========================================
        JPanel ruanganCard = new JPanel(new BorderLayout());
        ruanganCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel labelRuanganFull = new JLabel("Semua Daftar Ruangan");
        labelRuanganFull.setFont(new Font("Arial", Font.BOLD, 22));
        labelRuanganFull.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        ruanganCard.add(labelRuanganFull, BorderLayout.NORTH);
        
        tableRuanganFull = new JTable(tableModel);
        tableRuanganFull.setRowHeight(30);
        tableRuanganFull.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollRuanganFull = new JScrollPane(tableRuanganFull);
        ruanganCard.add(scrollRuanganFull, BorderLayout.CENTER);
        
        mainCardPanel.add(ruanganCard, "Daftar Ruangan");

        // ==========================================
        // CARD 3: RIWAYAT PEMESANAN (Full)
        // ==========================================
        JPanel riwayatCardFull = new JPanel(new BorderLayout());
        riwayatCardFull.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel labelRiwayatFull = new JLabel("Semua Riwayat Pemesanan");
        labelRiwayatFull.setFont(new Font("Arial", Font.BOLD, 22));
        labelRiwayatFull.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        riwayatCardFull.add(labelRiwayatFull, BorderLayout.NORTH);
        
        tableRiwayatFull = new JTable(modelRiwayat);
        tableRiwayatFull.setFillsViewportHeight(true);
        tableRiwayatFull.setRowHeight(35);
        tableRiwayatFull.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        tableRiwayatFull.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tableRiwayatFull.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(this));
        tableRiwayatFull.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        JScrollPane scrollRiwayatFull = new JScrollPane(tableRiwayatFull);
        
        JLabel emptyRiwayatFull = new JLabel("Belum ada yang memesan ruangan", SwingConstants.CENTER);
        emptyRiwayatFull.setFont(new Font("Arial", Font.BOLD, 18));
        emptyRiwayatFull.setForeground(Color.DARK_GRAY);
        
        fullHistoryLayout = new CardLayout();
        fullHistoryContainer = new JPanel(fullHistoryLayout);
        fullHistoryContainer.add(scrollRiwayatFull, "TABLE");
        fullHistoryContainer.add(emptyRiwayatFull, "EMPTY");

        riwayatCardFull.add(fullHistoryContainer, BorderLayout.CENTER);
        
        mainCardPanel.add(riwayatCardFull, "Riwayat Pemesanan");

        // Tambahkan CardLayout container ke tengah JFrame
        add(mainCardPanel, BorderLayout.CENTER);
        
        // ==========================================
        // Navigation Buttons (Left Menu Sidebar)
        // ==========================================
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Navigation Tabs
        JButton btnTabDashboard = new JButton("Dashboard");
        btnTabDashboard.setMaximumSize(new Dimension(170, 40));
        
        JButton btnTabRuangan = new JButton("Daftar Ruangan");
        btnTabRuangan.setMaximumSize(new Dimension(170, 40));
        
        JButton btnTabRiwayat = new JButton("Riwayat Pemesanan");
        btnTabRiwayat.setMaximumSize(new Dimension(170, 40));
        
        // Action Buttons
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setMaximumSize(new Dimension(170, 40));
        
        JButton btnBookRoom = new JButton("Booking Ruangan") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(getBackground().brighter());
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnBookRoom.setFont(new Font("Arial", Font.BOLD, 14));
        btnBookRoom.setMaximumSize(new Dimension(170, 40));
        btnBookRoom.setBackground(new Color(34, 139, 34)); // Hijau
        btnBookRoom.setForeground(Color.WHITE); // Putih
        btnBookRoom.setFocusPainted(false);
        btnBookRoom.setBorderPainted(false);
        btnBookRoom.setContentAreaFilled(false);
        
        JLabel brandLabel = new JLabel("Perpustakaan UPNVJ");
        brandLabel.setFont(new Font("Arial", Font.BOLD, 18));
        brandLabel.setForeground(new Color(34, 139, 34)); // Hijau gelap
        menuPanel.add(brandLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        menuPanel.add(btnTabDashboard);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnTabRuangan);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnTabRiwayat);
        
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        

        menuPanel.add(btnRefresh);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnBookRoom);
        
        add(menuPanel, BorderLayout.WEST);

        // Action Listeners for Tabs
        btnTabDashboard.addActionListener(e -> cardLayout.show(mainCardPanel, "Dashboard"));
        btnTabRuangan.addActionListener(e -> cardLayout.show(mainCardPanel, "Daftar Ruangan"));
        btnTabRiwayat.addActionListener(e -> cardLayout.show(mainCardPanel, "Riwayat Pemesanan"));

        // Load Data to Table
        loadDataRuangan();

        // Action Listeners
        btnRefresh.addActionListener(e -> loadDataRuangan());
        java.awt.event.ActionListener bookAction = e -> {
            BookingForm form = new BookingForm(this);
            form.setVisible(true);
            // Refresh table after booking is closed
            loadDataRuangan();
        };
        btnBookRoom.addActionListener(bookAction);
        btnBookRoomBottom.addActionListener(bookAction);
    }

    public controller.BookingController getBookingController() {
        return bookingController;
    }

    public void loadDataRuangan() {
        tableModel.setRowCount(0); // Clear table
        List<RuangDiskusi> listRuangan = controller.getAllRuangan();
        for (RuangDiskusi r : listRuangan) {
            Object[] row = {
                r.getIdRuang(),
                r.getNamaRuang(),
                r.getTipeRuang(),
                r.getLantai(),
                r.getJumlahKursi(),
                r.getFasilitas(),
                r.getStatusSaatIni()
            };
            tableModel.addRow(row);
        }
        
        // Memperbarui UI tabel jika datanya berubah agar ukurannya menyesuaikan
        tableRuangan.revalidate();
        tableRuangan.repaint();
        tableRuanganFull.revalidate();
        tableRuanganFull.repaint();

        // Load Riwayat Booking
        modelRiwayat.setRowCount(0);
        java.util.List<model.BookingRiwayat> history = bookingController.getAllBookingHistory();
        for (model.BookingRiwayat h : history) {
            Object[] row = {
                h.getIdRuang() + " - " + h.getNamaRuang(),
                h.getNim() + " - " + h.getNamaMahasiswa(),
                h.getTanggalBooking().toString(),
                h.getJamMulai().toString().substring(0, 5),
                h.getJamSelesai().toString().substring(0, 5),
                h.getStatus(),
                h // Melemparkan objek BookingRiwayat ke kolom Aksi
            };
            modelRiwayat.addRow(row);
        }
        
        if (modelRiwayat.getRowCount() == 0) {
            if (dashboardHistoryLayout != null) dashboardHistoryLayout.show(dashboardHistoryContainer, "EMPTY");
            if (fullHistoryLayout != null) fullHistoryLayout.show(fullHistoryContainer, "EMPTY");
        } else {
            if (dashboardHistoryLayout != null) dashboardHistoryLayout.show(dashboardHistoryContainer, "TABLE");
            if (fullHistoryLayout != null) fullHistoryLayout.show(fullHistoryContainer, "TABLE");
        }
        
        tableRiwayat.revalidate();
        tableRiwayat.repaint();
        tableRiwayatFull.revalidate();
        tableRiwayatFull.repaint();
    }

    public static void main(String[] args) {
        // Menggunakan Look and Feel agar desain lebih modern (mirip sistem operasi)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}
