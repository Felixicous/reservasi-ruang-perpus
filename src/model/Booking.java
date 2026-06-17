package model;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int idBooking;
    private String nim;
    private String idRuang;
    private Date tanggalBooking;
    private Time jamMulai;
    private Time jamSelesai;

    public Booking(int idBooking, String nim, String idRuang, Date tanggalBooking, Time jamMulai, Time jamSelesai) {
        this.idBooking = idBooking;
        this.nim = nim;
        this.idRuang = idRuang;
        this.tanggalBooking = tanggalBooking;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    // Getters
    public int getIdBooking() { return idBooking; }
    public String getNim() { return nim; }
    public String getIdRuang() { return idRuang; }
    public Date getTanggalBooking() { return tanggalBooking; }
    public Time getJamMulai() { return jamMulai; }
    public Time getJamSelesai() { return jamSelesai; }
}
