package ntt.bps.namberwan.allstatntt.tabelstatis;

/**
 * Created by zipo on 16/04/16.
 */
public class TabelItem {

    private String id;
    private String subjek;
    private String tanggal;
    private String judul;
    private String excel;
    private String urlShare;
    private String html;
    private int kategori;
    private boolean isBookmarked;
    private boolean isSection;

    public TabelItem(String id, String subjek, String tanggal, String judul, String excel, String urlShare,
                     String html, int kategori, boolean isBookmarked, boolean isSection){
        this.id = id;
        this.subjek = subjek;
        this.tanggal = tanggal;
        this.judul = judul;
        this.isBookmarked = isBookmarked;
        this.kategori = kategori;
        this.excel = excel;
        this.urlShare = urlShare;
        this.isSection = isSection;
        this.html = html;
    }

    public String getId() {
        return id;
    }

    public String getSubjek() {
        return subjek;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJudul() {
        return judul;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public int getKategori() {
        return kategori;
    }

    public String getExcel() {
        return excel;
    }

    public String getUrlShare() {
        return urlShare;
    }

    public boolean isSection() {
        return isSection;
    }

    public String getHtml() {
        return html;
    }
}
