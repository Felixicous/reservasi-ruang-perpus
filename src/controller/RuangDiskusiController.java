package controller;

import model.RuangDiskusi;
import model.RuangDiskusiBesar;
import model.RuangDiskusiPersonal;
import model.RuangDiskusiReguler;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RuangDiskusiController {

    // Mengambil semua data ruangan dari database
    public List<RuangDiskusi> getAllRuangan() {
        List<RuangDiskusi> listRuangan = new ArrayList<>();
        String query = "SELECT r.*, " +
                       "(SELECT COUNT(*) FROM booking b " +
                       " WHERE b.id_ruang = r.id_ruang " +
                       "   AND b.tanggal_booking = CURDATE() " +
                       "   AND CURTIME() BETWEEN b.jam_mulai AND b.jam_selesai) as is_booked " +
                       "FROM ruangan r";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String idRuang = rs.getString("id_ruang");
                String namaRuang = rs.getString("nama_ruang");
                String tipeRuang = rs.getString("tipe_ruang");
                int lantai = rs.getInt("lantai");
                int jumlahKursi = rs.getInt("jumlah_kursi");
                String fasilitas = rs.getString("fasilitas");
                int isBooked = rs.getInt("is_booked");
                String statusSaatIni = isBooked > 0 ? "Sedang Digunakan" : "Tersedia";

                RuangDiskusi ruang;
                // Polimorfisme: Membuat objek berdasarkan tipe ruangan
                if (tipeRuang.equalsIgnoreCase("Besar")) {
                    ruang = new RuangDiskusiBesar(idRuang, namaRuang, jumlahKursi, tipeRuang, String.valueOf(lantai), fasilitas, statusSaatIni);
                } else if (tipeRuang.equalsIgnoreCase("Personal")) {
                    ruang = new RuangDiskusiPersonal(idRuang, namaRuang, jumlahKursi, tipeRuang, String.valueOf(lantai), fasilitas, statusSaatIni);
                } else {
                    ruang = new RuangDiskusiReguler(idRuang, namaRuang, jumlahKursi, tipeRuang, String.valueOf(lantai), fasilitas, statusSaatIni);
                }
                listRuangan.add(ruang);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data ruangan: " + e.getMessage());
        }

        return listRuangan;
    }
}
