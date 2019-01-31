package ntt.bps.namberwan.allstatntt.chat.notifications;

public class Sender {

    public Data notification;

    public Sender(Data notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public String to;
}
