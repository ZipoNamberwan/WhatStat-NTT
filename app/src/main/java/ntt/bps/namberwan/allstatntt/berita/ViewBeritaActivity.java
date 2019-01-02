package ntt.bps.namberwan.allstatntt.berita;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

public class ViewBeritaActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RequestQueue requestQueue;
    private String idBerita;

    private TextView jenis;
    private TextView tanggal;
    private TextView rincian;
    private ImageView gambar;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        idBerita = getIntent().getStringExtra(BeritaAdapter.ID_BERITA);

        setContentView(R.layout.activity_view_berita);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        toolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        jenis = findViewById(R.id.jenis_berita);
        tanggal = findViewById(R.id.tanggal_berita);
        rincian = findViewById(R.id.rincian_berita);
        gambar = findViewById(R.id.gambar_berita);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.web_service_path_view_news)
                + getString(R.string.api_key) + "&id=" + idBerita, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    /*judul.setText(Html.fromHtml(jsonObject.getString("news_id")));*/
                    toolbarLayout.setTitle(jsonObject.getString("title"));
                    jenis.setText(jsonObject.getString("news_type"));
                    tanggal.setText(AppUtil.getDate(jsonObject.getString("rl_date"), false));
                    rincian.setText(stripHtml(jsonObject.getString("news")));
                    Picasso.get()
                            .load(jsonObject.getString("picture"))
                            .error(new IconicsDrawable(getApplicationContext()).color(ContextCompat.getColor(getApplicationContext(), R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                            .placeholder(new IconicsDrawable(getApplicationContext()).color(ContextCompat.getColor(getApplicationContext(), R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }
}
