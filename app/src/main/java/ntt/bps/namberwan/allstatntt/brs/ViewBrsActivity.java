package ntt.bps.namberwan.allstatntt.brs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

public class ViewBrsActivity extends AppCompatActivity {

    public static final String ID_BRS = "id brs";

    private String idBrs;
    private String urlDownload;
    private String judul;

    private TextView judulTv;
    private TextView releaseDateTv;
    private TextView sizeTv;
    private WebView abstrakWebView;
    private FloatingActionButton fab;

    private View mainView;
    private ShimmerFrameLayout shimmerFrameLayout;

    private JSONObject jsonObject;

    private RequestQueue queue;

    private ViewBrsActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_brs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        idBrs = getIntent().getStringExtra(ID_BRS);

        setUpInitView();

        getData(idBrs);

    }

    private void setUpDetailView() throws JSONException {
        judul = jsonObject.getString("title");
        String tanggalUp = AppUtil.getDate(jsonObject.getString("rl_date"), false);
        String size = "Size: " + jsonObject.getString("size");
        String abstrak = Html.fromHtml(jsonObject.getString("abstract")).toString();
        urlDownload = jsonObject.getString("pdf");

        judulTv.setText(judul);
        releaseDateTv.setText(tanggalUp);
        sizeTv.setText(size);
        abstrakWebView.loadData(abstrak, "text/html; charset=UTF-8", null);
    }

    private void getData(String idBrs) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        String s = getResources()
                .getString(R.string.web_service_path_detail_brs) + getResources().getString(R.string.api_key)
                + "&id=" + idBrs;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, s , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonObject = response.getJSONObject("data");
                    setUpDetailView();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    mainView.setVisibility(View.VISIBLE);
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

    private void setUpInitView(){
        mainView = findViewById(R.id.main);
        mainView.setVisibility(View.GONE);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        judulTv = findViewById(R.id.judul);
        releaseDateTv = findViewById(R.id.last_periode);
        sizeTv = findViewById(R.id.size);
        abstrakWebView = findViewById(R.id.abstrak);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                AppUtil.downloadFile(activity, urlDownload + "667e9fae3dfa0f334cf6d7c77462bdf0", judul);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder
                        .setTitle("Download")
                        .setMessage("Download BRS?")
                        .setPositiveButton("Ya", onClickListener)
                        .setNegativeButton("Tidak", onClickListener)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
