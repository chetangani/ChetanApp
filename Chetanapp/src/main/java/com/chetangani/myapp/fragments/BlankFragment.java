package com.chetangani.myapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chetangani.myapp.NavigationActivity;
import com.chetangani.myapp.R;

import java.util.Objects;

import static com.chetangani.myapp.values.Constants.CARDS_LAYOUT;
import static com.chetangani.myapp.values.Constants.EXPENSES_LAYOUT;
import static com.chetangani.myapp.values.Constants.FUEL_LAYOUT;
import static com.chetangani.myapp.values.Constants.SERVICES_LAYOUT;

public class BlankFragment extends Fragment implements View.OnClickListener {
    View view;

    Button btn_cards, btn_services, btn_fuels, btn_expenses;

    public BlankFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blank, container, false);

        btn_cards = view.findViewById(R.id.view_cards_btn);
        btn_cards.setOnClickListener(this);
        btn_services = view.findViewById(R.id.view_services_btn);
        btn_services.setOnClickListener(this);
        btn_fuels = view.findViewById(R.id.view_fuel_details_btn);
        btn_fuels.setOnClickListener(this);
        btn_expenses = view.findViewById(R.id.view_expenses_btn);
        btn_expenses.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_cards_btn:
                ((NavigationActivity) Objects.requireNonNull(getActivity())).startActivityprocess(getActivity(), CARDS_LAYOUT);
                break;

            case R.id.view_services_btn:
                ((NavigationActivity) Objects.requireNonNull(getActivity())).startActivityprocess(getActivity(), SERVICES_LAYOUT);
                break;

            case R.id.view_fuel_details_btn:
                ((NavigationActivity) Objects.requireNonNull(getActivity())).startActivityprocess(getActivity(), FUEL_LAYOUT);
                break;

            case R.id.view_expenses_btn:
                ((NavigationActivity) Objects.requireNonNull(getActivity())).startActivityprocess(getActivity(), EXPENSES_LAYOUT);
                break;
        }
    }
}
