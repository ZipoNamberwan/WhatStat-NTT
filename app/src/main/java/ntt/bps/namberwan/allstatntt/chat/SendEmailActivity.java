package ntt.bps.namberwan.allstatntt.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ntt.bps.namberwan.allstatntt.R;

public class SendEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Hubungi Kami - Email");

        Button button = findViewById(R.id.kirim_button);
        final EditText judul = findViewById(R.id.judul_input);
        final EditText pesan = findViewById(R.id.pesan_input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String judulString = judul.getText().toString();
                String pesanString = pesan.getText().toString();
                if (!judulString.equals("") | !pesanString.equals("")){
                    sendEmail(judul.getText().toString(), pesan.getText().toString());
                } else {
                    Snackbar.make(view, "Email Kosong!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail(String judul, String pesan) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "pst5300@bps.go.id"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, judul);
        emailIntent.putExtra(Intent.EXTRA_TEXT, pesan);
        //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendEmailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
