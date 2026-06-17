package model;

public class RuangDiskusiBesar extends RuangDiskusi {

    public RuangDiskusiBesar(String idRuang, String namaRuang, int jumlahKursi, String tipeRuang, String lantai, String fasilitas, String statusSaatIni) {
        super(idRuang, namaRuang, jumlahKursi, tipeRuang, lantai, fasilitas, statusSaatIni);
    }

    @Override
    public void tampilkanFasilitas() {
        System.out.println("Fasilitas Ruang Besar: " + getFasilitas());
    }
}
