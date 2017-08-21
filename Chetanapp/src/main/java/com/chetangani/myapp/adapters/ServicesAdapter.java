package com.chetangani.myapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chetangani.myapp.R;
import com.chetangani.myapp.fragments.services.Servicedetails;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;

/**
 * Created by Chetan Gani on 4/18/2017.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {
    Context context;
    ArrayList<Servicedetails> arrayList = new ArrayList<>();
    FunctionCalls functionCalls = new FunctionCalls();
    String reading="", lastreading="";

    public ServicesAdapter(Context context, ArrayList<Servicedetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_card, parent, false);
        ServiceViewHolder viewHolder = new ServiceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Servicedetails details = arrayList.get(position);
        holder.tv_date.setText(functionCalls.convertdate(details.getService_date()));
        holder.tv_description.setText(details.getDescription());
        holder.tv_amount.setText(context.getResources().getString(R.string.rs)+" "+details.getAmount()+" /-");
        try {
            reading = details.getReading();
            if (!reading.equals("")) {
                holder.tv_reading.setText(reading);
                try {
                    lastreading = details.getLastreading();
                    if (!lastreading.equals("")) {
                        int distance = Integer.parseInt(reading) - Integer.parseInt(lastreading);
                        holder.tv_distance.setText(""+distance+" Kms");
                    } else holder.distance_layout.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    holder.distance_layout.setVisibility(View.GONE);
                }
            } else {
                holder.tv_reading.setText("");
                holder.distance_layout.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            holder.tv_reading.setText("");
            holder.distance_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_date, tv_description, tv_reading, tv_amount, tv_distance;
        LinearLayout distance_layout;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = (TextView) itemView.findViewById(R.id.service_date);
            tv_description = (TextView) itemView.findViewById(R.id.service_description);
            tv_reading = (TextView) itemView.findViewById(R.id.service_kilometres);
            tv_amount = (TextView) itemView.findViewById(R.id.service_amount);
            tv_distance = (TextView) itemView.findViewById(R.id.service_distance);
            distance_layout = (LinearLayout) itemView.findViewById(R.id.service_distance_layout);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
