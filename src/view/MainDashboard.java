package view;

import controller.RuangDiskusiController;
import model.RuangDiskusi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
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
    private JButton btnTabRiwayat;
    
    private controller.BookingController bookingController;
    private RuangDiskusiController controller;

    private void styleTable(JTable table) {
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(new Color(0, 0, 0, 51)); // Black dengan opacity 20%
        
        table.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0, 0, 0, 51)));
        
        JTableHeader header = table.getTableHeader();
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.WHITE);
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            private int colIndex = -1;
            private int totalCols = -1;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                this.colIndex = column;
                this.totalCols = table.getColumnCount();
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(0x478E54));
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 14));
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 51)));
                return this;
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(getBackground());
                int r = 8; // Diameter (radius 4px * 2)
                
                if (colIndex == 0 && totalCols == 1) {
                     g2.fillRoundRect(0, 0, getWidth(), getHeight() + r, r, r);
                } else if (colIndex == 0) {
                     g2.fillRoundRect(0, 0, getWidth() + r, getHeight() + r, r, r);
                } else if (colIndex == totalCols - 1) {
                     g2.fillRoundRect(-r, 0, getWidth() + r, getHeight() + r, r, r);
                } else {
                     g2.fillRect(0, 0, getWidth(), getHeight());
                }
                
                g2.dispose();
                
                setOpaque(false);
                super.paintComponent(g);
                setOpaque(true);
            }
        };
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD));
                
                if (!isSelected && value != null) {
                    String status = value.toString();
                    if (status.equalsIgnoreCase("Tersedia")) {
                        setForeground(new Color(0x008000));
                    } else if (status.equalsIgnoreCase("Selesai")) {
                        setForeground(new Color(0x007BFF));
                    } else {
                        setForeground(new Color(0xF1C40F));
                    }
                }
                
                return c;
            }
        };

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            if (table.getColumnModel().getColumn(i).getCellRenderer() == null) {
                if (table.getColumnName(i).equalsIgnoreCase("Status")) {
                    table.getColumnModel().getColumn(i).setCellRenderer(statusRenderer);
                } else {
                    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }
            }
        }
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(new Color(0x1E1E1E));
        btn.setBackground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public MainDashboard() {
        controller = new RuangDiskusiController();
        bookingController = new controller.BookingController();

        setTitle("Sistem Reservasi Ruang Diskusi Perpustakaan UPN");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        // Main Layout
        setLayout(new BorderLayout());
        
        // Header dihapus sesuai permintaan
        

        // Main Card Layout Area
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.setBackground(Color.WHITE);
        
        // ==========================================
        // CARD 0: HOME (Tampilan Baru)
        // ==========================================
        JPanel homeCard = new JPanel(new GridBagLayout());
        homeCard.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Logo
        try {
            ImageIcon originalIcon = new ImageIcon("src/assets/logo.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            JLabel logoIconLabel = new JLabel(new ImageIcon(scaledImage));
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            homeCard.add(logoIconLabel, gbc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Title
        JLabel titlePerpus = new JLabel("Perpustakaan", SwingConstants.CENTER);
        titlePerpus.setFont(new Font("Arial", Font.BOLD, 48));
        titlePerpus.setForeground(new Color(0x478E54)); // Hijau
        
        JLabel titleUPN = new JLabel("UPN \"Veteran\" Jakarta", SwingConstants.CENTER);
        titleUPN.setFont(new Font("Arial", Font.BOLD, 36));
        titleUPN.setForeground(new Color(0xE3AD26)); // Oranye/Emas
        
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        homeCard.add(titlePerpus, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 0, 10);
        homeCard.add(titleUPN, gbc);
        
        // Tombol Pinjam Ruang Diskusi
        JButton btnHomePinjam = new JButton("Pinjam Ruang Diskusi") {
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
        btnHomePinjam.setFont(new Font("Arial", Font.BOLD, 18));
        btnHomePinjam.setPreferredSize(new Dimension(300, 60));
        btnHomePinjam.setBackground(new Color(0x478E54));
        btnHomePinjam.setForeground(Color.WHITE);
        btnHomePinjam.setFocusPainted(false);
        btnHomePinjam.setBorderPainted(false);
        btnHomePinjam.setContentAreaFilled(false);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(24, 10, 10, 10);
        homeCard.add(btnHomePinjam, gbc);
        
        // Subtitle
        JLabel subtitleHome = new JLabel("<html><div style='text-align: center;'>Pesan ruang diskusi dengan mudah untuk keperluan<br>akademik.</div></html>", SwingConstants.CENTER);
        subtitleHome.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleHome.setForeground(new Color(0x1E1E1E));
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 40, 10);
        homeCard.add(subtitleHome, gbc);
        
        JPanel homeWrapper = new JPanel(new BorderLayout());
        homeWrapper.setBackground(Color.WHITE);
        homeWrapper.add(homeCard, BorderLayout.CENTER);
        
        mainCardPanel.add(homeWrapper, "Home");

        // ==========================================
        // CARD 1: DASHBOARD (Tampilan Saat Ini)
        // ==========================================
        JPanel dashboardCard = new JPanel(new BorderLayout());
        dashboardCard.setBackground(Color.WHITE);
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
        styleTable(tableRuangan);
        
        JScrollPane scrollPane = new JScrollPane(tableRuangan);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JLabel labelDaftarRuangan = new JLabel("Daftar Ruangan");
        labelDaftarRuangan.setFont(new Font("Arial", Font.BOLD, 18));
        labelDaftarRuangan.setForeground(new Color(0xE3AD26));
        labelDaftarRuangan.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JButton btnBookRoomBottom = new JButton("Pinjam Ruang Diskusi") {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnBookRoomBottom.setFont(new Font("Arial", Font.BOLD, 14));
        btnBookRoomBottom.setPreferredSize(new Dimension(0, 48));
        btnBookRoomBottom.setBackground(new Color(0x478E54)); // Hijau
        btnBookRoomBottom.setForeground(Color.WHITE); // Putih
        btnBookRoomBottom.setFocusPainted(false);
        btnBookRoomBottom.setBorderPainted(false);
        btnBookRoomBottom.setContentAreaFilled(false);
        
        JPanel bottomBtnContainer = new JPanel(new BorderLayout());
        bottomBtnContainer.setBackground(Color.WHITE);
        bottomBtnContainer.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0)); // Jarak 24px dari tabel
        bottomBtnContainer.add(btnBookRoomBottom, BorderLayout.CENTER);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.add(labelDaftarRuangan, BorderLayout.NORTH);
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        tableWrapper.add(bottomBtnContainer, BorderLayout.SOUTH);
        
        dashboardCard.add(tableWrapper, BorderLayout.NORTH);
        
        // SETUP TABEL RIWAYAT DI DASHBOARD
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel labelRiwayat = new JLabel("Riwayat Pinjaman");
        labelRiwayat.setFont(new Font("Arial", Font.BOLD, 18));
        labelRiwayat.setForeground(new Color(0xE3AD26));
        labelRiwayat.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        historyPanel.add(labelRiwayat, BorderLayout.NORTH);
        
        String[] columnsRiwayat = {"Ruangan", "Peminjam", "Tanggal", "Jam Mulai", "Jam Selesai", "Status", "Aksi"};
        modelRiwayat = new DefaultTableModel(columnsRiwayat, 0);
        tableRiwayat = new JTable(modelRiwayat);
        
        styleTable(tableRiwayat);
        
        tableRiwayat.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tableRiwayat.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(this));
        tableRiwayat.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        JScrollPane scrollRiwayat = new JScrollPane(tableRiwayat);
        scrollRiwayat.setBorder(BorderFactory.createEmptyBorder());
        scrollRiwayat.setBackground(Color.WHITE);
        scrollRiwayat.getViewport().setBackground(Color.WHITE);
        
        JPanel emptyRiwayatDashboard = new JPanel(new GridBagLayout());
        emptyRiwayatDashboard.setBackground(Color.WHITE);
        try {
            ImageIcon emptyIcon = new ImageIcon("src/assets/empty_history.png");
            Image scaledEmpty = emptyIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaledEmpty));
            GridBagConstraints gbcE = new GridBagConstraints();
            gbcE.gridx = 0;
            gbcE.gridy = 0;
            gbcE.insets = new Insets(10, 10, 10, 10);
            emptyRiwayatDashboard.add(imgLabel, gbcE);
            
            JLabel textEmpty = new JLabel("Belum Ada Riwayat Peminjaman", SwingConstants.CENTER);
            textEmpty.setFont(new Font("Arial", Font.BOLD, 16));
            textEmpty.setForeground(new Color(0x478E54));
            
            gbcE.gridy = 1;
            emptyRiwayatDashboard.add(textEmpty, gbcE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        dashboardHistoryLayout = new CardLayout();
        dashboardHistoryContainer = new JPanel(dashboardHistoryLayout);
        dashboardHistoryContainer.setBackground(Color.WHITE);
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
        ruanganCard.setBackground(Color.WHITE);
        ruanganCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel labelRuanganFull = new JLabel("Daftar Ruangan");
        labelRuanganFull.setFont(new Font("Arial", Font.BOLD, 22));
        labelRuanganFull.setForeground(new Color(0xE3AD26));
        labelRuanganFull.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        ruanganCard.add(labelRuanganFull, BorderLayout.NORTH);
        
        tableRuanganFull = new JTable(tableModel);
        styleTable(tableRuanganFull);
        
        JScrollPane scrollRuanganFull = new JScrollPane(tableRuanganFull);
        scrollRuanganFull.setBorder(BorderFactory.createEmptyBorder());
        scrollRuanganFull.setBackground(Color.WHITE);
        scrollRuanganFull.getViewport().setBackground(Color.WHITE);
        ruanganCard.add(scrollRuanganFull, BorderLayout.CENTER);
        
        mainCardPanel.add(ruanganCard, "Daftar Ruangan");

        // ==========================================
        // CARD 3: RIWAYAT PEMESANAN (Full)
        // ==========================================
        JPanel riwayatCardFull = new JPanel(new BorderLayout());
        riwayatCardFull.setBackground(Color.WHITE);
        riwayatCardFull.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel labelRiwayatFull = new JLabel("Riwayat Pinjaman");
        labelRiwayatFull.setFont(new Font("Arial", Font.BOLD, 22));
        labelRiwayatFull.setForeground(new Color(0xE3AD26));
        labelRiwayatFull.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        riwayatCardFull.add(labelRiwayatFull, BorderLayout.NORTH);
        
        tableRiwayatFull = new JTable(modelRiwayat);
        
        styleTable(tableRiwayatFull);
        
        tableRiwayatFull.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tableRiwayatFull.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(this));
        tableRiwayatFull.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        JScrollPane scrollRiwayatFull = new JScrollPane(tableRiwayatFull);
        scrollRiwayatFull.setBorder(BorderFactory.createEmptyBorder());
        scrollRiwayatFull.setBackground(Color.WHITE);
        scrollRiwayatFull.getViewport().setBackground(Color.WHITE);
        
        JLabel emptyRiwayatFull = new JLabel("Belum ada yang memesan ruangan", SwingConstants.CENTER);
        emptyRiwayatFull.setFont(new Font("Arial", Font.BOLD, 18));
        emptyRiwayatFull.setForeground(new Color(0x1E1E1E));
        
        fullHistoryLayout = new CardLayout();
        fullHistoryContainer = new JPanel(fullHistoryLayout);
        fullHistoryContainer.setBackground(Color.WHITE);
        fullHistoryContainer.add(scrollRiwayatFull, "TABLE");
        fullHistoryContainer.add(emptyRiwayatFull, "EMPTY");

        riwayatCardFull.add(fullHistoryContainer, BorderLayout.CENTER);
        
        mainCardPanel.add(riwayatCardFull, "Riwayat Pemesanan");

        // Tambahkan CardLayout container ke tengah JFrame
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(mainCardPanel, BorderLayout.CENTER);
        
        JLabel globalFooter = new JLabel("<html><center>\u00A9 2026 Perpustakaan UPN \"Veteran\" Jakarta. All rights<br>reserved.</center></html>", SwingConstants.CENTER);
        globalFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        globalFooter.setForeground(Color.GRAY);
        globalFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        contentPanel.add(globalFooter, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // ==========================================
        // Navigation Buttons (Left Menu Sidebar)
        // ==========================================
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        
        // Brand Logo (Image + Text)
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.X_AXIS));
        brandPanel.setBackground(Color.WHITE);
        brandPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        try {
            ImageIcon originalIcon = new ImageIcon("src/assets/logo.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            JLabel logoIconLabel = new JLabel(new ImageIcon(scaledImage));
            brandPanel.add(logoIconLabel);
            brandPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel brandLabel1 = new JLabel("Perpustakaan");
        brandLabel1.setFont(new Font("Arial", Font.BOLD, 18));
        brandLabel1.setForeground(new Color(0x478E54)); // Hijau gelap
        brandLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel brandLabel2 = new JLabel("UPN \"Veteran\" Jakarta");
        brandLabel2.setFont(new Font("Arial", Font.BOLD, 11));
        brandLabel2.setForeground(new Color(0xE3AD26)); // Emas
        brandLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(brandLabel1);
        textPanel.add(brandLabel2);
        
        brandPanel.add(textPanel);
        
        menuPanel.add(brandPanel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Navigation Tabs
        JButton btnTabHome = createNavButton("Home");
        JButton btnTabDashboard = createNavButton("Dashboard");
        JButton btnTabRuangan = createNavButton("Daftar Ruangan");
        btnTabRiwayat = createNavButton("Riwayat Pemesanan");
        
        JButton[] navButtons = {btnTabHome, btnTabDashboard, btnTabRuangan, btnTabRiwayat};
        
        menuPanel.add(btnTabHome);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(btnTabDashboard);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(btnTabRuangan);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(btnTabRiwayat);
        
        // Push everything up
        menuPanel.add(Box.createVerticalGlue());
        
        // Add right border to menuPanel
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(30, 20, 20, 20)
        ));
        
        add(menuPanel, BorderLayout.WEST);

        // Action Listeners
        java.awt.event.ActionListener bookAction = e -> {
            BookingForm form = new BookingForm(this);
            form.setVisible(true);
            // Refresh table after booking is closed
            loadDataRuangan();
        };
        btnHomePinjam.addActionListener(bookAction);
        btnBookRoomBottom.addActionListener(bookAction);

        // Action Listeners for Tabs
        java.awt.event.ActionListener tabListener = e -> {
            JButton source = (JButton) e.getSource();
            // Reset all buttons to default style
            for (JButton btn : navButtons) {
                btn.setForeground(new Color(0x1E1E1E));
                btn.setFont(new Font("Arial", Font.BOLD, 16));
            }
            // Set active style
            source.setForeground(new Color(0x478E54));
            source.setFont(new Font("Arial", Font.BOLD, 16));
            
            // Switch card
            cardLayout.show(mainCardPanel, source.getText());
        };
        
        btnTabHome.addActionListener(tabListener);
        btnTabDashboard.addActionListener(tabListener);
        btnTabRuangan.addActionListener(tabListener);
        btnTabRiwayat.addActionListener(tabListener);
        
        // Set Default Active Tab
        btnTabHome.setForeground(new Color(0x478E54));
        btnTabHome.setFont(new Font("Arial", Font.BOLD, 16));
        cardLayout.show(mainCardPanel, "Home");

        // Load Data to Table
        loadDataRuangan();
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
    
    public void switchToRiwayatTab() {
        if (btnTabRiwayat != null) {
            btnTabRiwayat.doClick();
        }
    }
}
