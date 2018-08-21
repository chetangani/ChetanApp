package com.chetangani.myapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chetangani.myapp.R;
import com.chetangani.myapp.fragments.expenses.GetSet_Expenses;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;

public class DayExpenseAdapter extends RecyclerView.Adapter<DayExpenseAdapter.DayExpenseViewHolder> {
    private ArrayList<GetSet_Expenses> arrayList;
    private Context context;
    private FunctionCalls functionCalls = new FunctionCalls();

    public DayExpenseAdapter(ArrayList<GetSet_Expenses> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DayExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_day_view, parent, false);
        return new DayExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayExpenseViewHolder holder, int position) {
        GetSet_Expenses details = arrayList.get(position);
        int mode = Integer.parseInt(details.getDay_exp_mode());
        holder.tv_mode.setText(context.getResources().getStringArray(R.array.mode_list)[mode]);
        if (mode == 2) {
            holder.card_layout.setVisibility(View.VISIBLE);
            holder.tv_card.setText(details.getDay_exp_card());
        } else holder.card_layout.setVisibility(View.GONE);
        holder.tv_category.setText(details.getDay_exp_category());
        holder.tv_description.setText(details.getDay_exp_description());
        holder.tv_amount.setText(functionCalls.getAmount(context, Double.parseDouble(details.getDay_exp_amount())));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class DayExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mode, tv_card, tv_category, tv_description, tv_amount;
        LinearLayout card_layout;

        DayExpenseViewHolder(View itemView) {
            super(itemView);
            tv_mode = itemView.findViewById(R.id.txt_expense_mode);
            tv_card = itemView.findViewById(R.id.txt_expense_card);
            tv_category = itemView.findViewById(R.id.txt_expense_category);
            tv_description = itemView.findViewById(R.id.txt_expense_description);
            tv_amount = itemView.findViewById(R.id.txt_expense_amt);
            card_layout = itemView.findViewById(R.id.day_expense_card_layout);
        }
    }
}
