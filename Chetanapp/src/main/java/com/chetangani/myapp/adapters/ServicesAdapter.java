package com.chetangani.myapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chetangani.myapp.R;
import com.chetangani.myapp.fragments.services.Servicedetails;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {
    private Context context;
    private ArrayList<Servicedetails> arrayList;
    private FunctionCalls functionCalls = new FunctionCalls();

    public ServicesAdapter(Context context, ArrayList<Servicedetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Servicedetails details = arrayList.get(position);
        holder.tv_date.setText(functionCalls.convertdate(details.getService_date()));
        holder.tv_description.setText(details.getDescription());
        holder.tv_amount.setText(context.getResources().getString(R.string.rs)+" "+details.getAmount()+" /-");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_date, tv_description, tv_amount;

        ServiceViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = itemView.findViewById(R.id.service_date);
            tv_description = itemView.findViewById(R.id.service_particular);
            tv_amount = itemView.findViewById(R.id.service_amount);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
