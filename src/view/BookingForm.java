package view;

import controller.BookingController;
import controller.RuangDiskusiController;
import model.RuangDiskusi;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class BookingForm extends JDialog {
    private JTextField txtNIM;
    private JTextField txtNama;
    private JTextField txtTanggal;
    private JComboBox<String> cbLantai;
    private JComboBox<String> cbRuang;
    private JSpinner spinnerJamMulai;
    private JSpinner spinnerJamSelesai;
    private BookingController bookingController;
    private RuangDiskusiController ruangController;
    private List<RuangDiskusi> allRuangan;
    private boolean isEditMode = false;
    private int editIdBooking = -1;

    public BookingForm(JFrame parent) {
        super(parent, "Form Pemesanan Ruangan", true);
        initController();
        initUI();
    }

    public BookingForm(JFrame parent, model.BookingRiwayat bookingToEdit) {
        super(parent, "Edit Pemesanan Ruangan", true);
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
        setSize(450, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Komponen Form
        formPanel.add(new JLabel("NIM:"));
        txtNIM = new JTextField();
        formPanel.add(txtNIM);

        formPanel.add(new JLabel("Nama Mahasiswa:"));
        txtNama = new JTextField();
        formPanel.add(txtNama);

        formPanel.add(new JLabel("Pilih Lantai:"));
        cbLantai = new JComboBox<>();
        loadLantaiToComboBox();
        formPanel.add(cbLantai);

        formPanel.add(new JLabel("Pilih Ruangan:"));
        cbRuang = new JComboBox<>();
        formPanel.add(cbRuang);
        
        // Trigger untuk filter ruangan saat lantai dipilih
        cbLantai.addActionListener(e -> updateRoomDropdown());
        // Panggil sekali untuk inisialisasi awal
        updateRoomDropdown();

        formPanel.add(new JLabel("Tanggal Pemesanan (YYYY-MM-DD):"));
        txtTanggal = new JTextField(LocalDate.now().toString());
        formPanel.add(txtTanggal);

        formPanel.add(new JLabel("Jam Mulai (HH:mm):"));
        spinnerJamMulai = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(spinnerJamMulai, "HH:mm");
        spinnerJamMulai.setEditor(timeEditor1);
        formPanel.add(spinnerJamMulai);

        formPanel.add(new JLabel("Jam Selesai (Otomatis 2 Jam):"));
        spinnerJamSelesai = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(spinnerJamSelesai, "HH:mm");
        spinnerJamSelesai.setEditor(timeEditor2);
        spinnerJamSelesai.setEnabled(false); // Kunci agar patokan selalu 2 jam
        formPanel.add(spinnerJamSelesai);

        // Listener: Jika Jam Mulai diganti, Jam Selesai ikut berubah (+ 2 Jam)
        spinnerJamMulai.addChangeListener(e -> updateJamSelesai());
        // Set awal saat form dibuka
        updateJamSelesai();

        add(formPanel, BorderLayout.CENTER);

        // Tombol Submit
        JButton btnSubmit = new JButton(isEditMode ? "Simpan Perubahan" : "Konfirmasi Pemesanan");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.addActionListener(e -> prosesPemesanan(txtTanggal.getText()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        buttonPanel.add(btnSubmit);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadLantaiToComboBox() {
        List<String> lantaiUnik = new ArrayList<>();
        for (RuangDiskusi r : allRuangan) {
            String lantai = r.getLantai();
            if (!lantaiUnik.contains(lantai)) {
                lantaiUnik.add(lantai);
                cbLantai.addItem("Lantai " + lantai);
            }
        }
    }

    private void updateRoomDropdown() {
        cbRuang.removeAllItems();
        if (cbLantai.getSelectedItem() == null) return;
        
        String selectedLantai = cbLantai.getSelectedItem().toString().replace("Lantai ", "");
        for (RuangDiskusi r : allRuangan) {
            if (r.getLantai().equals(selectedLantai)) {
                cbRuang.addItem(r.getIdRuang() + " - " + r.getNamaRuang());
            }
        }
    }

    private void prefillData(model.BookingRiwayat b) {
        txtNIM.setText(b.getNim());
        txtNama.setText(b.getNamaMahasiswa());
        txtTanggal.setText(b.getTanggalBooking().toString());
        
        // Cari dan set combobox Lantai dan Ruang
        for (RuangDiskusi r : allRuangan) {
            if (r.getIdRuang().equals(b.getIdRuang())) {
                cbLantai.setSelectedItem("Lantai " + r.getLantai());
                updateRoomDropdown();
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
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, hasil, isEditMode ? "Edit Gagal" : "Pemesanan Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}
