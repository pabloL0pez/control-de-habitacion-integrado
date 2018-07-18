package ar.com.so_unlam.chiapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class AsyncTaskTest extends AsyncTask<String, Void, String> {
    protected String URL = "http://192.168.1.37/";

    private OnFetchFinishedListener listener;

    // the listener interface
    public interface OnFetchFinishedListener {
        void onFetchFinished(String result);
    }

    // getting a listener instance from the constructor
    public AsyncTaskTest(OnFetchFinishedListener listener) {
        this.listener = listener;
    }

    @Override protected void onPostExecute(String result) {
        Log.d("result", result);
        listener.onFetchFinished(result);
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("do asynccc", "true");
        String JsonResponse = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = null;
            try {
                url = new URL(this.URL + params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            urlConnection = (HttpURLConnection) url.openConnection();

            // is output buffer writter
            Log.d("method", params[1]);
            urlConnection.setRequestMethod(params[1]);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //set headers and method
            if(params[2] != null) {
                urlConnection.setDoOutput(true);
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(params[2]);
                // json data
                writer.close();
            }
            InputStream inputStream = urlConnection.getInputStream();
            //input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                return null;
            }
            JsonResponse = buffer.toString();

            return JsonResponse;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

}
