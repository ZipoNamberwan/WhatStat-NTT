package ntt.bps.namberwan.allstatntt.berita;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ntt.bps.namberwan.allstatntt.AppUtils;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

public class ViewBeritaActivity extends AppCompatActivity {

    public static final String ID_BERITA = "id berita";

    private DatabaseHelper db;

    private TextView judul;
    private TextView jenis;
    private TextView tanggal;
    private WebView rincian;
    private ImageView gambar;
    private ImageView scheduleImage;
    private FloatingActionButton fab;

    private String idBerita;
    private String judulString;
    private String tanggalString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        idBerita = getIntent().getStringExtra(ID_BERITA);

        setContentView(R.layout.activity_view_berita);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        judul = findViewById(R.id.judul);
        jenis = findViewById(R.id.jenis_berita);
        tanggal = findViewById(R.id.tanggal_berita);
        rincian = findViewById(R.id.rincian_berita);
        gambar = findViewById(R.id.gambar_berita);
        scheduleImage = findViewById(R.id.image_schedule);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.web_service_path_detail_news)
                + getString(R.string.api_key) + "&id=" + idBerita, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("data");

                    judulString = jsonObject.getString("title");
                    tanggalString = jsonObject.getString("rl_date");

                    judul.setText(judulString);
                    jenis.setText(jsonObject.getString("news_type"));
                    tanggal.setText(AppUtils.getDate(tanggalString, false));
                    rincian.loadData(Html.fromHtml(jsonObject.getString("news")).toString(), "text/html; charset=UTF-8", null);

                    fab.show();
                    scheduleImage.setVisibility(View.VISIBLE);
                    rincian.setVisibility(View.VISIBLE);

                    Picasso.get()
                            .load(jsonObject.getString("picture"))
                            .fit()
                            .into(gambar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
    }

    private void share() {
        String urlShare = AppUtils.getUrlShare(getString(R.string.web_share_news), tanggalString, idBerita, judulString);
        AppUtils.share(this, judulString, urlShare);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
