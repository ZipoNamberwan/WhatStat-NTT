package ntt.bps.namberwan.allstatntt.indikator;

public class IndikatorItem {

    private String id;
    private String judul;
    private String nama;
    private String sumber;
    private String nilai;
    private String satuan;
    private int position;

    public int getPosition() {
        return position;
    }

    public IndikatorItem(String id, String judul, String nama, String sumber, String nilai, String satuan, int position){
        this.id = id;
        this.judul = judul;
        this.nama = nama;
        this.sumber = sumber;
        this.nilai= nilai;
        this.satuan = satuan;
        this.position = position;

    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getNama() {
        return nama;
    }

    public String getSumber() {
        return sumber;
    }

    public String getNilai() {
        return nilai;
    }

    public String getSatuan() {
        return satuan;
    }
}
