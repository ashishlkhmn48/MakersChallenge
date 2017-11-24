package com.ashishlakhmani.event.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.ashishlakhmani.event.R;
import com.ashishlakhmani.event.adapters.ShoppingAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductBackground extends AsyncTask<Void, Void, String> {

    private Context context;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public ProductBackground(Context context, View view, SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.view = view;
        this.swipeRefreshLayout = swipeRefreshLayout;
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }


    @Override
    protected String doInBackground(Void... params) {

        String login_url = "https://halted-certificates.000webhostapp.com/php_files/product_details.php";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
            StringBuilder sb = new StringBuilder("");
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return sb.toString();

        } catch (Exception e) {
            return "Connection Problem";
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        //Recycler View initialization
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        JSONArray arr = null;
        ShoppingAdapter shoppingAdapter = null;
        try {
            arr = new JSONArray(result);
            shoppingAdapter = new ShoppingAdapter(arr, context, swipeRefreshLayout, view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(shoppingAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
