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
//        try {
//            jotaSon.put("value", String.valueOf());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String[] parameters = {"api/configs/5b3c48f8caaafe0bf38279c6", "GET"};

        if (jotaSon.length() > 0) {
            AsyncTask asyncTask = new AsyncTaskTest().execute(parameters);
        }
    }
}
