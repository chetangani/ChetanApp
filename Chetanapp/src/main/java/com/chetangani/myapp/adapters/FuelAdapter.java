package com.chetangani.myapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.fragments.fueltracker.GetSet_Fueldetails;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;

public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.FuelViewHolder> {
    private Context context;
    private ArrayList<GetSet_Fueldetails> arrayList;
    private FunctionCalls functionCalls = new FunctionCalls();
    private Database database;

    public FuelAdapter(Context context, ArrayList<GetSet_Fueldetails> arrayList, Database database) {
        this.context = context;
        this.arrayList = arrayList;
        this.database = database;
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuelcvtracker, parent, false);
        return new FuelViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {
        GetSet_Fueldetails getSetFueldetails = arrayList.get(position);
        holder.tv_fuelprice.setText(context.getResources().getString(R.string.rs)+" "+ getSetFueldetails.getFuelprice()+" /-");
        String fuel_filled = getSetFueldetails.getFuelfilled() + " ltr";
        holder.tv_fuelfilled.setText(fuel_filled);
        holder.tv_fuelamount.setText(context.getResources().getString(R.string.rs)+" "+ getSetFueldetails.getFuelamount()+" /-");
        holder.tv_fueldate.setText(functionCalls.convertdate(getSetFueldetails.getFueldate()));
        try {
            if (!getSetFueldetails.getEndreading().equals("")) {
                int days = functionCalls.getDuration(getSetFueldetails.getFuellastdate(), getSetFueldetails.getFueldate());
                if (days == 1) {
                    holder.tv_duration.setText(""+days+" Day");
                } else holder.tv_duration.setText(""+days+" Days");
            } else {
                int days = functionCalls.getDuration(functionCalls.getcurrentdate(), getSetFueldetails.getFueldate());
                if (days == 1) {
                    holder.tv_duration.setText(""+days+" Day");
                } else holder.tv_duration.setText(""+days+" Days");
            }
        } catch (NullPointerException e) {
            int days = functionCalls.getDuration(functionCalls.getcurrentdate(), getSetFueldetails.getFueldate());
            if (days == 1) {
                holder.tv_duration.setText(""+days+" Day");
            } else holder.tv_duration.setText(""+days+" Days");
        }
        if (position == 0)
            holder.delete_btn.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FuelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_fuelprice, tv_fuelfilled, tv_fueldate, tv_fuelamount, tv_duration;
        ImageView delete_btn;

        FuelViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tv_fuelprice = itemView.findViewById(R.id.tv_fuelprice);
            tv_fuelfilled = itemView.findViewById(R.id.tv_fuelfilled);
            tv_fueldate = itemView.findViewById(R.id.tv_fuelfilleddate);
            tv_fuelamount = itemView.findViewById(R.id.tv_fuelamount);
            tv_duration = itemView.findViewById(R.id.tv_fuelduration);
            delete_btn = itemView.findViewById(R.id.delete_btn);
            delete_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            GetSet_Fueldetails getSetFueldetails = arrayList.get(pos);
            String fuelid = getSetFueldetails.getFuelid();
            switch (v.getId()) {
                case R.id.delete_btn:
                    Cursor delete = database.deletefueldetails(fuelid);
                    delete.moveToNext();
                    arrayList.remove(pos);
                    notifyItemRemoved(pos);
                    notifyDataSetChanged();
                    break;

                default:
                    ((MainActivity) context).switchFuelContent(MainActivity.Steps.FORM3, getSetFueldetails);
                    break;
            }
        }
    }
}