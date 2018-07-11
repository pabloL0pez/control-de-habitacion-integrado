package ar.com.so_unlam.chiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class EstadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado);
    }

    private void getLog(float distancia) {
        JSONObject jotaSon = new JSONObject();
        try {
            jotaSon.put("label", "luminosidad");
            jotaSon.put("value", "100");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jotaSon.length() > 0) {
            new AsyncTaskTest().execute(String.valueOf(jotaSon));
        }
    }
}
