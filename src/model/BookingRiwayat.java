package model;

import java.sql.Date;
import java.sql.Time;

public class BookingRiwayat {
    private int idBooking;
    private String idRuang;
    private String namaRuang;
    private String nim;
    private String namaMahasiswa;
    private Date tanggalBooking;
    private Time jamMulai;
    private Time jamSelesai;
    private String status;

    public BookingRiwayat(int idBooking, String idRuang, String namaRuang, String nim, String namaMahasiswa, Date tanggalBooking, Time jamMulai, Time jamSelesai, String status) {
        this.idBooking = idBooking;
        this.idRuang = idRuang;
        this.namaRuang = namaRuang;
        this.nim = nim;
        this.namaMahasiswa = namaMahasiswa;
        this.tanggalBooking = tanggalBooking;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.status = status;
    }

    public int getIdBooking() { return idBooking; }
    public String getIdRuang() { return idRuang; }
    public String getNamaRuang() { return namaRuang; }
    public String getNim() { return nim; }
    public String getNamaMahasiswa() { return namaMahasiswa; }
    public Date getTanggalBooking() { return tanggalBooking; }
    public Time getJamMulai() { return jamMulai; }
    public Time getJamSelesai() { return jamSelesai; }
    public String getStatus() { return status; }
}
