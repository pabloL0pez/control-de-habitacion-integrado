package ar.com.so_unlam.chiapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class CodigoQrActivity extends AppCompatActivity {

    private static final int PEDIR_PERMISOS_CAMARA = 201;

    private SurfaceView vistaCamara;
    private TextView codigoQr;
    private CameraSource camara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);

        vistaCamara = findViewById(R.id.vistaCamara);
        codigoQr = findViewById(R.id.codigoQr);

        BarcodeDetector detectorCodigo = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        camara = new CameraSource.Builder(this, detectorCodigo).setRequestedPreviewSize(400, 400).build();
        // vistaCamara.getLayoutParams().width
        // vistaCamara.getLayoutParams().height
        vistaCamara.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(CodigoQrActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        camara.start(vistaCamara.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(CodigoQrActivity.this, new String[]{Manifest.permission.CAMERA}, PEDIR_PERMISOS_CAMARA);
                    }
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Nada de nada
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camara.stop();
            }
        });

        detectorCodigo.setProcessor(new Detector.Processor<Barcode>() {
            @Override public void release() {
                Toast.makeText(getApplicationContext(), "El escáner de códigos de barra se detuvo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detecciones) {
                SparseArray<Barcode> codigos = detecciones.getDetectedItems();
                if (codigos.size() != 0) {

                    codigoQr.post(() -> {
                        String valor = codigos.valueAt(0).displayValue;
                        if (valor != null) {
                            codigoQr.removeCallbacks(null);
                            codigoQr.setText(valor);
                        } else {
                            codigoQr.setText(R.string.default_qr_text);
                        }
                    });
                }
            }
        });
    }
}
