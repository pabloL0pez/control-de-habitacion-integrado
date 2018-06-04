package ar.com.so_unlam.chiapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager adminSensores; // Sensor Manager para obtener el servicio de sensores.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminSensores = (SensorManager) getSystemService(SENSOR_SERVICE);

        setContentView(R.layout.activity_main);
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

    private void funcionalidadAcelerometro(SensorEvent evento) {

    }

    private void funcionalidadOrientacion(SensorEvent evento) {

    }

    private void funcionalidadProximidad(SensorEvent evento) {

    }

}
