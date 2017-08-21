package com.chetangani.myapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.fragments.fueltracker.Fueldetails;
import com.chetangani.myapp.values.FunctionCalls;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.FuelViewHolder> {
    Context context;
    ArrayList<Fueldetails> arrayList = new ArrayList<Fueldetails>();
    FunctionCalls functionCalls = new FunctionCalls();
    Database database;

    public FuelAdapter(Context context, ArrayList<Fueldetails> arrayList, Database database) {
        this.context = context;
        this.arrayList = arrayList;
        this.database = database;
    }

    @Override
    public FuelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuelcvtracker, parent, false);
        FuelViewHolder viewHolder = new FuelViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FuelViewHolder holder, int position) {
        Fueldetails fueldetails = arrayList.get(position);
        holder.tv_startreading.setText(fueldetails.getStartreading());
        holder.tv_fuelprice.setText(context.getResources().getString(R.string.rs)+" "+fueldetails.getFuelprice()+" /-");
        holder.tv_fuelfilled.setText(fueldetails.getFuelfilled() + " ltr");
        holder.tv_fuelamount.setText(context.getResources().getString(R.string.rs)+" "+fueldetails.getFuelamount()+" /-");
        holder.tv_fueldate.setText(functionCalls.convertdate(fueldetails.getFueldate()));
        try {
            if (!fueldetails.getEndreading().equals("")) {
                holder.tv_endreading.setText(fueldetails.getEndreading());
                double distance = Integer.parseInt(fueldetails.getEndreading()) - Integer.parseInt(fueldetails.getStartreading());
                holder.tv_distance.setText(new DecimalFormat("##").format(distance)+" Kms");
                double fuel = Double.parseDouble(fueldetails.getFuelfilled());
                holder.tv_mileage.setText(new DecimalFormat("##.##").format(distance / fuel)+" Km/L");
                int days = functionCalls.getDuration(fueldetails.getFuellastdate(), fueldetails.getFueldate());
                if (days == 1) {
                    holder.tv_duration.setText(""+days+" Day");
                } else holder.tv_duration.setText(""+days+" Days");
            } else {
                holder.tv_endreading_txt.setVisibility(View.GONE);
                holder.tv_endreading.setVisibility(View.GONE);
                holder.distance_mileage_layout.setVisibility(View.GONE);
                int days = functionCalls.getDuration(functionCalls.getcurrentdate(), fueldetails.getFueldate());
                if (days == 1) {
                    holder.tv_duration.setText(""+days+" Day");
                } else holder.tv_duration.setText(""+days+" Days");
            }
        } catch (NullPointerException e) {
            holder.tv_endreading_txt.setVisibility(View.GONE);
            holder.tv_endreading.setVisibility(View.GONE);
            holder.distance_mileage_layout.setVisibility(View.GONE);
            int days = functionCalls.getDuration(functionCalls.getcurrentdate(), fueldetails.getFueldate());
            if (days == 1) {
                holder.tv_duration.setText(""+days+" Day");
            } else holder.tv_duration.setText(""+days+" Days");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FuelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_startreading, tv_endreading_txt, tv_endreading, tv_fuelprice, tv_fuelfilled, tv_fueldate,
                tv_fuelamount, tv_distance, tv_mileage, tv_duration;
        LinearLayout distance_mileage_layout;
        ImageView edit_btn, delete_btn;

        public FuelViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tv_startreading = (TextView) itemView.findViewById(R.id.tv_startreading);
            tv_endreading_txt = (TextView) itemView.findViewById(R.id.tv_endreading_txt);
            tv_endreading = (TextView) itemView.findViewById(R.id.tv_endreading);
            tv_fuelprice = (TextView) itemView.findViewById(R.id.tv_fuelprice);
            tv_fuelfilled = (TextView) itemView.findViewById(R.id.tv_fuelfilled);
            tv_fueldate = (TextView) itemView.findViewById(R.id.tv_fuelfilleddate);
            tv_fuelamount = (TextView) itemView.findViewById(R.id.tv_fuelamount);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distcovered);
            tv_mileage = (TextView) itemView.findViewById(R.id.tv_mileage);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_fuelduration);
            distance_mileage_layout = (LinearLayout) itemView.findViewById(R.id.distance_mileage_layout);
            edit_btn = (ImageView) itemView.findViewById(R.id.edit_btn);
            edit_btn.setOnClickListener(this);
            delete_btn = (ImageView) itemView.findViewById(R.id.delete_btn);
            delete_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Fueldetails fueldetails = arrayList.get(pos);
            String fuelid = fueldetails.getFuelid();
            switch (v.getId()) {
                case R.id.delete_btn:
                    Cursor delete = database.deletefueldetails(fuelid);
                    delete.moveToNext();
                    arrayList.remove(pos);
                    notifyItemRemoved(pos);
                    break;
            }
        }
    }
}