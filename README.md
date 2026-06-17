# 📚 Sistem Reservasi Ruang Diskusi — Perpustakaan UPNVJ

> Aplikasi desktop berbasis Java untuk mengelola peminjaman ruang diskusi di Perpustakaan Universitas Pembangunan Nasional Veteran Jakarta.

---

## 📖 Deskripsi

Sistem Reservasi Ruang Diskusi Perpustakaan UPNVJ adalah aplikasi manajemen peminjaman ruangan yang dibangun menggunakan **Java Swing** dengan arsitektur **MVC (Model-View-Controller)** dan menerapkan prinsip-prinsip **Object-Oriented Programming (OOP)** secara menyeluruh.

### Penerapan OOP dalam Proyek Ini

| Konsep OOP | Implementasi |
|---|---|
| **Abstraksi** | Class `RuangDiskusi` adalah *abstract class* dengan method abstrak `tampilkanFasilitas()` yang wajib diimplementasikan oleh subclass |
| **Pewarisan (Inheritance)** | Tiga subclass mewarisi `RuangDiskusi`: `RuangDiskusiReguler`, `RuangDiskusiPersonal`, dan `RuangDiskusiBesar` |
| **Enkapsulasi** | Semua atribut di setiap class bersifat `private` dan hanya dapat diakses melalui *getter/setter* |
| **Polimorfisme** | Method `tampilkanFasilitas()` di-*override* oleh setiap subclass sesuai jenis ruangannya |

---

## ✨ Fitur Utama

- 🏠 **Dashboard Utama** — Menampilkan ringkasan daftar ruangan dan riwayat pemesanan dalam satu layar
- 📋 **Daftar Ruangan** — Melihat semua ruang diskusi beserta status ketersediaan real-time (Tersedia / Sedang Digunakan)
- 📝 **Booking Ruangan** — Form pemesanan ruangan dengan validasi:
  - Durasi maksimal **2 jam** per pemesanan
  - Deteksi **bentrok jadwal** otomatis
  - Registrasi mahasiswa otomatis berdasarkan NIM
- 🕐 **Status Real-Time** — Status pemesanan dihitung otomatis berdasarkan waktu nyata: `Akan Datang`, `Sedang Berjalan`, atau `Selesai`
- ✏️ **Edit Riwayat Pemesanan** — Mengubah data booking yang sudah ada dengan validasi yang sama
- 🗑️ **Hapus Riwayat Pemesanan** — Menghapus data booking dengan konfirmasi dialog
- 📂 **Navigasi Tab** — Sidebar navigasi dengan tiga tab: Dashboard, Daftar Ruangan, dan Riwayat Pemesanan
- 📭 **Empty State** — Tampilan informatif "Belum ada yang memesan ruangan" saat data kosong

---

## 🛠️ Tech Stack

| Komponen | Teknologi |
|---|---|
| **Bahasa** | Java (JDK 11+) |
| **GUI Framework** | Java Swing |
| **Arsitektur** | MVC (Model-View-Controller) |
| **Database** | MySQL |
| **Koneksi DB** | JDBC (MySQL Connector/J 9.7.0) |
| **Build** | Manual `javac` / `java` |

### Struktur Paket

```
src/
├── model/
│   ├── RuangDiskusi.java         ← Abstract class (parent)
│   ├── RuangDiskusiReguler.java  ← Subclass: Ruang Reguler
│   ├── RuangDiskusiPersonal.java ← Subclass: Ruang Personal
│   ├── RuangDiskusiBesar.java    ← Subclass: Ruang Besar
│   ├── Booking.java              ← Model data transaksi booking
│   ├── BookingRiwayat.java       ← Model DTO untuk tampilan riwayat
│   └── Mahasiswa.java            ← Model data mahasiswa
├── controller/
│   ├── BookingController.java        ← Logika bisnis: booking, edit, hapus, validasi
│   └── RuangDiskusiController.java   ← Logika bisnis: ambil data ruangan
├── view/
│   ├── MainDashboard.java   ← Jendela utama & navigasi
│   ├── BookingForm.java     ← Form input pemesanan & edit
│   ├── ButtonRenderer.java  ← Renderer tombol Aksi di tabel
│   └── ButtonEditor.java    ← Editor tombol Aksi (klik handler)
└── util/
    └── DatabaseConnection.java  ← Singleton koneksi JDBC ke MySQL
```

---

## ⚙️ Cara Menjalankan

### Prasyarat
- **JDK 11** atau lebih baru sudah terinstall
- **MySQL Server** sudah berjalan
- File `mysql-connector-j-9.7.0.jar` sudah ada di root folder proyek

### 1. Siapkan Database
Import file skema ke MySQL Server kamu:
```sql
-- Jalankan file ini di MySQL Workbench atau terminal MySQL
SOURCE database.sql;
```

### 2. Konfigurasi Koneksi
Buka file `src/util/DatabaseConnection.java` dan sesuaikan parameter koneksi:
```java
String url = "jdbc:mysql://localhost:3306/nama_database_kamu";
String user = "root";
String password = "password_kamu";
```

### 3. Kompilasi Source Code
Jalankan perintah berikut di terminal dari root folder proyek:
```bash
# Windows (PowerShell)
mkdir -Force bin
javac -d bin -cp "mysql-connector-j-9.7.0.jar;src" (Get-ChildItem -Path src -Recurse -Filter *.java).FullName
```

### 4. Jalankan Aplikasi
```bash
# Windows (PowerShell)
java -cp "bin;mysql-connector-j-9.7.0.jar" view.MainDashboard
```

---

## 🎥 Demo Program

> **Link Video Demo:** [Klik di sini untuk melihat demo](https://drive.google.com/your-link-here)

---

## 👥 Tim Pengembang

**Kelompok 3 — Pemrograman Berorientasi Objek**
Universitas Pembangunan Nasional Veteran Jakarta

| No | Nama |
|---|---|
| 1 | Aeni Nurrachmania Hakim |
| 2 | Cicilya Setyani |
| 3 | Rafi Fauzi |
| 4 | Nayla Dwinta Putri Muharram |
| 5 | Regina Juliyanti Manalu |

---

<p align="center">
  Dibuat dengan ❤️ oleh Kelompok 3 &nbsp;|&nbsp; Perpustakaan UPNVJ
</p>
