package com.ashishlakhmani.event.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashishlakhmani.event.R;
import com.ashishlakhmani.event.activities.MainActivity;
import com.ashishlakhmani.event.classes.BillBackground;
import com.ashishlakhmani.event.classes.BudgetBillBakground;

public class BillFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        BudgetBillBakground budgetBillBakground = new BudgetBillBakground(getContext(), view);
        budgetBillBakground.execute(MainActivity.username);

        BillBackground billBackground = new BillBackground(getContext(), view);
        billBackground.execute(MainActivity.username);

        return view;
    }

}
