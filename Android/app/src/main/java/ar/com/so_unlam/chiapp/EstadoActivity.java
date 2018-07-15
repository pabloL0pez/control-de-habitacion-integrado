package ar.com.so_unlam.chiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class EstadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String jotaSonString;
        JSONObject jotaSon;
        TextView luminosidad;
        String valorLuminosidad = "-";

        luminosidad = findViewById(R.id.porcentajeLuminosidad);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado);

        String[] parametros = {"api/configs/5b3c48f8caaafe0bf38279c6", "GET", null}; // Servirá el mismo método que usamos para hacer los put? Porque en los parámetros mandamos un JSON y ahora no hace falta porque es un GET
        jotaSonString = new AsyncTaskTest().doInBackground(parametros);

        try {
            jotaSon = new JSONObject(jotaSonString);
            valorLuminosidad = jotaSon.getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        luminosidad.setText(valorLuminosidad);
    }
}
