package ntt.bps.namberwan.allstatntt.brs;

/**
 * Created by zipo on 16/04/16.
 */
public class BrsItem {

    private String id;
    private String judul;
    private String abstrak;
    private String tanggalRilis;
    private String urlPdf;
    private String subjek;
    private String urlShare;
    private int kategori;
    private boolean isSection;
    private boolean isBookmarked;

    public BrsItem(String id, String judul, String abstrak, String tanggalRilis, String urlPdf, String subjek,
                   String urlShare, int kategori, boolean isBookmarked, boolean isSection){
        this.id = id;
        this.judul = judul;
        this.abstrak = abstrak;
        this.tanggalRilis = tanggalRilis;
        this.urlPdf = urlPdf;
        this.subjek = subjek;
        this.kategori = kategori;
        this.isSection = isSection;
        this.isBookmarked = isBookmarked;
        this.urlShare = urlShare;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getAbstrak() {
        return abstrak;
    }

    public String getTanggalRilis() {
        return tanggalRilis;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public String getSubjek() {
        return subjek;
    }

    public int getKategori() {
        return kategori;
    }

    public boolean isSection() {
        return isSection;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public String getUrlShare() {
        return urlShare;
    }
}
