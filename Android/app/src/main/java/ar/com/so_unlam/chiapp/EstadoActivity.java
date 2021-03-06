package ar.com.so_unlam.chiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class EstadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String jotaSonString;
        JSONObject jotaSon;
        TextView luminosidad;
        TextView cuantaLuz;
        TextView hayMovimiento;
        String valorLuminosidad = "-";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado);
        luminosidad = findViewById(R.id.etiquetaLuz);
        cuantaLuz = findViewById(R.id.luzEnHabitacion);
        hayMovimiento = findViewById(R.id.hayMovimiento);


        String[] parametros = {"datosGenerales", "GET", null}; // Servirá el mismo método que usamos para hacer los put? Porque en los parámetros mandamos un JSON y ahora no hace falta porque es un GET
        new AsyncTaskTest(new AsyncTaskTest.OnFetchFinishedListener() {
            @Override
            public void onFetchFinished(String result) {
                Log.d("result after", result);
                try {
                    JSONObject luminosidadObj = new JSONObject(result);
                    luminosidad.setText("luz prendida al " + luminosidadObj.getString("luz") + "%");
                    cuantaLuz.setText("Valor de movimiento (pir)" + luminosidadObj.getString("hayMovimiento"));
                    hayMovimiento.setText("Valor de luz (ldr)" + luminosidadObj.getString("valorLdr"));
                } catch (Throwable t) {
                    Log.d("Error", "No se pudo parsear el json");
                }
            }
        }).execute(parametros);
    }
}
