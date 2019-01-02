package ntt.bps.namberwan.allstatntt.indikator;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndikatorFragment extends Fragment {

    private DatabaseHelper db;
    private RequestQueue queue;
    private boolean isLoading;

    private ArrayList<IndikatorItem> list;
    private IndikatorAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    public IndikatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_indikator, container, false);

        db = new DatabaseHelper(getContext());
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        isLoading = false;

        recyclerView = view.findViewById(R.id.listview);
        mLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        /*for (int i = 0; i < 3; i++){
            list.add(new IndikatorItem("1","2","3","4", "5","6"));
        }*/
        adapter = new IndikatorAdapter(list, getActivity(), new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Object object) {
                //do shit here
                if (((IndikatorItem) object).getId().equals("2")){
                    //jumlah penduduk

                }else if (((IndikatorItem) object).getId().equals("3")){
                    //inflasi
                    Intent i = new Intent(getActivity(), IndikatorViewActivity.class);
                    i.putExtra(IndikatorViewActivity.VAR_ID, "2");
                    startActivity(i);
                }else if (((IndikatorItem) object).getId().equals("4")){
                    //jml penduduk miskin
                    Intent i = new Intent(getActivity(), IndikatorViewActivity.class);
                    i.putExtra(IndikatorViewActivity.VAR_ID, "584");
                    startActivity(i);
                }else if (((IndikatorItem) object).getId().equals("5")){
                    //pengangguran
                    Intent i = new Intent(getActivity(), IndikatorViewActivity.class);
                    i.putExtra(IndikatorViewActivity.VAR_ID, "522");
                    startActivity(i);
                }else if (((IndikatorItem) object).getId().equals("7")){
                    //pertumbuhan ekonomi
                    Intent i = new Intent(getActivity(), IndikatorViewActivity.class);
                    i.putExtra(IndikatorViewActivity.VAR_ID, "438");
                    startActivity(i);
                }else if (((IndikatorItem) object).getId().equals("8")){
                    //Harapan Hidup
                }else if (((IndikatorItem) object).getId().equals("9")){
                    //Ekspor
                }else if (((IndikatorItem) object).getId().equals("10")){
                    //Impor
                }else if (((IndikatorItem) object).getId().equals("11")){
                    //NTP
                }else if (((IndikatorItem) object).getId().equals("13")){
                    //Gini Rasio
                    Intent i = new Intent(getActivity(), IndikatorViewActivity.class);
                    i.putExtra(IndikatorViewActivity.VAR_ID, "616");
                    startActivity(i);
                }
            }
        });

        SlideInBottomAnimationAdapter animatedAdapter = new SlideInBottomAnimationAdapter(adapter);
        animatedAdapter.setDuration(500);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(animatedAdapter));

        addDataToArray();
        return view;
    }
    private void addDataToArray() {
        isLoading = true;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.web_service_path_list_indicators)
                + getString(R.string.api_key), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        addJSONToAdapter(jsonObject);
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

    private void addJSONToAdapter(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
            for (int i = 0; i < jsonArray.length(); i++){
                list.add(new IndikatorItem(jsonArray.getJSONObject(i).getString("indicator_id"),
                        jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getString("data_source"), jsonArray.getJSONObject(i).getString("value"),
                        jsonArray.getJSONObject(i).getString("unit")));
            }
            Collections.sort(list, new Comparator<IndikatorItem>() {
                @Override
                public int compare(IndikatorItem o1, IndikatorItem o2) {
                    Integer a = Integer.parseInt(o1.getId());
                    Integer b = Integer.parseInt(o2.getId());
                    return a.compareTo(b);
                }
            });

            adapter.notifyItemRangeInserted(0,jsonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
