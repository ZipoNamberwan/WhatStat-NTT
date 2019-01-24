package ntt.bps.namberwan.allstatntt.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatUtils {

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
}
