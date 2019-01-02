package ntt.bps.namberwan.allstatntt.indikator;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.evrencoskun.tableview.TableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ntt.bps.namberwan.allstatntt.R;

public class TabelDetailActivity extends AppCompatActivity {

    public static final String JSON_STRING = "JSON STRING";
    public static final String ID_VAR = "ID VAR";

    private JSONObject jsonObject;

    private TableView tableView;
    private Button turVarSpinner;
    private View control;

    private String idVar;
    private String labelVar;
    private String unitVar;

    private String idTurVar;
    private String labelTurVar;

    private List<VertikalVariabel> vertikalVariabels;
    private List<TurunanVertikalVariabel> turunanVertikalVariabels;
    private List<TahunVariabel> tahunVariabels;
    private List<TurunanTahunVariabel> turunanTahunVariabels;

    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabel_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        idVar = getIntent().getStringExtra(ID_VAR);

        setUpInitialView();

        try {
            jsonObject = new JSONObject(getIntent().getStringExtra(JSON_STRING));
            setUpVarAttribute(jsonObject);
            setUpDataTabel(turunanVertikalVariabels.get(turunanVertikalVariabels.size()-1).getId());
            setUpTabel();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void setUpTabel() {
        TabelViewAdapter adapter = new TabelViewAdapter(this);
        tableView.setAdapter(adapter);
        adapter.setAllItems(columnHeaderList, rowHeaderList, cellList);
        String s = "Tabel " + labelVar + " " + columnHeaderList.get(columnHeaderList.size()-1).getData()
                +" - " + columnHeaderList.get(0).getData() + " " + unitVar;
        setTitle(s);

        if (turunanVertikalVariabels.size()==1){
            control.setVisibility(View.GONE);
        }
        turVarSpinner.setText(labelTurVar);
    }

    private void setUpDataTabel(String idTurVar) throws JSONException {

        //setup data
        columnHeaderList = new ArrayList<>();
        rowHeaderList = new ArrayList<>();
        cellList = new ArrayList<>();

        JSONObject dataContentJson = jsonObject.getJSONObject("datacontent");

        for (int i = tahunVariabels.size()-1; i >= 0; i--){
            String idTahun = tahunVariabels.get(i).getId();
            for (int j = turunanTahunVariabels.size()-1; j >= 0; j--){
                String idTurTahun = turunanTahunVariabels.get(j).getId();
                List<Cell> cellsByColumn = new ArrayList<>();
                for (int k = 0; k < vertikalVariabels.size(); k++){
                    String idVerVar = vertikalVariabels.get(k).getId();
                    String idDataContent = idVerVar + idVar + idTurVar + idTahun + idTurTahun;
                    if (dataContentJson.has(idDataContent)){
                        cellsByColumn.add(new Cell(idDataContent, dataContentJson.getString(idDataContent)));
                    }else {
                        cellsByColumn.add(new Cell(idDataContent, "-"));
                    }
                }
                boolean isNotEmpty = isNotEmptyList(cellsByColumn);
                if (isNotEmpty){
                    cellList.add(cellsByColumn);
                    columnHeaderList.add(new ColumnHeader(
                            turunanTahunVariabels.get(j).getId() + " " + tahunVariabels.get(i).getId(),
                            turunanTahunVariabels.get(j).getLabel() + " " + tahunVariabels.get(i).getLabel()
                    ));
                }
            }
        }
        cellList = transpose(cellList);

        for (int i = 0; i < vertikalVariabels.size(); i++){
            rowHeaderList.add(new RowHeader(vertikalVariabels.get(i).getId(), vertikalVariabels.get(i).getLabel()));
        }

        //setup turunan var
        this.idTurVar = idTurVar;
        for (int i = 0; i < turunanVertikalVariabels.size(); i++){
            if (idTurVar.equals(turunanVertikalVariabels.get(i).getId())){
                this.labelTurVar = turunanVertikalVariabels.get(i).getLabel();
            }
        }
    }

    private boolean isNotEmptyList(List<Cell> cellsByColumn) {
        boolean isNotEmpty = false;
        for (int i = 0; i < cellsByColumn.size(); i++){
            if (!cellsByColumn.get(i).getData().equals("-")){
                isNotEmpty = true;
                break;
            }
        }
        return isNotEmpty;
    }

    private List<List<Cell>> transpose(List<List<Cell>> cellList) {

        List<List<Cell>> transpose = new ArrayList<>();

        for (int i = 0; i < cellList.get(0).size(); i++){
            List<Cell> listCell = new ArrayList<>();
            for (int j = 0; j < cellList.size(); j++){
                Cell c = cellList.get(j).get(i);
                listCell.add(c);
            }
            transpose.add(listCell);
        }
        return transpose;
    }

    private void setUpInitialView() {
        tableView = findViewById(R.id.tabel);
        turVarSpinner = findViewById(R.id.spinner_turvar);
        control = findViewById(R.id.control);
        turVarSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialogTurVar = createTurVarDialog();
                dialogTurVar.show();
            }
        });
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_tabel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_rotate){
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            return true;
        }/*if (id == R.id.action_filter){
            final AlertDialog dialogTurVar = createTurVarDialog();
            dialogTurVar.show();
        }*/
        return super.onOptionsItemSelected(item);
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
                    setUpDataTabel(turunanVertikalVariabels.get(position).getId());
                    setUpTabel();
                    turVarSpinner.setText(turunanVertikalVariabels.get(position).getLabel());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return builder.create();
    }

}
