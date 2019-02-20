package ntt.bps.namberwan.allstatntt.chat;

public class Kontak {

    private int icon;
    private String judul;
    private String deskripsi;
    private String jenis;

    public int getIcon() {
        return icon;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getJenis() {
        return jenis;
    }

    public Kontak(int icon, String judul, String deskripsi, String jenis) {
        this.icon = icon;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.jenis = jenis;

    }
}
