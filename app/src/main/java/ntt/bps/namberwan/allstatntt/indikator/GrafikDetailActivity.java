package ntt.bps.namberwan.allstatntt.indikator;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ntt.bps.namberwan.allstatntt.R;

public class GrafikDetailActivity extends AppCompatActivity {

    public static final String JSON_STRING = "JSON STRING";
    public static final String ID_VAR = "ID VAR";
    public static final String INIT_VER_VAR = "INIT VER VAR";
    public static final String INIT_TUR_VAR = "INIT TUR VAR";

    private LineChart lineChart;
    private TextView judulGrafik;
    private Button verVarSpinner;
    private Button turVarSpinnerGrafik;

    private String idVerVar;
    private String labelVerVar;
    private String idTurvarGrafik;
    private String labelTurvarGrafik;

    private JSONObject jsonObject;

    private List<VertikalVariabel> vertikalVariabels;
    private List<TurunanVertikalVariabel> turunanVertikalVariabels;
    private List<TahunVariabel> tahunVariabels;
    private List<TurunanTahunVariabel> turunanTahunVariabels;

    private String idVar;
    private String labelVar;
    private String unitVar;

    private List<String> axisLabels;
    private List<Entry> grafikContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().hide();

        idVar = getIntent().getStringExtra(ID_VAR);

        setUpInitialView();

        try {
            jsonObject = new JSONObject(getIntent().getStringExtra(JSON_STRING));
            setUpVarAttribute(jsonObject);
            setUpDataGrafik(jsonObject, getIntent().getStringExtra(INIT_VER_VAR), getIntent().getStringExtra(INIT_TUR_VAR));
            setUpGrafikStat();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpInitialView() {
        lineChart = findViewById(R.id.chart);
        judulGrafik = findViewById(R.id.judul_grafik);
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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = labelVar + " " + labelVerVar + " " + labelTurvarGrafik + " detail";
                s = s.replaceAll("[[-+.^:,/$#]]","");
                if (lineChart.saveToGallery(s)){
                    Snackbar.make(view, "Grafik tersimpan di gallery", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void setUpDataGrafik(JSONObject jsonObject, String idVerVar, String idTurvar) throws JSONException {

        //Setup Data
        this.jsonObject = jsonObject;

        JSONObject dataContentJson = jsonObject.getJSONObject("datacontent");

        axisLabels = new ArrayList<>();
        grafikContents = new ArrayList<>();

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
        LineDataSet dataSet = new LineDataSet(grafikContents, "");
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("#.####");
                return decimalFormat.format(value);
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
        xAxis.setValueFormatter(new IndexAxisValueFormatter(axisLabels));
        xAxis.setAxisLineWidth(2f);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(getResources().getColor(R.color.material_drawer_secondary_text));
        xAxis.setLabelRotationAngle(40f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(true);
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(getResources().getColor(R.color.material_drawer_secondary_text));

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
                + axisLabels.get(0)+" - "+ axisLabels.get(axisLabels.size()-1) + " " + unitVar;
        judulGrafik.setText(s);

        //Setup Spinner dan button
        verVarSpinner.setText(labelVerVar);
        turVarSpinnerGrafik.setText(labelTurvarGrafik);
        if (turunanVertikalVariabels.size()==1){
            turVarSpinnerGrafik.setVisibility(View.GONE);
        }
    }

    private void setUpVarAttribute(JSONObject jsonObject) throws JSONException {

        //Setup Nama dan unit var
        JSONArray varArrayJson = jsonObject.getJSONArray("var");
        labelVar = varArrayJson.getJSONObject(0).getString("label");
        unitVar = varArrayJson.getJSONObject(0).getString("unit");
        if (!unitVar.equals("")){
            unitVar = "(" + unitVar + ")";
        }

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
