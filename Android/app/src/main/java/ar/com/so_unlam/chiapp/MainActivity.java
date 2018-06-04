package ar.com.so_unlam.chiapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener{

    private SensorManager adminSensores; //
    private Button botonEstado;
    private Button botonLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("CHIApp");

        adminSensores = (SensorManager) getSystemService(SENSOR_SERVICE);

        botonEstado = findViewById(R.id.botonEstado);
        botonLog = findViewById(R.id.botonLog);
        botonEstado.setOnClickListener(this);
        botonLog.setOnClickListener(this);
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
        adminSensores.registerListener((SensorEventListener) this, adminSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        adminSensores.registerListener((SensorEventListener) this, adminSensores.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        adminSensores.registerListener((SensorEventListener) this, adminSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
    Este método lo que hace es desactivar los listener de los 3 sensores que vamos a usar: acelerómetro, de movimiento y de proximidad.
     */
    private void pararSensores() {
        adminSensores.unregisterListener((SensorEventListener) this, adminSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        adminSensores.unregisterListener((SensorEventListener) this, adminSensores.getDefaultSensor(Sensor.TYPE_ORIENTATION));
        adminSensores.unregisterListener((SensorEventListener) this, adminSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    /*
    Este método define que hace la aplicación al activarse el acelerómetro.
     */
    private void funcionalidadAcelerometro(SensorEvent evento) {

    }

    /*
    Este método define que hace la aplicación al activarse el sensor de orientación.
     */
    private void funcionalidadOrientacion(SensorEvent evento) {

    }

    /*
    Este método define que hace la aplicación al activarse el sensor de proximidad.
     */
    private void funcionalidadProximidad(SensorEvent evento) {

    }
}
