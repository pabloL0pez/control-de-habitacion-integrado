package ar.com.so_unlam.chiapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

class QRCodeAsyncTask extends AsyncTask<String, Void, Integer> {
    private Intent intent;
    private String valor;
    private Context context;

    public QRCodeAsyncTask(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    protected Integer doInBackground(String... valores) {
        try {
            valor = valores[0];
            URL urlObj = new URL("http://192.168.0.7:3000/access/" + valor);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            int code = urlConnection.getResponseCode();
            return code;
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == 200) {
            intent.putExtra("codigoQr", valor);
            intent.putExtra("habitacion", valor);
        }
        else if (result == 401) {
            intent.putExtra("codigoQr", "Código inválido");
        }
        else {
            intent.putExtra("codigoQr", "Error");
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}