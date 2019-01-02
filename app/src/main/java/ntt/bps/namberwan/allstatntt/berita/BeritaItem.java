package ntt.bps.namberwan.allstatntt.berita;

/**
 * Created by zipo on 16/04/16.
 */
public class BeritaItem {

    private String id;
    private String jenis;
    private String tanggal;
    private String urlFoto;
    private String judul;
    private String rincian;
    private String urlShare;
    private boolean isBookmarked;
    private boolean isExpanded;

    public BeritaItem(String id, String jenis, String tanggal, String urlFoto, String judul, String rincian,
                      String urlShare, boolean isBookmarked, boolean isExpanded){
        this.id = id;
        this.jenis = jenis;
        this.tanggal = tanggal;
        this.urlFoto = urlFoto;
        this.judul = judul;
        this.rincian = rincian;
        this.urlShare = urlShare;
        this.isBookmarked = isBookmarked;
        this.isExpanded = isExpanded;
    }

    public String getId() {
        return id;
    }

    public String getJenis() {
        return jenis;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public String getJudul() {
        return judul;
    }

    public String getRincian() {
        return rincian;
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

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
}
