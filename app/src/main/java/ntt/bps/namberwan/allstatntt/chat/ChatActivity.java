package ntt.bps.namberwan.allstatntt.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ntt.bps.namberwan.allstatntt.R;

public class ChatActivity extends AppCompatActivity implements MessageInput.InputListener, MessageInput.TypingListener {

    public static final String ID_ADMIN_RECEIVER = "id receiver";
    public static final String ID_USER_SENDER = "id sender";

    private MessageInput messageInput;
    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;

    private User sender;
    private User receiver;

    private String idReceiver;
    private String idSender;
    private String idChat;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    private UserModel userModel;

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

        setupFirebase();

        createUserModel();

        updateUserInformation(userModel.getId(), userModel.getUsername(),
                userModel.getUrlPhoto(), userModel.getLastSeen(), true, false);

        setupChatRoom();

        setupReceiverInformation();

        initView();

    }

    private void createUserModel() {
        String idUser = firebaseAuth.getCurrentUser().getUid();
        String username = "";
        if (firebaseAuth.getCurrentUser().getDisplayName() != null){
            username = firebaseAuth.getCurrentUser().getDisplayName();
        }
        String urlPhoto = "";
        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null){
            urlPhoto = firebaseAuth.getCurrentUser().getPhotoUrl().getPath();
        }
        long lastSeen = System.currentTimeMillis();

        userModel = new UserModel(idUser, username, urlPhoto, lastSeen);
    }

    private void updateUserInformation(String idUser, String username, String urlPhoto, long lastSeen, boolean isOnline, boolean isTyping) {

        String key = reference.child(idUser).getKey();

        Map<String, Object> childUpdates = ChatUtils.updateUserInformation(key, username, urlPhoto, lastSeen, isOnline, isTyping);

        reference.updateChildren(childUpdates);
    }

    private void setupReceiverInformation() {

        setTitle(receiver.getName());

        if (getSupportActionBar()!=null){

            DatabaseReference reference = firebaseDatabase.getReference("Users").child(receiver.getId());
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserModel temp = dataSnapshot.getValue(UserModel.class);
                    String status = ChatUtils.getStatusString(ChatActivity.this, temp);
                    if (getSupportActionBar()!=null){
                        getSupportActionBar().setSubtitle(status);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserModel temp = dataSnapshot.getValue(UserModel.class);
                    String status = ChatUtils.getStatusString(ChatActivity.this, temp);
                    if (getSupportActionBar()!=null){
                        getSupportActionBar().setSubtitle(status);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setupFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setupChatRoom() {

        idReceiver = getIntent().getStringExtra(ID_ADMIN_RECEIVER);
        idSender = getIntent().getStringExtra(ID_USER_SENDER);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String urlAvatar = "";

        if (firebaseUser.getPhotoUrl()!=null){
            urlAvatar = firebaseUser.getPhotoUrl().toString();
        }

        sender = new User(idSender, firebaseUser.getDisplayName(), urlAvatar, "",true);

        UserModel receiverUM = ChatUtils.getAdminById(idReceiver);

        receiver = new User(receiverUM.getId(), receiverUM.getUsername(), receiverUM.getUrlPhoto(), "", receiverUM.getIsOnline());

        idChat = createIdChat(idSender, idReceiver);

    }

    private String createIdChat(String idSender, String idReceiver) {
        ArrayList<String> list = new ArrayList<>();
        list.add(idSender);
        list.add(idReceiver);
        Collections.sort(list);
        return list.get(0) + list.get(1);
    }

    private void initView() {

        messageInput = findViewById(R.id.input);
        messageInput.setInputListener(this);
        messageInput.setTypingListener(this);

        messagesList = findViewById(R.id.messages_list);

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

        displayMessage(idSender, idReceiver);
    }

    private void displayMessage(final String idSender, final String idReceiver) {

        final List<Message> messages = new ArrayList<>();
        DatabaseReference reference = firebaseDatabase.getReference("Chats").child(idChat);
        reference.keepSynced(true);
        reference.orderByChild("createdAt");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MessageModel temp = snapshot.getValue(MessageModel.class);
                    if (temp.getSender().equals(idSender) && temp.getReceiver().equals(idReceiver)
                            || temp.getSender().equals(idReceiver) && temp.getReceiver().equals(idSender)){
                        User user = new User(temp.getSender(), "", "", "", false);
                        Message message = new Message(temp.getSender(), user, temp.getMessage(), new Date(temp.getCreatedAt()));
                        messages.add(message);
                    }
                }

                adapter.clear();
                adapter.addToEnd(messages, true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(CharSequence input) {

        DatabaseReference reference = firebaseDatabase.getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender.getId());
        hashMap.put("receiver", receiver.getId());
        hashMap.put("message", input.toString());
        hashMap.put("createdAt", System.currentTimeMillis());

        reference.child("Chats").child(idChat).push().setValue(hashMap);

        Message message = new Message(idSender, sender, input.toString());
        adapter.addToStart(message, true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        if (!input.equals("")){
            sendMessage(input);
        } else {
            Toast.makeText(ChatActivity.this, "Isi pesan kosong, mau kirim apa?", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInformation(userModel.getId(), userModel.getUsername(),
                userModel.getUrlPhoto(), System.currentTimeMillis(),
                true, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateUserInformation(userModel.getId(), userModel.getUsername(),
                userModel.getUrlPhoto(), System.currentTimeMillis(),
                false, false);
    }

    @Override
    public void onStartTyping() {
        updateUserInformation(userModel.getId(), userModel.getUsername(),
                userModel.getUrlPhoto(), System.currentTimeMillis(),
                true, true);
    }

    @Override
    public void onStopTyping() {
        updateUserInformation(userModel.getId(), userModel.getUsername(),
                userModel.getUrlPhoto(), System.currentTimeMillis(),
                true, false);
    }
}
