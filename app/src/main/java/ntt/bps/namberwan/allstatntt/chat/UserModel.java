package ntt.bps.namberwan.allstatntt.chat;

public class UserModel {

    private String id;
    private String username;
    private String urlPhoto;
    private long lastSeen;
    private boolean isOnline;

    public UserModel(){

    }

    public UserModel(String id, String username, String urlPhoto, long lastSeen, boolean isOnline){
        this.setId(id);
        this.setUsername(username);
        this.setUrlPhoto(urlPhoto);
        this.setLastSeen(lastSeen);
        this.setOnline(isOnline);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }



/*    public Map<String, Object> toMap() {

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("isOnline", isOnline);
        result.put("lastSeen", lastSeen);
        result.put("urlPhoto", urlPhoto);
        result.put("username", username);

        return result;
    }*/
}
