package com.chetangani.myapp.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chetangani.myapp.R;
import com.chetangani.myapp.fragments.fueltracker.GetSet_Fueldetails;

import java.util.ArrayList;

public class MileageStatusAdapter extends RecyclerView.Adapter<MileageStatusAdapter.MileageViewHolder> {
    private ArrayList<GetSet_Fueldetails> arrayList;

    public MileageStatusAdapter(ArrayList<GetSet_Fueldetails> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MileageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_date_view, parent, false);
        return new MileageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MileageViewHolder holder, int position) {
        GetSet_Fueldetails fueldetails = arrayList.get(position);
        holder.tv_mileage.setText(fueldetails.getTest_mileage() + " Kms/L");
        holder.tv_distance.setText(fueldetails.getTest_distance() + " Kms");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MileageViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mileage, tv_distance;

        MileageViewHolder(View itemView) {
            super(itemView);
            tv_mileage = itemView.findViewById(R.id.txt_expense_date);
            tv_distance = itemView.findViewById(R.id.txt_expense_amount);
        }
    }
}
