package ntt.bps.namberwan.allstatntt.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatUtils {

    private static boolean isOnline = true;

    public static List<User> getAdminList(){
        ArrayList<User> adminUsers = new ArrayList<>();
        adminUsers.add(new User("HV8p5WYKBDVewiRiYyJkePhpNbi1", "Test Admin", "", "Yesterday",
                true));
        adminUsers.add(new User("xZMJl3pua3N5BIj96O4PcZ5iPB53", "Test Admin 2", "", "Yesterday",
                true));
        /*adminUsers.add(new User("mzbjj2NkC8Yo17F0BeA7LCP78l33", "Test Admin 3",  "", "Yesterday",
                true));*/

        return adminUsers;
    }

    public static User getAdminById(String id){
        User u = null;
        for (User user : getAdminList()){
            if (id.equals(user.getId())){
                u = user;
                break;
            }
        }
        return u;
    }

    public static Map<String, Object> updateUserInformation(String key, String username, String urlPhoto, long lastSeen, boolean isOnline, boolean isTyping) {

        Map<String, Object> update = new HashMap<>();
        update.put("username", username);
        if (urlPhoto != null){
            update.put("urlPhoto", urlPhoto);
        } else {
            update.put("urlPhoto", "");
        }

        update.put("lastSeen", lastSeen);
        if (isOnline){
            update.put("isOnline", true);
        } else {
            update.put("isOnline", false);
        }

        update.put("isTyping", isTyping);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + key + "/information/", update);

        return childUpdates;
    }

    public static boolean isIsOnline() {
        return isOnline;
    }

    public static void setIsOnline(boolean isOnline) {
        ChatUtils.isOnline = isOnline;
    }
}
