package ar.com.so_unlam.chiapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener{

    private SensorManager adminSensores; //
    private Switch switchLuz;

    // Variables para el acelerómetro.
    private static final int UMBRAL_SACUDIDA = 200; // Velocidad mínima para ser considerada sacudida (m/s).
    private static final int UMBRAL_ACTUALIZACION = 500; // Intervalo de tiempo para el cual se va a chequear una sacudida (mseg).
    private long tiempoUltimaActualizacion;
    private float ultimoX;
    private float ultimoY;
    private float ultimoZ;
    private Boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String habitacion;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("CHIApp");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView valorCodigoQr = findViewById(R.id.valorCodigoQr);
            valorCodigoQr.setText(extras.getString("codigoQr"));
            habitacion = extras.getString("habitacion");
        }

        adminSensores = (SensorManager) getSystemService(SENSOR_SERVICE);

        Button botonEstado = findViewById(R.id.botonEstado);
        Button botonLog = findViewById(R.id.botonLog);
        Button botonQr = findViewById(R.id.botonQR);

        botonEstado.setOnClickListener(this);
        botonLog.setOnClickListener(this);
        botonQr.setOnClickListener(this);

        switchLuz = findViewById(R.id.switchLuz);
    }

    @Override
    protected void onRestart() {
        inicializarSensores();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarSensores();
    }

    @Override
    protected void onPause() {
        pararSensores();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        pararSensores();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        pararSensores();
        super.onStop();
    }

    /*
    Este método es el que se encarga de iniciar las activitys correspondientes al botón que haya sido presionado.
     */
    @Override
    public void onClick(View vista) {
        switch(vista.getId()) {

            case R.id.botonEstado:
                Intent intentEstado = new Intent(this, EstadoActivity.class);
                startActivity(intentEstado);
                break;

            case R.id.botonLog:
                Intent intentLog = new Intent(this, LogActivity.class);
                startActivity(intentLog);
                break;

            case R.id.botonQR:
                Intent intentCodigoQr = new Intent(this, CodigoQrActivity.class);
                startActivity(intentCodigoQr);
                break;
        }
    }

    /*
    No lo vamos a usar pero hay que ponerlo ya que se implementa la interfaz SensorEventListener.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
    Este método es el que escucha los cambios en los sensores.
     */
    @Override
    public void onSensorChanged(SensorEvent evento) {
        synchronized (this) {
            int tipoEvento = evento.sensor.getType();
            switch(tipoEvento) {

                case Sensor.TYPE_ACCELEROMETER:
                    funcionalidadAcelerometro(evento);
                    break;

                case Sensor.TYPE_ORIENTATION:
                    funcionalidadOrientacion(evento);
                    break;

                case Sensor.TYPE_PROXIMITY:
                    funcionalidadProximidad(evento);
                    break;
            }
        }
    }

    /*
    Este método lo que hace es inicializar los listener de los 3 sensores que vamos a usar: acelerómetro, de movimiento y de proximidad.
     */
    private void inicializarSensores() {
        adminSensores.registerListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        adminSensores.registerListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        adminSensores.registerListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
    Este método lo que hace es desactivar los listener de los 3 sensores que vamos a usar: acelerómetro, de movimiento y de proximidad.
     */
    private void pararSensores() {
        adminSensores.unregisterListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        adminSensores.unregisterListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_ORIENTATION));
        adminSensores.unregisterListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    /*
    Este método calcular el valor del acelerometro para detectar si hubo o no un shake, y en base a esto desbloquea o no la puerta.
     */
    private void funcionalidadAcelerometro(SensorEvent evento) {
        float x, y, z;
        double aceleracionAnterior, aceleracionActual, velocidad;
        long tiempoActual = System.currentTimeMillis();
        long diferenciaDeTiempo = tiempoActual - tiempoUltimaActualizacion;

        if (diferenciaDeTiempo > UMBRAL_ACTUALIZACION) {

            tiempoUltimaActualizacion = tiempoActual;

            x = evento.values[0];
            y = evento.values[1];
            z = evento.values[2];

            aceleracionActual = x + y + z;
            aceleracionAnterior = this.ultimoX + this.ultimoY + this.ultimoZ;

            velocidad = Math.abs(aceleracionActual - aceleracionAnterior) / diferenciaDeTiempo * 10000;

            if (velocidad > UMBRAL_SACUDIDA) {
                desbloquearPuerta(velocidad);
            }
            this.ultimoX = x;
            this.ultimoY = y;
            this.ultimoZ = z;
        }
    }

    /*
    Este método verifica el angulo de orientación en el eje X del teléfono y en base a esto define el porcentaje de intensidad que va a tener la luz.
     */
    private void funcionalidadOrientacion(SensorEvent evento) {
        float anguloEnY = Math.abs(evento.values[1]);
        float valorMaximo = (float)-(Math.PI)*1/2;

        if (switchLuz.isChecked()) {
            if(anguloEnY >= 0 && anguloEnY <= 100 && evento.values[1] < 0) {
                modificarIntensidadLuz(Math.round(anguloEnY));
            } else if(anguloEnY > 100) {
                modificarIntensidadLuz(100);
            } else if(evento.values[1] > 0) {
                modificarIntensidadLuz(0);

            }

        }
    }

    /*
    Este método define que hace la aplicación al activarse el sensor de proximidad.
     */
    private void funcionalidadProximidad(SensorEvent evento) {
        if (!this.isFirstTime && evento.values[0] < 30) {
            toggleLuz();
        }

        this.isFirstTime = false;
    }

    /*
    Este método desbloquea la puerta de la habitación.
     */
    private void desbloquearPuerta(double velocidad) {
        Toast.makeText(this, "Se detectó una sacudida con una velocidad de: " + velocidad, Toast.LENGTH_SHORT).show();
        JSONObject jotaSon = new JSONObject();
        try {
            jotaSon.put("value", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] parameters = {"api/configs/5b3d5abced34b412053c07c1", "PUT", String.valueOf(jotaSon)};

        if (jotaSon.length() > 0) {
            new AsyncTaskTest(new AsyncTaskTest.OnFetchFinishedListener() {
                @Override
                public void onFetchFinished(String result) {
                    Log.d("result after", result);
                }
            }).execute(parameters);
        }
    }

    /*
    Este método enciende las luces de la habitación.
     */
    private void prenderLuz() {
        JSONObject jotaSon = new JSONObject();
        try {
            jotaSon.put("value", "100");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] parameters = {"toggleLuz", "PUT", String.valueOf(jotaSon)};

        if (jotaSon.length() > 0) {
            new AsyncTaskTest(new AsyncTaskTest.OnFetchFinishedListener() {
                @Override
                public void onFetchFinished(String result) {
                    Log.d("result after", result);
                }
            }).execute(parameters);
        }
    }

    private void modificarIntensidadLuz(Integer intensidad) {
        JSONObject jotaSon = new JSONObject();
        try {
            jotaSon.put("value", String.valueOf(intensidad));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] parameters = {"api/configs/5b3c48f8caaafe0bf38279c6", "PUT", String.valueOf(jotaSon)};

        if (jotaSon.length() > 0) {
            new AsyncTaskTest(new AsyncTaskTest.OnFetchFinishedListener() {
                @Override
                public void onFetchFinished(String result) {
                    Log.d("result after", result);
                }
            }).execute(parameters);
        }
    }
    private void toggleLuz() {
        JSONObject jotaSon = new JSONObject();
        try {
            jotaSon.put("value", "100");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] parameters = {"toggleLuz", "PUT", String.valueOf(jotaSon)};

        if (jotaSon.length() > 0) {
            new AsyncTaskTest(new AsyncTaskTest.OnFetchFinishedListener() {
                @Override
                public void onFetchFinished(String result) {
                    Log.d("result after", result);
                }
            }).execute(parameters);
        }
    }
}

