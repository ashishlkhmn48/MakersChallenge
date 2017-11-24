package com.ashishlakhmani.event.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashishlakhmani.event.R;
import com.ashishlakhmani.event.activities.MainActivity;
import com.ashishlakhmani.event.classes.BudgetBackground;
import com.ashishlakhmani.event.classes.ProductBackground;

public class ShoppingFragment extends Fragment {

    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        ProductBackground productBackground = new ProductBackground(getContext(), view, swipeRefreshLayout);
        productBackground.execute();
        BudgetBackground budgetBackground = new BudgetBackground(getContext(), view, swipeRefreshLayout);
        budgetBackground.execute(MainActivity.username);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BudgetBackground budgetBackground = new BudgetBackground(getContext(), view, swipeRefreshLayout);
                budgetBackground.execute(MainActivity.username);

                ProductBackground productBackground = new ProductBackground(getContext(), view, swipeRefreshLayout);
                productBackground.execute();
            }
        });
        return view;
    }

}
