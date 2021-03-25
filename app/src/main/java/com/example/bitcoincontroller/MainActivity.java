package com.example.bitcoincontroller;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private static final String XBTEUR_ENDPOINT = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static final String XBTUSD_ENDPOINT = "https://api.kraken.com/0/public/Ticker?pair=XBTUSD";
    TextView eurText = null;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        eurText = (TextView) findViewById(R.id.text_eur);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_load) {
            Log.i(TAG, "Llamada a la API con la información");
            //TODO:1- Add ProgressBar to show here
            load();
        }
        return false;
    }

    private void load() {
        Log.d(TAG, "Lanzada la petición: ");
        Log.d(TAG, XBTEUR_ENDPOINT + " " + XBTUSD_ENDPOINT);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest eurRequest = new JsonObjectRequest(
                Request.Method.GET, XBTEUR_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonReader(response);
                Log.d(TAG, "Llega la respuesta, OK");
                Log.d(TAG, response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Llega la respuesta, KO");
                Log.e(TAG, error.toString());
            }

        });

        queue.add(eurRequest);
    }

    private void jsonReader(JSONObject response) {
        try {
            JSONObject jResult = response.getJSONObject("result");
            JSONObject jPares = jResult.getJSONObject("XXBTZEUR");
            JSONArray jParEur = jPares.getJSONArray("c");
            String xbt = jParEur.getString(0).trim();
            String xbtEur = trimmer(xbt);
            Log.i(TAG, "Extraída la información para cargarla");
            eurText.setText(xbtEur + " €");

        } catch (Exception err) {
            eurText.setText(R.string.load_error);
            Log.e(TAG, "Se ha producido un error al leer el JSON");
        }

    }

    private String trimmer(String xbt) {
        return xbt.substring(0, xbt.length() - 4);
    }

}