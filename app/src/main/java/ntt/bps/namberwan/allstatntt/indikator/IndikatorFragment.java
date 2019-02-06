package ntt.bps.namberwan.allstatntt.indikator;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import ntt.bps.namberwan.allstatntt.EndlessRecyclerViewScrollListener;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndikatorFragment extends Fragment {

    private RequestQueue queue;

    private ArrayList<IndikatorItem> list;
    private IndikatorAdapter adapter;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private View failureView;

    public IndikatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_indikator, container, false);

        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        shimmerFrameLayout = view.findViewById(R.id.shimmer);

        failureView = view.findViewById(R.id.view_failure);
        Button refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToArray(1);
            }
        });

        recyclerView = view.findViewById(R.id.listview);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();

        adapter = new IndikatorAdapter(list, getActivity(), new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Object object) {
                //do shit here
                Intent i = new Intent(getActivity(), IndikatorViewActivity.class);
                switch (((IndikatorItem) object).getId()) {
                    case "1":
                        //IPM
                        i.putExtra(IndikatorViewActivity.VAR_ID, "46");
                        startActivity(i);
                        break;
                    case "2":
                        //jumlah penduduk
                        i.putExtra(IndikatorViewActivity.VAR_ID, "28");
                        startActivity(i);
                        break;
                    case "3":
                        //inflasi
                        i.putExtra(IndikatorViewActivity.VAR_ID, "1");
                        startActivity(i);
                        break;
                    case "4":
                        //jml penduduk miskin
                        i.putExtra(IndikatorViewActivity.VAR_ID, "584");
                        startActivity(i);
                        break;
                    case "5":
                        //pengangguran
                        i.putExtra(IndikatorViewActivity.VAR_ID, "522");
                        startActivity(i);
                        break;
                    case "7":
                        //pertumbuhan ekonomi
                        i.putExtra(IndikatorViewActivity.VAR_ID, "438");
                        startActivity(i);
                        break;
                    case "8":
                        //Harapan Hidup
                        i.putExtra(IndikatorViewActivity.VAR_ID, "583");
                        startActivity(i);
                        break;
                    case "9":
                        //Ekspor
                        i.putExtra(IndikatorViewActivity.VAR_ID, "107");
                        startActivity(i);
                        break;
                    case "10":
                        //Impor
                        i.putExtra(IndikatorViewActivity.VAR_ID, "109");
                        startActivity(i);
                        break;
                    case "11":
                        //NTP
                        i.putExtra(IndikatorViewActivity.VAR_ID, "104");
                        startActivity(i);
                        break;
                    case "12":
                        //Jumlah Wisman
                        i.putExtra(IndikatorViewActivity.VAR_ID, "67");
                        startActivity(i);
                        break;
                    case "13":
                        //Gini Rasio
                        i.putExtra(IndikatorViewActivity.VAR_ID, "616");
                        startActivity(i);
                        break;
                }
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

        setViewVisibility(false,true,false);

        addDataToArray(1);

        return view;
    }

    private void addDataToArray(final int page) {
        if (list.isEmpty()){
            setViewVisibility(false, true, false);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.web_service_path_list_indicators)
                + getString(R.string.api_key) + "&page="+page, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (list.isEmpty()){
                            setViewVisibility(true, false, false);
                        }
                        addJSONToAdapter(jsonObject, page);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (list.isEmpty()){
                    setViewVisibility(false, false, true);
                }
            }
        });
        queue.add(request);
    }

    private void addJSONToAdapter(JSONObject jsonObject, int page){
        try {
            if (jsonObject.getString("data-availability").equals("available")){
                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                ArrayList<IndikatorItem> subList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++){
                    subList.add(new IndikatorItem(jsonArray.getJSONObject(i).getString("indicator_id"),
                            jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("name"),
                            jsonArray.getJSONObject(i).getString("data_source"), jsonArray.getJSONObject(i).getString("value"),
                            jsonArray.getJSONObject(i).getString("unit")));
                }
                Collections.sort(subList, new Comparator<IndikatorItem>() {
                    @Override
                    public int compare(IndikatorItem o1, IndikatorItem o2) {
                        Integer a = Integer.parseInt(o1.getId());
                        Integer b = Integer.parseInt(o2.getId());
                        return a.compareTo(b);
                    }
                });

                list.addAll(subList);
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
