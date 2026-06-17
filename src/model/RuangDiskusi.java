package model;

public abstract class RuangDiskusi {
    private String idRuang;
    private String namaRuang;
    private int jumlahKursi;
    private String tipeRuang;
    private String lantai;
    private String fasilitas;
    private String statusSaatIni;

    public RuangDiskusi() {
    }

    public RuangDiskusi(String idRuang, String namaRuang, int jumlahKursi, String tipeRuang, String lantai, String fasilitas, String statusSaatIni) {
        this.idRuang = idRuang;
        this.namaRuang = namaRuang;
        this.jumlahKursi = jumlahKursi;
        this.tipeRuang = tipeRuang;
        this.lantai = lantai;
        this.fasilitas = fasilitas;
        this.statusSaatIni = statusSaatIni;
    }

    public String getIdRuang() {
        return idRuang;
    }

    public void setIdRuang(String idRuang) {
        this.idRuang = idRuang;
    }

    public String getNamaRuang() {
        return namaRuang;
    }

    public void setNamaRuang(String namaRuang) {
        this.namaRuang = namaRuang;
    }

    public int getJumlahKursi() {
        return jumlahKursi;
    }

    public void setJumlahKursi(int jumlahKursi) {
        this.jumlahKursi = jumlahKursi;
    }

    public String getTipeRuang() {
        return tipeRuang;
    }

    public void setTipeRuang(String tipeRuang) {
        this.tipeRuang = tipeRuang;
    }

    public String getLantai() {
        return lantai;
    }

    public void setLantai(String lantai) {
        this.lantai = lantai;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getStatusSaatIni() {
        return statusSaatIni;
    }

    public void setStatusSaatIni(String statusSaatIni) {
        this.statusSaatIni = statusSaatIni;
    }

    public abstract void tampilkanFasilitas();
}
