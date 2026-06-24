package controller;

import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingController {

    // Registrasi Mahasiswa otomatis jika belum ada di database
    public void pastikanMahasiswaTerdaftar(String nim, String nama) {
        String checkQuery = "SELECT nim FROM mahasiswa WHERE nim = ?";
        String insertQuery = "INSERT INTO mahasiswa (nim, nama) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, nim);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                // Mahasiswa belum terdaftar, daftarkan otomatis
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, nim);
                    insertStmt.setString(2, nama);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mendaftarkan mahasiswa: " + e.getMessage());
        }
    }

    // Validasi dan proses pemesanan
    public String prosesBooking(String nim, String nama, String idRuang, Date tanggal, Time jamMulai, Time jamSelesai) {
        // Validasi Aturan Bisnis 1: Maksimal 2 Jam
        LocalTime mulai = jamMulai.toLocalTime();
        LocalTime selesai = jamSelesai.toLocalTime();
        long durasiMenit = ChronoUnit.MINUTES.between(mulai, selesai);
        
        if (durasiMenit <= 0) {
            return "Waktu selesai harus lebih besar dari waktu mulai!";
        }
        if (durasiMenit > 120) {
            return "Maksimal durasi peminjaman adalah 2 jam!";
        }

        // Validasi Aturan Bisnis 2: Bentrok Jadwal
        String checkBentrokQuery = "SELECT id_booking FROM booking WHERE id_ruang = ? AND tanggal_booking = ? " +
                                   "AND ((jam_mulai < ? AND jam_selesai > ?) OR (jam_mulai < ? AND jam_selesai > ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkBentrokQuery)) {
            stmt.setString(1, idRuang);
            stmt.setDate(2, tanggal);
            stmt.setTime(3, jamSelesai);
            stmt.setTime(4, jamMulai);
            stmt.setTime(5, jamSelesai);
            stmt.setTime(6, jamMulai);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "Maaf, ruangan sudah dibooking pada jam tersebut.";
            }

            // Lolos validasi, proses penyimpanan
            pastikanMahasiswaTerdaftar(nim, nama);

            String insertBookingQuery = "INSERT INTO booking (nim, id_ruang, tanggal_booking, jam_mulai, jam_selesai) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertBookingQuery)) {
                insertStmt.setString(1, nim);
                insertStmt.setString(2, idRuang);
                insertStmt.setDate(3, tanggal);
                insertStmt.setTime(4, jamMulai);
                insertStmt.setTime(5, jamSelesai);
                insertStmt.executeUpdate();
                return "SUCCESS";
            }

        } catch (SQLException e) {
            return "Error database: " + e.getMessage();
        }
    }
    public void deleteOldBookings() {
        String query = "DELETE FROM booking WHERE tanggal_booking < CURRENT_DATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public java.util.List<model.BookingRiwayat> getAllBookingHistory() {
        deleteOldBookings();
        java.util.List<model.BookingRiwayat> listRiwayat = new java.util.ArrayList<>();
        String query = "SELECT b.id_booking, b.tanggal_booking, b.jam_mulai, b.jam_selesai, " +
                       "r.id_ruang, r.nama_ruang, m.nim, m.nama as nama_mahasiswa " +
                       "FROM booking b " +
                       "JOIN ruangan r ON b.id_ruang = r.id_ruang " +
                       "JOIN mahasiswa m ON b.nim = m.nim " +
                       "ORDER BY b.tanggal_booking DESC, b.jam_mulai DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idBooking = rs.getInt("id_booking");
                Date tanggalBooking = rs.getDate("tanggal_booking");
                Time jamMulai = rs.getTime("jam_mulai");
                Time jamSelesai = rs.getTime("jam_selesai");
                String idRuang = rs.getString("id_ruang");
                String namaRuang = rs.getString("nama_ruang");
                String nim = rs.getString("nim");
                String namaMahasiswa = rs.getString("nama_mahasiswa");

                // Menentukan Status
                String status = "Dibooking";
                LocalDate today = LocalDate.now();
                LocalDate bookingDate = tanggalBooking.toLocalDate();

                if (bookingDate.isBefore(today)) {
                    status = "Selesai";
                } else if (bookingDate.isAfter(today)) {
                    status = "Dibooking";
                } else {
                    // Tanggal hari ini
                    LocalTime timeNow = LocalTime.now();
                    LocalTime timeMulai = jamMulai.toLocalTime();
                    LocalTime timeSelesai = jamSelesai.toLocalTime();

                    if (timeNow.isAfter(timeSelesai)) {
                        status = "Selesai";
                    } else if (timeNow.isBefore(timeMulai)) {
                        status = "Dibooking";
                    } else {
                        status = "Sedang Digunakan";
                    }
                }

                model.BookingRiwayat riwayat = new model.BookingRiwayat(idBooking, idRuang, namaRuang, nim, namaMahasiswa, tanggalBooking, jamMulai, jamSelesai, status);
                listRiwayat.add(riwayat);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching booking history: " + e.getMessage());
        }

        return listRiwayat;
    }

    public boolean deleteBooking(int idBooking) {
        String query = "DELETE FROM booking WHERE id_booking = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idBooking);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            return false;
        }
    }

    public String updateBooking(int idBooking, String nim, String nama, String idRuang, Date tanggal, Time jamMulai, Time jamSelesai) {
        LocalTime mulai = jamMulai.toLocalTime();
        LocalTime selesai = jamSelesai.toLocalTime();
        long durasiMenit = ChronoUnit.MINUTES.between(mulai, selesai);
        
        if (durasiMenit <= 0) {
            return "Waktu selesai harus lebih besar dari waktu mulai!";
        }
        if (durasiMenit > 120) {
            return "Maksimal durasi peminjaman adalah 2 jam!";
        }

        String checkBentrokQuery = "SELECT id_booking FROM booking WHERE id_ruang = ? AND tanggal_booking = ? " +
                                   "AND id_booking != ? " +
                                   "AND ((jam_mulai < ? AND jam_selesai > ?) OR (jam_mulai < ? AND jam_selesai > ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkBentrokQuery)) {
            stmt.setString(1, idRuang);
            stmt.setDate(2, tanggal);
            stmt.setInt(3, idBooking);
            stmt.setTime(4, jamSelesai);
            stmt.setTime(5, jamMulai);
            stmt.setTime(6, jamSelesai);
            stmt.setTime(7, jamMulai);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "Maaf, ruangan sudah dibooking pada jam tersebut.";
            }

            pastikanMahasiswaTerdaftar(nim, nama);

            String updateQuery = "UPDATE booking SET nim = ?, id_ruang = ?, tanggal_booking = ?, jam_mulai = ?, jam_selesai = ? WHERE id_booking = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, nim);
                updateStmt.setString(2, idRuang);
                updateStmt.setDate(3, tanggal);
                updateStmt.setTime(4, jamMulai);
                updateStmt.setTime(5, jamSelesai);
                updateStmt.setInt(6, idBooking);
                updateStmt.executeUpdate();
                return "SUCCESS";
            }

        } catch (SQLException e) {
            return "Error database: " + e.getMessage();
        }
    }
}
