package ntt.bps.namberwan.allstatntt.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;

public class ViewChatAdminActivity extends AppCompatActivity {

    public static final int SIGN_IN_REQUEST_CODE = 0;

    private RecyclerView recyclerView;

    private String idUser;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chat_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.ic_bps_launcher)
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            firebaseAuth = FirebaseAuth.getInstance();
            reference = FirebaseDatabase.getInstance().getReference();
            idUser = firebaseAuth.getCurrentUser().getUid();
            updateUserInformation(System.currentTimeMillis(), true);
            displayListAdmin();
        }
    }

    private void updateUserInformation(long lastSeen, boolean isOnline) {

        String key = reference.child(idUser).getKey();

        Map<String, Object> update = new HashMap<>();
        update.put("username", firebaseAuth.getCurrentUser().getDisplayName());
        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null){
            update.put("urlPhoto", firebaseAuth.getCurrentUser().getPhotoUrl().getPath());
        } else {
            update.put("urlPhoto", "");
        }

        update.put("lastSeen", lastSeen);
        if (isOnline){
            update.put("isOnline", true);
        } else {
            update.put("isOnline", false);
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + key + "/information/", update);

        reference.updateChildren(childUpdates);
    }

    private void displayListAdmin() {

        List<User> adminUsers = ChatUtils.getAdminList();

        AdminUserAdapter adapter = new AdminUserAdapter(adminUsers, this, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Object object) {
                Intent i = new Intent(ViewChatAdminActivity.this, ChatActivity.class);
                i.putExtra(ChatActivity.ID_ADMIN_RECEIVER, ((User) object).getId());
                i.putExtra(ChatActivity.ID_USER_SENDER, idUser);
                startActivity(i);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateUserInformation(System.currentTimeMillis(), false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInformation(System.currentTimeMillis(), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayListAdmin();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ViewChatAdminActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }
}
