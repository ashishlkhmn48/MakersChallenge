package com.ashishlakhmani.event.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishlakhmani.event.R;

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

/**
 * Created by ALakhmani on 31-10-2017.
 */

public class BudgetBillBakground extends AsyncTask<String, Void, String> {

    private Context context;
    private View view;

    public BudgetBillBakground(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected String doInBackground(String... params) {
        HashMap<String, String> map = new HashMap<>();
        String login_url = "https://halted-certificates.000webhostapp.com/php_files/amount_details.php";
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
    protected void onPostExecute(String s) {
        TextView textView = view.findViewById(R.id.amount);
        textView.setText(s);
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("amount", s);
        editor.apply();
        Toast.makeText(context, "Budget Left : " + s, Toast.LENGTH_SHORT).show();
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
