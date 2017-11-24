package com.ashishlakhmani.event.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashishlakhmani.event.R;
import com.ashishlakhmani.event.adapters.BillAdapter;

import org.json.JSONArray;
import org.json.JSONException;

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

public class BillBackground extends AsyncTask<String, Void, String> {

    private Context context;
    private View view;
    private ProgressBar progressBar;
    private HashMap<String, String> map = new HashMap<>();

    public BillBackground(Context context, View view) {
        this.context = context;
        this.view = view;
        progressBar = view.findViewById(R.id.bill_progressBar);
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://halted-certificates.000webhostapp.com/php_files/bill.php";
        try {
            map.put("username", params[0].toLowerCase());

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
    protected void onPostExecute(String result) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bill_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        JSONArray arr = null;
        BillAdapter billAdapter = null;
        try {
            arr = new JSONArray(result);
            if (arr.length() != 0) {
                billAdapter = new BillAdapter(context, arr);
            } else {
                Toast.makeText(context, "No Transactions Done Yet.!!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(billAdapter);
        progressBar.setVisibility(View.INVISIBLE);
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
