package ntt.bps.namberwan.allstatntt.tabelstatis;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import ntt.bps.namberwan.allstatntt.AppUtils;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.EndlessRecyclerViewScrollListener;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

import static ntt.bps.namberwan.allstatntt.MainActivity.SEARCH_KEYWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabelFragment extends Fragment {

    private DatabaseHelper db;
    private RequestQueue queue;
    private boolean isLoading;
    private boolean isViewCreated;

    private ArrayList<TabelItem> list;
    private TabelAdapter adapter;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private View failureView;
    private String keyword;

    public TabelFragment() {
        // Required empty public constructor
        isViewCreated = false;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tabel, container, false);
        db = new DatabaseHelper(getContext());
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        isLoading = false;

        if (getArguments()!=null){
            keyword = "&keyword=" + getArguments().getString(SEARCH_KEYWORD);
        }else {
            keyword = "";
        }

        failureView = view.findViewById(R.id.view_failure);
        Button refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToArray(1);
            }
        });

        recyclerView = view.findViewById(R.id.listview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        shimmerFrameLayout = view.findViewById(R.id.shimmer);

        list = new ArrayList<>();
        adapter = new TabelAdapter(list, getActivity(), new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Object object) {
                // do shit here
                String s = ((TabelItem)object).getId();
                Intent i = new Intent(getActivity(), ViewTabelActivity.class);
                i.putExtra(ViewTabelActivity.ID_TABEL, s);
                startActivity(i);
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

        setViewVisibility(false, true, false);

        if (isVisible()){
            addDataToArray(1);
        }

        isViewCreated = true;

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewCreated & isVisibleToUser){
            if (list.isEmpty()){
                addDataToArray(1);
            }
        }
    }

    private void addDataToArray(final int page) {
        isLoading = true;
        if (list.isEmpty()){
            setViewVisibility(false, true, false);
        }
        String url = getString(R.string.web_service_path_list_table)
                + getString(R.string.api_key) + "&page="+page + keyword;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (list.isEmpty()){
                            setViewVisibility(true, false, false);
                        }
                        addJSONToAdapter(jsonObject, page);
                        isLoading = false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                if (list.isEmpty()){
                    setViewVisibility(false, false, true);
                }
            }
        });
        queue.add(request);
    }

    private void addJSONToAdapter(JSONObject jsonObject, int page) {
        try {
            if (jsonObject.getString("data-availability").equals("available")){
                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                for (int i = 0; i < jsonArray.length(); i++){
                    boolean isSection;
                    if (i == 0) {
                        isSection = list.isEmpty() || !AppUtils.getDate(list.get(list.size() - 1).getTanggal(), true).equals(AppUtils.getDate(jsonArray.getJSONObject(0).getString("updt_date"), true));
                    }else {
                        isSection = !AppUtils.getDate(jsonArray.getJSONObject(i).getString("updt_date"), true).equals(AppUtils.getDate(jsonArray.getJSONObject(i - 1).getString("updt_date"), true));
                    }
                    list.add(new TabelItem(jsonArray.getJSONObject(i).getString("table_id"),
                            jsonArray.getJSONObject(i).getString("subj"), jsonArray.getJSONObject(i).getString("updt_date"),
                            jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("excel"),
                            jsonArray.getJSONObject(i).getString("updt_date"),jsonArray.getJSONObject(i).getString("updt_date"),
                            4,
                            db.isTabelBookmarked(jsonArray.getJSONObject(i).getInt("table_id")), isSection));
                }
                adapter.notifyItemRangeInserted(page * 10 + 1,jsonArray.length());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility(boolean isMainVisible, boolean isShimmerVisible, boolean isFailureVisible) {

        if (isMainVisible){
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
        }

        if (isShimmerVisible){
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
        }else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }

        if (isFailureVisible){
            failureView.setVisibility(View.VISIBLE);
        }else {
            failureView.setVisibility(View.GONE);
        }

    }

}
