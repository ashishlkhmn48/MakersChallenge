package com.ashishlakhmani.event.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UpdateServer extends AsyncTask<String, Void, String> {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private Button button;
    private View view;

    public UpdateServer(Context context, SwipeRefreshLayout swipeRefreshLayout, ProgressBar progressBar, Button button, View view) {
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.progressBar = progressBar;
        this.button = button;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", params[0]);
        map.put("productName", params[1]);
        map.put("qtySelected", params[2]);
        map.put("totalPrice", params[3]);
        map.put("max",params[4]);

        String login_url = "https://halted-certificates.000webhostapp.com/php_files/check.php";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(getPostDataString(map));
            outputStreamWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder sb = new StringBuilder();
            int letter = inputStreamReader.read();
            while (letter != -1) {
                sb.append((char) letter);
                letter = inputStreamReader.read();
            }
            inputStreamReader.close();
            httpURLConnection.disconnect();
            return sb.toString();

        } catch (Exception e) {
            return "Connection Error! Please make sure that there is Internet Connection.";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        progressBar.setVisibility(View.INVISIBLE);
        button.setVisibility(View.VISIBLE);
        //BudgetBackground amountBackground = new BudgetBackground(context, view, swipeRefreshLayout);
        //amountBackground.execute(MainActivity.username);
        //ProductBackground productBackground = new ProductBackground(context, view, swipeRefreshLayout);
        //productBackground.execute();
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
