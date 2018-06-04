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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener{

    private SensorManager adminSensores; //
    private Button botonEstado;
    private Button botonLog;
    private Switch switchLuz;

    // Borrarlas despues
    private TextView orientacionEnX;
    private TextView orientacionEnY;
    private TextView orientacionEnZ;

    // Variables para el acelerómetro.
    private static final int UMBRAL_SACUDIDA = 70; // Velocidad mínima para ser considerada sacudida (m/s).
    private static final int UMBRAL_ACTUALIZACION = 150; // Intervalo de tiempo para el cual se va a chequear una sacudida (mseg).
    private long tiempoUltimaActualizacion;
    private float ultimoX;
    private float ultimoY;
    private float ultimoZ;

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

        switchLuz = findViewById(R.id.switchLuz);

        orientacionEnX = findViewById(R.id.orientacionX);
        orientacionEnY = findViewById(R.id.orientacionY);
        orientacionEnZ = findViewById(R.id.orientacionZ);

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
    Este método define que hace la aplicación al activarse el acelerómetro (en este caso un shake).
     */
    private void funcionalidadAcelerometro(SensorEvent evento) {
        float x, y, z;
        double moduloAceleracionAnterior, moduloAceleracionActual, velocidad;
        long tiempoActual = System.currentTimeMillis();
        long diferenciaDeTiempo = tiempoActual - tiempoUltimaActualizacion;

        if (diferenciaDeTiempo > UMBRAL_ACTUALIZACION) {

            tiempoUltimaActualizacion = tiempoActual;

            x = evento.values[0];
            y = evento.values[1];
            z = evento.values[2];

            moduloAceleracionActual = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            moduloAceleracionAnterior = Math.sqrt(Math.pow(this.ultimoX, 2) + Math.pow(this.ultimoY, 2) + Math.pow(this.ultimoZ, 2));

            velocidad = Math.abs(moduloAceleracionActual - moduloAceleracionAnterior) / diferenciaDeTiempo * 1000;

            if (velocidad > UMBRAL_SACUDIDA) {
                desbloquearPuerta(velocidad);
            }
            this.ultimoX = x;
            this.ultimoY = y;
            this.ultimoZ = z;
        }
    }

    /*
    Este método define que hace la aplicación al activarse el sensor de orientación.
     */
    private void funcionalidadOrientacion(SensorEvent evento) {
        if (switchLuz.isChecked()) {
            orientacionEnX.setText("" + evento.values[0]);
            orientacionEnY.setText("" + evento.values[1]);
            orientacionEnZ.setText("" + evento.values[2]);
        }
    }

    /*
    Este método define que hace la aplicación al activarse el sensor de proximidad.
     */
    private void funcionalidadProximidad(SensorEvent evento) {
        if (evento.values[0] > 1) {
            encenderLuces(evento.values[0]);
        }
    }

    /*
    Este método desbloquea la puerta de la habitación.
     */
    private void desbloquearPuerta(double velocidad) {
        Toast.makeText(this, "Se detectó una sacudida con una velocidad de: " + velocidad, Toast.LENGTH_SHORT).show();
    }

    /*
    Este método enciende las luces de la habitación.
     */
    private void encenderLuces(float distancia) {
        Toast.makeText(this, "Sensor de proximidad: " + distancia, Toast.LENGTH_SHORT).show();
    }
}

    /*
     x = evento.values[0];
     y = evento.values[1];
     z = evento.values[2];

     velocidad = Math.abs((x + y + z) - this.ultimoX - this.ultimoY - this.ultimoZ) / diferenciaDeTiempo * 10000;

      Log.d("Acelerómetro", "Se detectó una sacudida con una velocidad de: " + velocidad);
     */

