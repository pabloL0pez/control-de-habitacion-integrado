package ar.com.so_unlam.chiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String jotaSonString;
        JSONObject jotaSon;
        JSONArray jotaSonArray;
        StringBuilder logsString;
        JSONObject unLog;
        TextView logs;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logs = findViewById(R.id.logs);
        logsString = new StringBuilder();

        String[] parametros = {"api/logs", "GET", null}; // Servirá el mismo método que usamos para hacer los put? Porque en los parámetros mandamos un JSON y ahora no hace falta porque es un GET
        jotaSonString = new AsyncTaskTest().doInBackground(parametros);
        jotaSonArray = new JSONArray(); // Asumo que como son varios logs, los guarda en un JSONSArray

        try {
            jotaSon = new JSONObject(jotaSonString);
            jotaSonArray = jotaSon.getJSONArray("LOGS"); // No sé si se llama así
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0 ; i < jotaSonArray.length() ; i++) {
            try {
                unLog = jotaSonArray.getJSONObject(i);
                logsString.append(unLog.getString("LOG")); // No sé si un log es simplemente un string, o si son strings separados como "id tarjeta" y "hora de acceso", por ejemplo
                logsString.append("\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        logs.setText(logsString);
    }
}
