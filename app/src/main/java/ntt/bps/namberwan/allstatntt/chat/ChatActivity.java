package ntt.bps.namberwan.allstatntt.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ntt.bps.namberwan.allstatntt.R;

public class ChatActivity extends AppCompatActivity implements MessageInput.InputListener, MessageInput.AttachmentsListener {

    public static final String ID_ADMIN_RECEIVER = "id receiver";
    public static final String ID_USER_SENDER = "id sender";

    private MessageInput messageInput;
    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;

    private User sender;
    private User receiver;

    private String idReceiver;
    private String idSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        idReceiver = getIntent().getStringExtra(ID_ADMIN_RECEIVER);
        idSender = getIntent().getStringExtra(ID_USER_SENDER);

        messageInput = findViewById(R.id.input);
        messageInput.setInputListener(this);
        messageInput.setAttachmentsListener(this);

        messagesList = findViewById(R.id.messages_list);

        setUser();
        initAdapter();

    }

    private void setUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String urlAvatar = "";

        if (firebaseUser.getPhotoUrl()!=null){
            urlAvatar = firebaseUser.getPhotoUrl().toString();
        }

        sender = new User(idSender, firebaseUser.getDisplayName(), urlAvatar, "",true);
        receiver = ChatUtils.getAdminById(idReceiver);
        setTitle(receiver.getName());
    }

    private void initAdapter() {

        //initial setup adapter
        adapter = new MessagesListAdapter<>(idSender, null);
        messagesList.setAdapter(adapter);

        DateFormatter.Formatter formatter = new DateFormatter.Formatter() {
            @Override
            public String format(Date date) {
                if (DateFormatter.isToday(date)) {
                    return getString(R.string.date_header_today);
                } else if (DateFormatter.isYesterday(date)) {
                    return getString(R.string.date_header_yesterday);
                } else {
                    return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
                }
            }
        };

        adapter.setDateHeadersFormatter(formatter);

        //Display older Message
        displayMessage();
    }

    private void displayMessage() {

        String string = "December 24, 2018";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Message message = new Message(idReceiver, receiver, "test kaka", date);
        adapter.addToStart(message, true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        sendMessage(input);
        return true;
    }

    private void sendMessage(CharSequence input) {
        Message message = new Message(idSender, sender, input.toString());
        adapter.addToStart(message, true);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onAddAttachments() {

    }
}
