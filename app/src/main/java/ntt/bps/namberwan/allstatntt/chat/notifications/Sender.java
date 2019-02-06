package ntt.bps.namberwan.allstatntt.chat.notifications;

public class Sender {

    public Data data;
    public Data notification;
    public String to;
    public String priority;

    public Sender(Data notification, Data data, String to, String priority) {
        this.notification = notification;
        this.data = data;
        this.to = to;
        this.priority = priority;
    }

}
