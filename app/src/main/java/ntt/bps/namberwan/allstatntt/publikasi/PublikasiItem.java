package ntt.bps.namberwan.allstatntt.publikasi;

/**
 * Created by zipo on 16/04/16.
 */
public class PublikasiItem {

    private String id;
    private String judul;
    private String tanggal;
    private String isbn;
    private String abstrak;
    private String nomerKatalog;
    private String urlCover;
    private String urlPdf;
    private boolean isBookmarked;
    private boolean isExpanded;

    public PublikasiItem(String id, String judul, String tanggal, String isbn, String abstrak,
                         String nomerKatalog, String urlCover, String urlPdf, boolean isBookmarked, boolean isExpanded){
        this.id = id;
        this.judul = judul;
        this.tanggal = tanggal;
        this.isbn = isbn;
        this.abstrak = abstrak;
        this.nomerKatalog = nomerKatalog;
        this.urlCover = urlCover;
        this.urlPdf = urlPdf;
        this.isBookmarked = isBookmarked;
        this.isExpanded = isExpanded;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAbstrak() {
        return abstrak;
    }

    public String getNomerKatalog() {
        return nomerKatalog;
    }

    public String getUrlCover() {
        return urlCover;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
}
