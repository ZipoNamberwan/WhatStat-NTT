package ntt.bps.namberwan.allstatntt.publikasi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.EndlessRecyclerViewScrollListener;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublikasiFragment extends Fragment{

    private RequestQueue queue;
    private ArrayList<PublikasiItem> list;
    private PublikasiAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading;
    private DatabaseHelper db;
    private ShimmerFrameLayout shimmerFrameLayout;


    public PublikasiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publikasi, container, false);
        db = new DatabaseHelper(getContext());

        isLoading = true;
        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmerAnimation();

        recyclerView = view.findViewById(R.id.listview);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        isLoading = false;

        list = new ArrayList<>();
        addDataToArray(1);

        adapter = new PublikasiAdapter(list, getActivity(), new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Object object) {
                //do shit here
            }
        });
        SlideInBottomAnimationAdapter animatedAdapter = new SlideInBottomAnimationAdapter(adapter);
        animatedAdapter.setDuration(500);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(animatedAdapter));
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                addDataToArray(page + 1);
            }
        });

        return view;
    }

    private void addDataToArray(final int page){
        isLoading = true;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.web_service_path_list_publication)
                + getString(R.string.api_key) + "&page="+page, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        addJSONToAdapter(jsonObject, page);
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        isLoading = false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
            }
        });
        queue.add(request);
    }

    private void addJSONToAdapter(JSONObject jsonObject, int page) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
            for(int i = 0; i < jsonArray.length() ; i++){
                list.add(new PublikasiItem(jsonArray.getJSONObject(i).getString("pub_id"),
                        jsonArray.getJSONObject(i).getString("title"),jsonArray.getJSONObject(i).getString("rl_date"),
                        jsonArray.getJSONObject(i).getString("issn"),jsonArray.getJSONObject(i).getString("title"),
                        jsonArray.getJSONObject(i).getString("issn"),jsonArray.getJSONObject(i).getString("cover"),
                        jsonArray.getJSONObject(i).getString("pdf"),
                        db.isPublikasiBookmarked(jsonArray.getJSONObject(i).getString("pub_id")), false));
            }
            adapter.notifyItemRangeInserted(page * 10 + 1,jsonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
