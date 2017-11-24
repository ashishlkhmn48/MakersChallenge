package com.ashishlakhmani.event.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishlakhmani.event.R;
import com.ashishlakhmani.event.activities.MainActivity;
import com.ashishlakhmani.event.classes.UpdateServer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingAdapter extends RecyclerView.Adapter {

    static int count = 0;

    private SwipeRefreshLayout swipeRefreshLayout;
    private JSONArray arr;
    private Context context;
    private View view;

    public ShoppingAdapter(JSONArray arr, Context context, SwipeRefreshLayout swipeRefreshLayout, View view) {
        this.arr = arr;
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent, false);
        return new ShoppingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        try {
            final JSONObject object = arr.getJSONObject(position);
            ((MyViewHolder) holder).productName.setText(object.getString("name"));
            ((MyViewHolder) holder).qtyLeft.setText(object.getString("qty"));
            ((MyViewHolder) holder).price.setText(object.getString("price"));
            ((MyViewHolder) holder).maxQty.setText(object.getString("max"));

            Picasso.with(context)
                    .load(object.getString("image"))
                    .placeholder(R.drawable.placeholder_album)
                    .into(((MyViewHolder) holder).productImage);


            ((MyViewHolder) holder).buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final int total = Integer.parseInt(((MyViewHolder) holder).qtySelect.getText().toString()) * Integer.parseInt(((MyViewHolder) holder).price.getText().toString());
                        if(Integer.parseInt(((MyViewHolder) holder).qtySelect.getText().toString()) == 0){
                            throw new NullPointerException();
                        }

                        if (Integer.parseInt(((MyViewHolder) holder).maxQty.getText().toString()) >= Integer.parseInt(((MyViewHolder) holder).qtySelect.getText().toString())) {

                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                            alertDialogBuilder.setTitle("Warning..!!!");
                            alertDialogBuilder.setIcon(R.drawable.warning);
                            alertDialogBuilder.setMessage("You Can't Replace the Item/s.\nAre you sure??");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    UpdateServer updateServer = new UpdateServer(context, swipeRefreshLayout, ((MyViewHolder) holder).progressBar, ((MyViewHolder) holder).buyNow, view);
                                    updateServer.execute(MainActivity.username, ((MyViewHolder) holder).productName.getText().toString(), ((MyViewHolder) holder).qtySelect.getText().toString(), String.valueOf(total), ((MyViewHolder) holder).maxQty.getText().toString());
                                }
                            });

                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            Toast.makeText(context, "You can only Select Maximum Given.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Please Enter Correct Quantity Format.", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e){
                        Toast.makeText(context, "Please Enter a Valid Quantity.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arr.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        // initialize the item view's

        TextView productName;
        TextView qtyLeft;
        TextView price;
        Button buyNow;
        ImageView productImage;
        ProgressBar progressBar;
        TextView maxQty;
        EditText qtySelect;


        public MyViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            qtyLeft = itemView.findViewById(R.id.qty_left);
            price = itemView.findViewById(R.id.product_price);
            buyNow = itemView.findViewById(R.id.buyNow);
            productImage = itemView.findViewById(R.id.product_image);
            progressBar = itemView.findViewById(R.id.transaction_progress);
            maxQty = itemView.findViewById(R.id.maxQty);
            qtySelect = itemView.findViewById(R.id.qtySelect);
        }
    }
}
