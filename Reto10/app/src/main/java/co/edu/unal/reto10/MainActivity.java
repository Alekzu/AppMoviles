package co.edu.unal.reto10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Spinner monthSpinner;
    private ArrayList<String> monthArray;
    private ArrayAdapter<String> monthAdapter;
    private Spinner locationSpinner;
    private ArrayList<String> locationArray;
    private ArrayAdapter<String> locationAdapter;
    private ListView list;

    private ArrayAdapter<String> codAdapter;
    private Context context = this;
    private Spinner mesSpinner;
    private ArrayList<String> mesArray;
    private ArrayAdapter<String> mesAdapter;
    private Spinner ubicacionSpinner;
    private ArrayList<String> ubicacionArray;
    private ArrayAdapter<String> ubicacionAdapter;
    //private ListView list;
    private ArrayList<String> outpost;
    //private Context context = this;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mesArray = new ArrayList<>();
        ubicacionArray = new ArrayList<>();
        outpost = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        //String url = "https://www.datos.gov.co/resource/xdk5-pm3f.json?$select=distinct%20departamento&$order=departamento%20ASC";
        String url = "https://www.datos.gov.co/resource/drfm-i22d.json?$select=distinct%20mes&$order=mes%20ASC";
        mesSpinner = (Spinner) findViewById(R.id.meses);
        ubicacionSpinner = (Spinner) findViewById(R.id.ubicacion);
        list = findViewById(R.id.list);
        JsonArrayRequest departamentos = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject tmp = null;
                            try {
                                tmp = response.getJSONObject(i);
                                mesArray.add(tmp.getString("mes"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mesArray);
                        mesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mesSpinner.setAdapter(mesAdapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        mesSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ubicacionArray.clear();
                String tmp = (String) parent.getItemAtPosition(pos);
                //String url = "https://www.datos.gov.co/resource/xdk5-pm3f.json?$select=distinct%20municipio&departamento="+ tmp + "&$order=municipio%20ASC";
                String url = "https://www.datos.gov.co/resource/drfm-i22d.json?$select=distinct%20ubicaci_n&$order=ubicaci_n%20ASC";
                JsonArrayRequest municipios = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        ubicacionArray.add(tmp.getString("ubicaci_n"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ubicacionAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ubicacionArray);
                                ubicacionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ubicacionSpinner.setAdapter(ubicacionAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                queue.add(municipios);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ubicacionSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                outpost.clear();
                final String tmp = (String) parent.getItemAtPosition(pos);
                final String tmpS = mesSpinner.getSelectedItem().toString();
                //String url = "https://www.datos.gov.co/resource/drfm-i22d.json?ubicaci_n=" + tmp;
                String tmpURL = "Parque%20Bachue";
                try {
                    tmpURL = URLEncoder.encode(tmp, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String url = "https://www.datos.gov.co/resource/drfm-i22d.json?ubicaci_n="+tmpURL+"&mes="+ tmpS;
                JsonArrayRequest codes = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        String tmp2 = "Año: " + tmp.getString("a_o") + "\n";
                                        tmp2 += "Mes: " + tmp.getString("mes") + "\n";
                                        tmp2 += "Nombre de la red: " + tmp.getString("ssid") + "\n";
                                        tmp2 += "Ubicación: " + tmp.getString("ubicaci_n") + "\n";
                                        tmp2 += "Localización: " + tmp.getString("localizaci_n") + "\n";
                                        tmp2 +="Nº de conexiones promedio en 24 horas : " + tmp.getString("n_de_conexiones_promedio") + "\n";
                                        tmp2 += "Consumo de ancho de banda promedio por día (GB): " + tmp.getString("consumo_de_ancho_de_banda") + "\n";
                                        outpost.add(tmp2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                codAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, outpost);
                                list.setAdapter(codAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", url);
                            }
                        });
                queue.add(codes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        queue.add(departamentos);

    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }*/
}