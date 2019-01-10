package ntt.bps.namberwan.allstatntt.publikasi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.VolleySingleton;
import ntt.bps.namberwan.allstatntt.auth.AuthActivity;

public class ViewPublikasiActivity extends AppCompatActivity {

    public static final String ID_PUBLIKASI = "id publikasi";

    private TextView judulTv;
    private TextView tanggalTv;
    private TextView issnTv;
    private ImageView coverImage;
    private TextView abstrakTv;
    private TextView nomerKatalog;
    private TextView nomerPublikasi;
    private View issnView;
    private View noKatalogView;
    private View noPubView;
    private FloatingActionButton fab;
    private ShimmerFrameLayout shimmerFrameLayout;

    private String urlPdf;
    private String judul;
    private boolean isTokenExpired;

    private View main1;
    private View main2;

    private JSONObject jsonObject;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_publikasi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        String idPublikasi = getIntent().getStringExtra(ID_PUBLIKASI);

        setupInitView();

        getData(idPublikasi);
    }

    private void getData(String idPublikasi) {
        String s = getResources()
                .getString(R.string.web_service_path_detail_publication) + getResources().getString(R.string.api_key)
                + "&id=" + idPublikasi;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, s , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonObject = response.getJSONObject("data");
                    judul = jsonObject.getString("title");
                    judulTv.setText(jsonObject.getString("title"));
                    tanggalTv.setText(AppUtil.getDate(jsonObject.getString("rl_date"), false));

                    Picasso.get()
                            .load(jsonObject.getString("cover"))
                            .error(new IconicsDrawable(getBaseContext()).color(ContextCompat.getColor(getBaseContext(), R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                            .placeholder(new IconicsDrawable(getBaseContext()).color(ContextCompat.getColor(getBaseContext(), R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                            .fit()
                            .into(coverImage);

                    abstrakTv.setText(Html.fromHtml(jsonObject.getString("abstract")));

                    if (!jsonObject.getString("issn").equals("")){
                        issnTv.setText(jsonObject.getString("issn"));
                    }else {
                        issnView.setVisibility(View.GONE);
                    }
                    if (!jsonObject.getString("kat_no").equals("")){
                        nomerKatalog.setText(jsonObject.getString("kat_no"));
                    }else {
                        noKatalogView.setVisibility(View.GONE);
                    }
                    if (!jsonObject.getString("pub_no").equals("")){
                        nomerPublikasi.setText(jsonObject.getString("pub_no"));
                    }else {
                        noPubView.setVisibility(View.GONE);
                    }

                    urlPdf = jsonObject.getString("pdf");

                    setMainViewVisibility(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void setupInitView() {
        judulTv = findViewById(R.id.judul_publikasi);
        tanggalTv = findViewById(R.id.tanggal_publikasi);
        issnTv = findViewById(R.id.issn_publikasi);
        abstrakTv = findViewById(R.id.abstrak);
        coverImage = findViewById(R.id.cover_publikasi);
        nomerKatalog = findViewById(R.id.nomer_katalog);
        nomerPublikasi = findViewById(R.id.nomer_publikasi);
        issnView = findViewById(R.id.issn_view);
        noKatalogView = findViewById(R.id.nomer_katalog_view);
        noPubView = findViewById(R.id.nomer_publikasi_view);

        main1 = findViewById(R.id.main1);
        main2 = findViewById(R.id.main2);

        fab = findViewById(R.id.fab);

        final ViewPublikasiActivity activity = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Snackbar snackbar = Snackbar.make(view, R.string.checking_user_auth_string, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                final Intent i = new Intent(activity, AuthActivity.class);
                if (AppUtil.getToken(activity)==null){

                    startActivity(i);

                }else {
                    String token = AppUtil.getToken(activity);

                    final String s = urlPdf + token;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, activity.getString(R.string.web_service_path_cek_token_valid) + token, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        snackbar.dismiss();
                                        isTokenExpired = response.getString("message").equals("Token is expired");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                        builder.setTitle("Token Invalid").setMessage("Silakan login ulang")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int j) {
                                                        startActivity(i);
                                                    }
                                                });

                                        builder.show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            isTokenExpired = false;
                            String namaFile = judul.replaceAll("\\W+", "");
                            AppUtil.downloadFile(activity, s, judul, namaFile + ".pdf");
                            snackbar.dismiss();
                        }
                    });
                    queue.add(request);
                }
            }
        });

        shimmerFrameLayout = findViewById(R.id.shimmer);

        setMainViewVisibility(false);
    }

    private void setMainViewVisibility(boolean isVisible) {
        if (isVisible){
            main1.setVisibility(View.VISIBLE);
            main2.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }else {
            main1.setVisibility(View.GONE);
            main2.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
