package ntt.bps.namberwan.allstatntt.chat;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message implements IMessage {

    private String idSender;
    private String message;
    private Date createdAt;
    private User user;
    private String idReceiver;

    public Message(){

    }

    public Message(String id, User user, String text) {
        this(id, user, text, new Date());
        this.idReceiver = user.getId();
    }

    public Message(String idSender, User user, String message, Date createdAt) {
        this.idSender = idSender;
        this.message = message;
        this.user = user;
        this.createdAt = createdAt;
        this.idReceiver = user.getId();
    }

    @Override
    public String getId() {
        return this.idSender;
    }

    @Override
    public String getText() {
        return this.message;
    }

    @Override
    public IUser getUser() {
        return this.user;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setSender(String idSender){
        this.idSender = idSender;
    }

    public void setReceiver(String idReceiver){
        this.idReceiver = idReceiver;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getSender(){
        return this.idSender;
    }
    public String getReceiver(){
        return this.idReceiver;
    }
    public String getMessage(){
        return this.message;
    }
}
