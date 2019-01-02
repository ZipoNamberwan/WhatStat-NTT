package ntt.bps.namberwan.allstatntt.brs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.EndlessRecyclerViewScrollListener;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrsFragment extends Fragment {

    private DatabaseHelper db;
    private RequestQueue queue;
    private boolean isLoading;

    private ArrayList<BrsItem> list;
    private BrsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    public BrsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = new DatabaseHelper(getContext());
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        isLoading = false;

        View view = inflater.inflate(R.layout.fragment_brs, container, false);
        recyclerView = view.findViewById(R.id.listview);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new BrsAdapter(list, getActivity(), new RecyclerViewClickListener() {
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

        addDataToArray(1);

        return view;
    }

    private void addDataToArray(final int page) {
        isLoading = true;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.web_service_path_list_press_release)
                + getString(R.string.api_key) + "&page="+page, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        addJSONToAdapter(jsonObject, page);
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

    private void addJSONToAdapter(JSONObject jsonObject, int page){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
            for (int i = 0; i < jsonArray.length(); i++){
                boolean isSection;
                if (i == 0) {
                    isSection = list.isEmpty() || !AppUtil.getDate(list.get(list.size() - 1).getTanggalRilis(), true).equals(AppUtil.getDate(jsonArray.getJSONObject(0).getString("rl_date"), true));
                }else {
                    isSection = !AppUtil.getDate(jsonArray.getJSONObject(i).getString("rl_date"), true).equals(AppUtil.getDate(jsonArray.getJSONObject(i - 1).getString("rl_date"), true));
                }
                list.add(new BrsItem(jsonArray.getJSONObject(i).getString("brs_id"),
                        jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("brs_id"),
                        jsonArray.getJSONObject(i).getString("rl_date"), jsonArray.getJSONObject(i).getString("pdf"),
                        jsonArray.getJSONObject(i).getString("subj"), jsonArray.getJSONObject(i).getString("title"),
                        4,
                        db.isBrsBookmarked(jsonArray.getJSONObject(i).getString("brs_id")), isSection));
            }
            adapter.notifyItemRangeInserted(page * 10 + 1,jsonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

