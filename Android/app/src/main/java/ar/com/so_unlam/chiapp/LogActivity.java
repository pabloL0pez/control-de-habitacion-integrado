package ar.com.so_unlam.chiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

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

        String[] parametros = {"api/logs?populate=card", "GET", null}; // Servirá el mismo método que usamos para hacer los put? Porque en los parámetros mandamos un JSON y ahora no hace falta porque es un GET
        new AsyncTaskTest(new AsyncTaskTest.OnFetchFinishedListener() {
            @Override
            public void onFetchFinished(String result) {
                try {
                    JSONArray obj = new JSONArray(result);
                    Log.d("My App", obj.toString());
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject cardDataObj = obj.getJSONObject(i);
                        Log.d("res", cardDataObj.getJSONObject("card").get("card").toString());
                        logsString.append("rfid: " + cardDataObj.getJSONObject("card").get("card").toString() + " Fecha: " + cardDataObj.getJSONObject("card").get("createdAt").toString() + "\n");

                    }

                    logs.setText(logsString);


//                    Iterator<String> iter = obj.keys();
//                    while (iter.hasNext()) {
//                        String key = iter.next();
//                        try {
//                            Object value = obj.get(key);
//                            Log.d("value", value.toString());
//                        } catch (JSONException e) {
//                            // Something went wrong!
//                        }
//                    }

                } catch (Throwable t) {
                    Log.e("Error", "No se pudo parsear el json");
                }
            }
        }).execute(parametros);

//        jotaSonArray = new JSONArray(); // Asumo que como son varios logs, los guarda en un JSONSArray
//
//        try {
//            jotaSon = new JSONObject(jotaSonString);
//            jotaSonArray = jotaSon.getJSONArray("LOGS"); // No sé si se llama así
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        for(int i = 0 ; i < jotaSonArray.length() ; i++) {
//            try {
//                unLog = jotaSonArray.getJSONObject(i);
//                logsString.append(unLog.getString("LOG")); // No sé si un log es simplemente un string, o si son strings separados como "id tarjeta" y "hora de acceso", por ejemplo
//                logsString.append("\n");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        logs.setText(logsString);
    }
}
