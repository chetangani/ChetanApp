package com.chetangani.myapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.fragments.expenses.GetSet_Expenses;
import com.chetangani.myapp.fragments.expenses.ViewExpenses;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;

import static com.chetangani.myapp.fragments.expenses.GetSet_Expenses.DATE_TYPE;
import static com.chetangani.myapp.fragments.expenses.GetSet_Expenses.MONTH_TYPE;

public class ExpensesAdapter extends RecyclerView.Adapter{
    private ArrayList<GetSet_Expenses> arrayList;
    private Context context;
    private FunctionCalls functionCalls = new FunctionCalls();

    public ExpensesAdapter(ArrayList<GetSet_Expenses> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MONTH_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_month_view, parent, false);
                return new MonthViewHolder(view);

            case DATE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_date_view, parent, false);
                return new DateViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GetSet_Expenses getSetExpenses = arrayList.get(position);
        if (getSetExpenses != null) {
            switch (getSetExpenses.getType()) {
                case MONTH_TYPE:
                    ((MonthViewHolder) holder).tv_month.setText(getSetExpenses.getValue());
                    ((MonthViewHolder) holder).tv_amount.setText(getSetExpenses.getExp_debit());
                    break;

                case DATE_TYPE:
                    ((DateViewHolder) holder).tv_date.setText(getSetExpenses.getValue());
                    ((DateViewHolder) holder).tv_amount.setText(getSetExpenses.getExp_debit());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList != null) {
            GetSet_Expenses object = arrayList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {
        TextView tv_month, tv_amount;

        MonthViewHolder(View itemView) {
            super(itemView);
            tv_month = itemView.findViewById(R.id.txt_expense_month);
            tv_amount = itemView.findViewById(R.id.txt_expense_month_amount);
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_date, tv_amount;

        DateViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = itemView.findViewById(R.id.txt_expense_date);
            tv_amount = itemView.findViewById(R.id.txt_expense_amount);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetSet_Expenses getSetExpenses = arrayList.get(position);
            ViewExpenses.exp_month = "";
            ((MainActivity) context).switchExpensesContent(MainActivity.Steps.FORM6, functionCalls.expenses_day_view(getSetExpenses.getValue()));
        }
    }
}
