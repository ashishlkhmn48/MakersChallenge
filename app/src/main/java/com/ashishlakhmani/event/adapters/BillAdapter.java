package com.ashishlakhmani.event.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashishlakhmani.event.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BillAdapter extends RecyclerView.Adapter {

    private Context context;
    private JSONArray array;

    public BillAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_layout, parent, false);
        return new BillAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final JSONObject object;
        try {
            object = array.getJSONObject(position);
            ((MyViewHolder) holder).name.setText(object.getString("name"));
            ((MyViewHolder) holder).qty.setText(object.getString("qty"));
            ((MyViewHolder) holder).price.setText(object.getString("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        // initialize the item view's

        TextView name;
        TextView qty;
        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bill_name);
            qty = itemView.findViewById(R.id.bill_qty);
            price = itemView.findViewById(R.id.bill_price);
        }
    }
}
