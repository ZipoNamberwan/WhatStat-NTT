package ntt.bps.namberwan.allstatntt.chat;

public class CustomerService {

    private String id;
    private String nama;
    private boolean isOnline;
    private String lastSeen;

    public CustomerService(String id, String nama, boolean isOnline, String lastSeen){
        this.id = id;
        this.nama = nama;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getLastSeen() {
        return lastSeen;
    }
}
