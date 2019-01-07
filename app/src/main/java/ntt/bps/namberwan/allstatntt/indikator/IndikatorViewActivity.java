package ntt.bps.namberwan.allstatntt.indikator;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.VolleySingleton;

public class IndikatorViewActivity extends AppCompatActivity {

    public static final String VAR_ID = "var id";

    private RequestQueue queue;
    private boolean isLoading;

    private JSONObject jsonObject;

    //Grafik View
    private TextView judulGrafik;
    private LineChart lineChart;
    private TextView detailStatGrafik;
    private TextView valueHighlight1Grafik;
    private TextView tahunHighlight1Grafik;
    private TextView valueHighlight2Grafik;
    private TextView tahunHighlight2Grafik;
    private ImageView icon;
    private ImageView arrow;
    private Button moreGrafikButton;
    private ImageButton saveGrafikButton;
    private Button verVarSpinner;
    private Button turVarSpinnerGrafik;
    private Button tahunSpinner;
    private Button turTahunSpinner;
    private Button turVarSpinnerTabel;

    //Tabel View
    private TextView judulTabel;
    private TableLayout tableLayout;
    private ImageButton filterTabelButton;
    private Button moreTabelButton;

    //Deskripsi View
    private TextView judulDeskripsi;
    private TextView lastUpdateDeskripsi;
    private TextView subjekDeskripsi;
    private TextView definisi;
    private TextView sumber;

    private String idVerVar;
    private String labelVerVar;
    private String idTurvarGrafik;
    private String labelTurvarGrafik;
    private String idTahun;
    private String labelTahun;
    private String idTurTahun;
    private String labelTurTahun;
    private String idTurvarTabel;
    private String labelTurvarTabel;

    private String labelHeader;
    private String periodeHeader;

    private List<Entry> subGrafikContents;
    private List<String> subAxisLabels;
    private List<String> tabelContents;

    private List<VertikalVariabel> vertikalVariabels;
    private List<TurunanVertikalVariabel> turunanVertikalVariabels;
    private List<TahunVariabel> tahunVariabels;
    private List<TurunanTahunVariabel> turunanTahunVariabels;

    private String idVar;
    private String labelVar;
    private String unitVar;
    private String defVar;
    private String subjekVar;
    private String note;

    private View tabelView;
    private View grafikView;
    private View deskripsiView;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indikator_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        idVar = getIntent().getStringExtra(VAR_ID);
        isLoading = true;

        setUpInitialView();

        getData(idVar);

    }

    private void getData(String idVar) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.web_service_path_detail_indicators)
                + getString(R.string.api_key) + "&var=" + idVar, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            //setup variabel
                            setUpVarAttribute(jsonObject);

                            //setup grafik awal
                            idVerVar = vertikalVariabels.get(vertikalVariabels.size()-1).getId();
                            idTurvarGrafik = turunanVertikalVariabels.get(turunanVertikalVariabels.size()-1).getId();
                            setUpDataGrafik(jsonObject, idVerVar, idTurvarGrafik);
                            setUpGrafikStat();

                            //setup tabel awal
                            getLatestPeriode(jsonObject);
                            idTurvarTabel = turunanVertikalVariabels.get(turunanVertikalVariabels.size()-1).getId();
                            setUpDataTabel(jsonObject, idTahun, idTurTahun, idTurvarTabel);
                            setUpTabelStat();

                            //setup deskripsi
                            setUpDeskripsi();

                            //setup action bar
                            setTitle(labelVar);

                            //setup shimmer
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);

                            //setup view
                            setVisibilityView(true, false, false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void getLatestPeriode(JSONObject jsonObject) throws JSONException {

        JSONObject dataContentJson = jsonObject.getJSONObject("datacontent");

        String idDataContent = null;
        for (int x = tahunVariabels.size()-1; x >= 0; x--){
            String tahun = tahunVariabels.get(x).getId();
            for (int y = turunanTahunVariabels.size()-1; y >= 0; y-- ){
                String turTahun = turunanTahunVariabels.get(y).getId();
                for (int z = 0; z < vertikalVariabels.size(); z++){
                    String vertikalVar = vertikalVariabels.get(z).getId();
                    for (int a = 0; a < turunanVertikalVariabels.size(); a++){
                        String turVervar = turunanVertikalVariabels.get(a).getId();
                        idDataContent = vertikalVar + this.idVar + turVervar + tahun + turTahun;
                        if (dataContentJson.has(idDataContent)){
                            this.idTahun = tahun;
                            this.idTurTahun = turTahun;
                            break;
                        }
                    }
                    if (dataContentJson.has(idDataContent)){
                        break;
                    }
                }
                if (dataContentJson.has(idDataContent)){
                    break;
                }
            }
            if (dataContentJson.has(idDataContent)){
                break;
            }
        }
    }

    private void setUpInitialView() {
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmerAnimation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tabelView = findViewById(R.id.tabel_view);
        grafikView = findViewById(R.id.grafik_view);
        deskripsiView = findViewById(R.id.deskripsi_view);

        setVisibilityView(false, false, false);

        judulGrafik = findViewById(R.id.judul_grafik);
        lineChart = findViewById(R.id.chart);
        detailStatGrafik = findViewById(R.id.detail_nama_indikator);
        valueHighlight1Grafik = findViewById(R.id.value_grafik_highlight1);
        tahunHighlight1Grafik = findViewById(R.id.tahun_grafik_highlight1);
        valueHighlight2Grafik = findViewById(R.id.value_grafik_highlight2);
        tahunHighlight2Grafik = findViewById(R.id.tahun_grafik_highlight2);

        judulDeskripsi = findViewById(R.id.judul_deskripsi);
        lastUpdateDeskripsi = findViewById(R.id.last_periode);
        subjekDeskripsi = findViewById(R.id.subjek_deskripsi);
        definisi = findViewById(R.id.definisi);
        sumber = findViewById(R.id.sumber);

/*        valueHighlight1Tabel = findViewById(R.id.value_tabel_highlight1);
        verVarHighlight1Tabel = findViewById(R.id.vervar_tabel_highlight1);
        valueHighlight2Tabel = findViewById(R.id.value_tabel_highlight2);
        verVarHighlight2Tabel = findViewById(R.id.vervar_tabel_highlight2);*/

        icon = findViewById(R.id.icon_change);
        arrow = findViewById(R.id.arrow);
        moreGrafikButton = findViewById(R.id.more_grafik_button);
        moreGrafikButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GrafikDetailActivity.class);
                i.putExtra(GrafikDetailActivity.JSON_STRING, jsonObject.toString());
                i.putExtra(GrafikDetailActivity.INIT_VER_VAR, idVerVar);
                i.putExtra(GrafikDetailActivity.INIT_TUR_VAR, idTurvarGrafik);
                i.putExtra(GrafikDetailActivity.ID_VAR, idVar);
                startActivity(i);
            }
        });
        saveGrafikButton = findViewById(R.id.save_grafik_button);
        verVarSpinner = findViewById(R.id.spinner_vervar);
        turVarSpinnerGrafik = findViewById(R.id.spinner_turvar_grafik);

        verVarSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialogVerVar = createVervarDialog();
                dialogVerVar.show();
            }
        });

        turVarSpinnerGrafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialogTurVar = createTurVarDialog();
                dialogTurVar.show();
            }
        });

        saveGrafikButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = labelVar + " " + labelVerVar + " " + labelTurvarGrafik;
                s = s.replaceAll("[[-+.^:,/$#]]","");
                if (lineChart.saveToGallery(s)){
                    Snackbar.make(v, "Grafik tersimpan di gallery", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        judulTabel = findViewById(R.id.judul_tabel);
        tableLayout = findViewById(R.id.tabel);
        filterTabelButton = findViewById(R.id.filter_button);
        filterTabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFilterDialog().show();
            }
        });

        moreTabelButton = findViewById(R.id.more_tabel_button);
        moreTabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TabelDetailActivity.class);
                i.putExtra(TabelDetailActivity.ID_VAR, idVar);
                i.putExtra(TabelDetailActivity.JSON_STRING, jsonObject.toString());
                startActivity(i);
            }
        });
    }

    private void setUpDataTabel(JSONObject jsonObject, String tahunId, String turTahunId, String turVarId) throws JSONException {

        //Setup Data
        this.jsonObject = jsonObject;
        JSONArray verVarJsonArray = jsonObject.getJSONArray("vervar");
        JSONObject dataContentJson = jsonObject.getJSONObject("datacontent");

        tabelContents = new ArrayList<>();
        for (int k = 0; k < verVarJsonArray.length(); k++){
            String verVar = verVarJsonArray.getJSONObject(k).getString("val");
            String idDataContent = verVar + idVar + turVarId + tahunId + turTahunId;
            if (dataContentJson.has(idDataContent)){
                tabelContents.add(dataContentJson.getString(idDataContent));
            }else {
                tabelContents.add("-");
            }
        }

        //setup header value
        labelHeader = jsonObject.getString("labelvervar");
        periodeHeader = getPeriodeHeader(tahunId, turTahunId);

        //setup var tahun
        this.idTahun = tahunId;
        for (int i = 0; i < tahunVariabels.size(); i++){
            if (tahunId.equals(tahunVariabels.get(i).getId())){
                this.labelTahun = tahunVariabels.get(i).getLabel();
            }
        }
        //setup turunan tahun
        this.idTurTahun = turTahunId;
        for (int i = 0; i < turunanTahunVariabels.size(); i++){
            if (turTahunId.equals(turunanTahunVariabels.get(i).getId())){
                this.labelTurTahun = turunanTahunVariabels.get(i).getLabel();
            }
        }
        //setup turunan var
        this.idTurvarTabel = turVarId;
        for (int i = 0; i < turunanVertikalVariabels.size(); i++){
            if (turVarId.equals(turunanVertikalVariabels.get(i).getId())){
                this.labelTurvarTabel = turunanVertikalVariabels.get(i).getLabel();
            }
        }

    }

    private void setUpTabelStat(){
        tableLayout.removeAllViews();

        String s = "Tabel " + labelVar + " " + periodeHeader + " " + unitVar;
        judulTabel.setText(s);

        TableRow header = new TableRow(this);
        insertRow(tableLayout, header, labelHeader, periodeHeader, true, false);

        for (int i = 0; i < vertikalVariabels.size(); i++){
            TableRow tr = new TableRow(this);
            if (tabelContents.get(i).equals("-")){
                insertRow(tableLayout, tr, vertikalVariabels.get(i).getLabel(), tabelContents.get(i), false, i%2==0);
            }else {
                insertRow(tableLayout, tr, vertikalVariabels.get(i).getLabel(), AppUtil.formatNumberSeparator(Float.parseFloat(tabelContents.get(i))), false, i%2==0);
            }
        }
    }

    private void insertRow(TableLayout tableLayout, TableRow tr, String label, String value, boolean isHeader, boolean isEvenRow) {
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        View rowView = View.inflate(this,R.layout.layout_cell_tabel, null);
        if (isEvenRow){
            rowView.setBackgroundColor(getResources().getColor(R.color.md_blue_grey_50));
        }
        rowView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        TextView col1 = rowView.findViewById(R.id.vervar);
        TextView col2 = rowView.findViewById(R.id.value);
        col1.setText(label);
        col2.setText(value);
        if (isHeader){
            col1.setTypeface(Typeface.DEFAULT_BOLD);
            col1.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));
            col2.setTypeface(Typeface.DEFAULT_BOLD);
            col2.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        }
        tr.addView(rowView);
        tableLayout.addView(tr);
    }

    private void setUpDataGrafik(JSONObject jsonObject, String idVerVar, String idTurvar) throws JSONException {

        //Setup Data
        this.jsonObject = jsonObject;

        JSONObject dataContentJson = jsonObject.getJSONObject("datacontent");

        List<String> axisLabels = new ArrayList<>();
        List<Entry> grafikContents = new ArrayList<>();

        int z = 0;
        for (int x = 0; x < tahunVariabels.size(); x++){
            String tahunId = tahunVariabels.get(x).getId();
            for (int y = 0 ; y < turunanTahunVariabels.size(); y++) {
                String turTahunId = turunanTahunVariabels.get(y).getId();
                String idDataContent = idVerVar + idVar + idTurvar + tahunId + turTahunId;
                if (dataContentJson.has(idDataContent)){
                    axisLabels.add(turunanTahunVariabels.get(y).getLabel() + " " + tahunVariabels.get(x).getLabel());
                    float value = Float.parseFloat(dataContentJson.getString(idDataContent));
                    grafikContents.add(new Entry(z, value));
                    z++;
                }
            }
        }

        //Ambil lima data terbaru
        subGrafikContents = new ArrayList<>();
        if (grafikContents.size()>=5){
            z = 0;
            for (int i = grafikContents.size()-5; i < grafikContents.size(); i++){
                subGrafikContents.add(new Entry(z, grafikContents.get(i).getY()));
                z++;
            }
            subAxisLabels = axisLabels.subList(axisLabels.size()-5, axisLabels.size());
        }else {
            subGrafikContents = grafikContents;
            subAxisLabels = axisLabels;
        }

        //Setup data u/ spinner
        //Setup vervar Spinner
        this.idVerVar = idVerVar;
        for (int i = 0; i < vertikalVariabels.size(); i++){
            if ((vertikalVariabels.get(i).getId()).equals(idVerVar)){
                labelVerVar = vertikalVariabels.get(i).getLabel();
            }
        }

        //Setup turvar Spinner
        this.idTurvarGrafik = idTurvar;
        for (int i = 0; i < turunanVertikalVariabels.size(); i++){
            if (turunanVertikalVariabels.get(i).getId().equals(idTurvar)){
                labelTurvarGrafik = turunanVertikalVariabels.get(i).getLabel();
            }
        }
    }

    private void setUpGrafikStat() {

        //Setup Grafik
        LineDataSet dataSet = new LineDataSet(subGrafikContents, "");
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return AppUtil.formatNumberSeparator(value);
            }
        });
        dataSet.setColor(getResources().getColor(R.color.primary));
        dataSet.setCircleColor(getResources().getColor(R.color.primary));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextColor(getResources().getColor(R.color.material_drawer_primary_text));

        LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize(12f);
        lineData.setHighlightEnabled(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(subAxisLabels));
        xAxis.setAxisLineWidth(2f);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(getResources().getColor(R.color.material_drawer_secondary_text));
        xAxis.setLabelRotationAngle(20f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(true);
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(getResources().getColor(R.color.material_drawer_secondary_text));
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return AppUtil.formatNumberSeparator(value);
            }
        });

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.animateX(700);
        Description description = new Description();
        description.setText("*Sumber: Badan Pusat Statistik Provinsi NTT");
        description.setTextColor(getResources().getColor(R.color.material_drawer_secondary_text));
        lineChart.setDescription(description);

        String s = "Grafik " + labelVar + " " + labelVerVar + " "
                + subAxisLabels.get(0)+" - "+ subAxisLabels.get(subAxisLabels.size()-1) + " " + unitVar;
        judulGrafik.setText(s);

        float value1 = subGrafikContents.get(subGrafikContents.size()-2).getY();
        float value2 = subGrafikContents.get(subGrafikContents.size()-1).getY();

        //Setup Detail Statistik

        valueHighlight1Grafik.setText(AppUtil.formatNumberSeparator(value1));
        tahunHighlight1Grafik.setText(subAxisLabels.get(subAxisLabels.size()-2));
        valueHighlight2Grafik.setText(AppUtil.formatNumberSeparator(value2));
        tahunHighlight2Grafik.setText(subAxisLabels.get(subAxisLabels.size()-1));
        s = labelVar + " " + labelVerVar + " " + unitVar;
        detailStatGrafik.setText(s);

        if (value1>value2){
            icon.setImageDrawable(new IconicsDrawable(this).color(getResources().getColor(R.color.md_red_600)).icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down));
        }else {
            icon.setImageDrawable(new IconicsDrawable(this).color(getResources().getColor(R.color.md_green_600)).icon(GoogleMaterial.Icon.gmd_keyboard_arrow_up));
        }
        arrow.setImageDrawable(new IconicsDrawable(this).color(getResources().getColor(R.color.material_drawer_hint_text)).icon(FontAwesome.Icon.faw_long_arrow_right));

        //Setup Spinner dan button
        verVarSpinner.setText(labelVerVar);
        turVarSpinnerGrafik.setText(labelTurvarGrafik);
        if (turunanVertikalVariabels.size()==1){
            turVarSpinnerGrafik.setVisibility(View.GONE);
        }
    }

    private void setUpDeskripsi(){
        judulDeskripsi.setText(labelVar);
        subjekDeskripsi.setText(subjekVar);
        definisi.setText(defVar);
        sumber.setText(note);
        String s = "Data terakhir: " + getPeriodeHeader(idTahun, idTurTahun);
        lastUpdateDeskripsi.setText(s);
    }

    private List<TurunanTahunVariabel> sortTurTahunList(List<TurunanTahunVariabel> turunanTahunVariabels){

        Collections.sort(turunanTahunVariabels, new Comparator<TurunanTahunVariabel>() {
            @Override
            public int compare(TurunanTahunVariabel o1, TurunanTahunVariabel o2) {

                    Integer a = Integer.parseInt(o1.getId());
                    Integer b = Integer.parseInt(o2.getId());
                    return a.compareTo(b);
            }
        });

        return turunanTahunVariabels;
    }

    private List<TahunVariabel> sortTahunList(List<TahunVariabel> tahunVariabels) {

        Collections.sort(tahunVariabels, new Comparator<TahunVariabel>() {
            @Override
            public int compare(TahunVariabel o1, TahunVariabel o2) {
                    Integer a = Integer.parseInt(o1.getLabel());
                    Integer b = Integer.parseInt(o2.getLabel());
                    return a.compareTo(b);
            }
        });

        return tahunVariabels;
    }

    private void setUpVarAttribute(JSONObject jsonObject) throws JSONException {

        //Setup Nama dan unit var
        JSONArray varArrayJson = jsonObject.getJSONArray("var");
        labelVar = varArrayJson.getJSONObject(0).getString("label");
        unitVar = varArrayJson.getJSONObject(0).getString("unit");
        defVar = varArrayJson.getJSONObject(0).getString("def");
        subjekVar = varArrayJson.getJSONObject(0).getString("subj");
        if (!unitVar.equals("")){
            unitVar = "(" + unitVar + ")";
        }
        note = varArrayJson.getJSONObject(0).getString("note");

        //setUpVerVar
        vertikalVariabels = new ArrayList<>();
        JSONArray verVarJsonArray = jsonObject.getJSONArray("vervar");
        for (int i = 0; i < verVarJsonArray.length(); i++){
            vertikalVariabels.add(new VertikalVariabel(verVarJsonArray.getJSONObject(i).getString("val"),
                    verVarJsonArray.getJSONObject(i).getString("label")));
        }

        //Setup Turvar
        turunanVertikalVariabels = new ArrayList<>();
        JSONArray turVarArrayJson = jsonObject.getJSONArray("turvar");
        for (int i = 0; i < turVarArrayJson.length(); i++){
            turunanVertikalVariabels.add(new TurunanVertikalVariabel(turVarArrayJson.getJSONObject(i).getString("val"),
                    turVarArrayJson.getJSONObject(i).getString("label")));
        }

        //Setup Tahun
        tahunVariabels = new ArrayList<>();
        JSONArray tahunArrayJson = jsonObject.getJSONArray("tahun");
        for (int i = 0; i < tahunArrayJson.length(); i++){
            tahunVariabels.add(new TahunVariabel(tahunArrayJson.getJSONObject(i).getString("val"),
                    tahunArrayJson.getJSONObject(i).getString("label")));
        }
        tahunVariabels = sortTahunList(tahunVariabels);

        //Setup Turunan Tahun
        turunanTahunVariabels = new ArrayList<>();
        JSONArray turTahunArrayJson = jsonObject.getJSONArray("turtahun");
        for (int i = 0; i < turTahunArrayJson.length(); i++){
            turunanTahunVariabels.add(new TurunanTahunVariabel(turTahunArrayJson.getJSONObject(i).getString("val"),
                    turTahunArrayJson.getJSONObject(i).getString("label")));
        }
        turunanTahunVariabels = sortTurTahunList(turunanTahunVariabels);
    }

    private AlertDialog createVervarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Series");

        String list[] = new String[vertikalVariabels.size()];
        for (int i = 0; i < vertikalVariabels.size(); i++){
            list[i] = vertikalVariabels.get(i).getLabel();
        }

        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                try {
                    setUpDataGrafik(jsonObject, vertikalVariabels.get(position).getId(), idTurvarGrafik);
                    setUpGrafikStat();
                    verVarSpinner.setText(vertikalVariabels.get(position).getLabel());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return builder.create();
    }

    private AlertDialog createTurVarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Series");

        String list[] = new String[turunanVertikalVariabels.size()];

        for (int i = 0; i < turunanVertikalVariabels.size(); i++){
            list[i] = turunanVertikalVariabels.get(i).getLabel();
        }

        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                try {
                    setUpDataGrafik(jsonObject, idVerVar, turunanVertikalVariabels.get(position).getId());
                    setUpGrafikStat();
                    turVarSpinnerGrafik.setText(turunanVertikalVariabels.get(position).getLabel());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return builder.create();
    }

    private String getPeriodeHeader(String tahunId, String turTahunId) {
        StringBuilder periode = new StringBuilder();
        for (int i = 0; i < turunanTahunVariabels.size(); i++){
            if (turTahunId.equals(turunanTahunVariabels.get(i).getId())){
                periode.append(turunanTahunVariabels.get(i).getLabel()).append(" ");
            }
        }
        for (int i = 0; i < tahunVariabels.size(); i++){
            if (tahunId.equals(tahunVariabels.get(i).getId())){
                periode.append(tahunVariabels.get(i).getLabel());
            }
        }

        return periode.toString();
    }

    private AlertDialog createFilterDialog() {
        View dialogView = View.inflate(this, R.layout.layout_filter_tabel_dialog, null);
        tahunSpinner = dialogView.findViewById(R.id.spinner_tahun);
        tahunSpinner.setText(labelTahun);

        tahunSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTahunPopup().show();
            }
        });
        turTahunSpinner = dialogView.findViewById(R.id.spinner_turtahun);
        if (turunanTahunVariabels.size()==1){
            turTahunSpinner.setVisibility(View.GONE);
        }else {
            turTahunSpinner.setText(labelTurTahun);
            turTahunSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTurTahunPopup().show();
                }
            });
        }
        turVarSpinnerTabel = dialogView.findViewById(R.id.spinner_turvar_tabel);
        if (turunanVertikalVariabels.size()==1){
            turVarSpinnerTabel.setVisibility(View.GONE);
        }else {
            turVarSpinnerTabel.setText(labelTurvarTabel);
            turVarSpinnerTabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTurvarPopupTabel().show();
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter Tabel").setView(dialogView).setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    setUpDataTabel(jsonObject, idTahun, idTurTahun, idTurvarTabel);
                    setUpTabelStat();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        return builder.create();
    }

    private AlertDialog createTurvarPopupTabel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String list[] = new String[turunanVertikalVariabels.size()];

        for (int i = 0; i < turunanVertikalVariabels.size(); i++){
            list[i] = turunanVertikalVariabels.get(i).getLabel();
        }

        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                idTurvarTabel = turunanVertikalVariabels.get(position).getId();
                turVarSpinnerTabel.setText(turunanVertikalVariabels.get(position).getLabel());
            }
        });

        return builder.create();
    }

    private AlertDialog createTahunPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String list[] = new String[tahunVariabels.size()];

        for (int i = 0; i < tahunVariabels.size(); i++){
            list[i] = tahunVariabels.get(i).getLabel();
        }

        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                idTahun = tahunVariabels.get(position).getId();
                tahunSpinner.setText(tahunVariabels.get(position).getLabel());
            }
        });

        return builder.create();
    }

    private AlertDialog createTurTahunPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String list[] = new String[turunanTahunVariabels.size()];

        for (int i = 0; i < turunanTahunVariabels.size(); i++){
            list[i] = turunanTahunVariabels.get(i).getLabel();
        }

        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                idTurTahun = turunanTahunVariabels.get(position).getId();
                turTahunSpinner.setText(turunanTahunVariabels.get(position).getLabel());
            }
        });

        return builder.create();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setVisibilityView(true, false, false);
                    return true;
                case R.id.navigation_dashboard:
                    setVisibilityView(false, true, false);
                    return true;
                case R.id.navigation_notifications:
                    setVisibilityView(false, false, true);
                    return true;
            }
            return false;
        }
    };

    private void setVisibilityView(boolean isGrafikVisible, boolean isTabelVisible, boolean isDeskripsiVisible){
        if (isGrafikVisible){
            grafikView.setVisibility(View.VISIBLE);
        }else {
            grafikView.setVisibility(View.GONE);
        }

        if (isTabelVisible){
            tabelView.setVisibility(View.VISIBLE);
        }else {
            tabelView.setVisibility(View.GONE);
        }

        if (isDeskripsiVisible){
            deskripsiView.setVisibility(View.VISIBLE);
        }else {
            deskripsiView.setVisibility(View.GONE);
        }
    }
}
