package com.chetangani.myapp.fragments.fueltracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chetangani.myapp.R;
import com.chetangani.myapp.values.FunctionCalls;

import java.text.DecimalFormat;
import java.util.Objects;

import static com.chetangani.myapp.values.Constants.SEARCH_ID;

public class Fuel_Details extends Fragment {

    View view;

    TextView tv_fuel_date, tv_fuel_amount, tv_fuel_price, tv_fuel_filled, tv_fuel_start_reading, tv_fuel_end_reading, tv_fuel_distance,
            tv_fuel_milenge, tv_fuel_duration;
    LinearLayout end_reading_layout;
    ImageView fuel_brand_logo;

    GetSet_Fueldetails getSetFueldetails;
    FunctionCalls functionCalls;

    public Fuel_Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fuel_details, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            getSetFueldetails = (GetSet_Fueldetails) bundle.getSerializable(SEARCH_ID);
        }

        initialize();

        setvalues();

        return view;
    }

    private void initialize() {
        functionCalls = new FunctionCalls();

        tv_fuel_date = view.findViewById(R.id.details_fuel_date);
        tv_fuel_amount = view.findViewById(R.id.details_fuel_amount);
        tv_fuel_price = view.findViewById(R.id.details_fuel_price);
        tv_fuel_filled = view.findViewById(R.id.details_fuel_filled);
        tv_fuel_start_reading = view.findViewById(R.id.details_fuel_start_reading);
        tv_fuel_end_reading = view.findViewById(R.id.details_fuel_end_reading);
        tv_fuel_distance = view.findViewById(R.id.details_fuel_distance);
        tv_fuel_milenge = view.findViewById(R.id.details_fuel_mileage);
        tv_fuel_duration = view.findViewById(R.id.details_fuel_duration);

        fuel_brand_logo = view.findViewById(R.id.fuel_brand_logo);

        end_reading_layout = view.findViewById(R.id.end_reading_layout);
    }

    @SuppressLint("SetTextI18n")
    private void setvalues() {
        tv_fuel_date.setText(functionCalls.convertdate(getSetFueldetails.getFueldate()));
        tv_fuel_start_reading.setText(getSetFueldetails.getStartreading());
        tv_fuel_price.setText(getResources().getString(R.string.rs)+" "+ getSetFueldetails.getFuelprice()+" /-");
        tv_fuel_amount.setText(getResources().getString(R.string.rs)+" "+ getSetFueldetails.getFuelamount()+" /-");
        tv_fuel_filled.setText(getSetFueldetails.getFuelfilled() + " ltr");
        if (!getSetFueldetails.getEndreading().equals("")) {
            int days = functionCalls.getDuration(getSetFueldetails.getFuellastdate(), getSetFueldetails.getFueldate());
            if (days == 1) {
                tv_fuel_duration.setText(""+days+" Day");
            } else tv_fuel_duration.setText(""+days+" Days");
            end_reading_layout.setVisibility(View.VISIBLE);
            tv_fuel_end_reading.setText(getSetFueldetails.getEndreading());
            int distance = Integer.parseInt(getSetFueldetails.getEndreading()) - Integer.parseInt(getSetFueldetails.getStartreading());
            tv_fuel_distance.setText(String.valueOf(distance)+" Kms");
            double fuel = Double.parseDouble(getSetFueldetails.getFuelfilled());
            String mileage = new DecimalFormat("##.##").format(distance / fuel)+" Kms/L";
            tv_fuel_milenge.setText(mileage);
        } else {
            int days = functionCalls.getDuration(functionCalls.getcurrentdate(), getSetFueldetails.getFueldate());
            if (days == 1) {
                tv_fuel_duration.setText(""+days+" Day");
            } else tv_fuel_duration.setText(""+days+" Days");
            end_reading_layout.setVisibility(View.GONE);
        }
        switch (getSetFueldetails.getFuel_brand()) {
            case "1":
                fuel_brand_logo.setImageResource(R.drawable.hp_logo);
                break;

            case "2":
                fuel_brand_logo.setImageResource(R.drawable.bp_logo);
                break;

            case "3":
                fuel_brand_logo.setImageResource(R.drawable.shell_logo);
                break;

            case "4":
                fuel_brand_logo.setImageResource(R.drawable.essar_logo);
                break;

            case "5":
                fuel_brand_logo.setImageResource(R.drawable.indian_oil_logo);
                break;
        }
    }

}
