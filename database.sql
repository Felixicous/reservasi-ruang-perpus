-- Tabel Mahasiswa
CREATE TABLE IF NOT EXISTS mahasiswa (
    nim VARCHAR(15) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL
);

-- Tabel Ruangan
CREATE TABLE IF NOT EXISTS ruangan (
    id_ruang VARCHAR(10) PRIMARY KEY,
    nama_ruang VARCHAR(50) NOT NULL,
    tipe_ruang VARCHAR(30) NOT NULL,
    lantai INT NOT NULL,
    jumlah_kursi INT NOT NULL,
    fasilitas TEXT NOT NULL
);

-- Tabel Booking
CREATE TABLE IF NOT EXISTS booking (
    id_booking INT PRIMARY KEY AUTO_INCREMENT,
    nim VARCHAR(15) NOT NULL,
    id_ruang VARCHAR(10) NOT NULL,
    tanggal_booking DATE NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    FOREIGN KEY (nim) REFERENCES mahasiswa(nim),
    FOREIGN KEY (id_ruang) REFERENCES ruangan(id_ruang)
);

-- Seeder Data Awal Ruangan
INSERT INTO ruangan (id_ruang, nama_ruang, tipe_ruang, lantai, jumlah_kursi, fasilitas) VALUES
('R1-01', 'Ruang 1.1', 'Reguler', 1, 4, '4 Kursi, 1 Smart TV, Wi-Fi'),
('R1-02', 'Ruang 1.2', 'Reguler', 1, 4, '4 Kursi, Wi-Fi'),
('R1-03', 'Ruang 1.3', 'Personal', 1, 2, '2 Kursi, Wi-Fi'),
('R3-01', 'Ruang 3.1', 'Besar', 3, 8, '8 Kursi, Wi-Fi'),
('R4-01', 'Ruang 4.1', 'Reguler', 4, 4, '4 Kursi, 1 Papan Tulis, Wi-Fi'),
('R4-02', 'Ruang 4.2', 'Reguler', 4, 4, '4 Kursi, 1 Papan Tulis, Wi-Fi')
ON DUPLICATE KEY UPDATE 
nama_ruang=VALUES(nama_ruang), tipe_ruang=VALUES(tipe_ruang), lantai=VALUES(lantai), jumlah_kursi=VALUES(jumlah_kursi), fasilitas=VALUES(fasilitas);
