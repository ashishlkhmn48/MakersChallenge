package com.ashishlakhmani.event.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashishlakhmani.event.R;
import com.ashishlakhmani.event.activities.MainActivity;

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


public class LoginBackground extends AsyncTask<String, Void, String> {

    private Context context;
    private Activity activity;
    private ProgressBar progressBar;
    private Button loginButton;
    private HashMap<String, String> map = new HashMap<>();

    public LoginBackground(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressBar = (ProgressBar) activity.findViewById(R.id.login_progressbar);
        loginButton = (Button) activity.findViewById(R.id.login_button);
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://halted-certificates.000webhostapp.com/php_files/login.php";

        try {
            map.put("username", params[0].toLowerCase());
            map.put("password", params[1].toLowerCase());

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
        if (!s.equalsIgnoreCase("failed")) {
            Intent intent = new Intent(context, MainActivity.class);
            MainActivity.username = map.get("username").toUpperCase();
            Toast.makeText(context, "Login Success.", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", map.get("username").toUpperCase());
            editor.putString("amount", s);
            editor.apply();

            progressBar.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);

            activity.startActivity(intent);
            activity.finish();
        } else if (s.equalsIgnoreCase("failed")) {
            Toast.makeText(context, "Login Failed.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
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
