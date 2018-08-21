package com.chetangani.myapp.fragments.expenses;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.adapters.ExpensesAdapter;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;
import java.util.Objects;

public class ViewExpenses extends Fragment {

    View view;

    public static String exp_month="";

    Database database;
    GetSet_Expenses getSetExpenses;
    FunctionCalls functionCalls;

    RecyclerView expenses_view;
    ArrayList<GetSet_Expenses> expenses_list;
    ExpensesAdapter expensesAdapter;
    FloatingActionButton addexpense_btn;

    public ViewExpenses() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_expenses, container, false);

        initialize();

        getValues();

        addexpense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                ft.replace(R.id.container_main, new AddExpenses());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    private void initialize() {
        functionCalls = new FunctionCalls();
        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        addexpense_btn = view.findViewById(R.id.addexpenses_btn);

        expenses_view = view.findViewById(R.id.view_all_expenses);
        expenses_list = new ArrayList<>();
        expensesAdapter = new ExpensesAdapter(expenses_list, getActivity());
        expenses_view.setHasFixedSize(true);
        expenses_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenses_view.setAdapter(expensesAdapter);
    }

    private void getValues() {
        Cursor data = database.getTotalExpenses();
        Cursor month_data = database.getTotalMonthExpenses();
        if (data.getCount() > 0) {
            expenses_list.clear();
            while (data.moveToNext()) {
                getSetExpenses = new GetSet_Expenses();
                String value = data.getString(data.getColumnIndex("exp_month"));
                if (!exp_month.equals(value)) {
                    exp_month = value;
                    getSetExpenses.setValue(functionCalls.expenses_month_view(value));
                    month_data.moveToNext();
                    getSetExpenses.setExp_debit(functionCalls.getAmount(getActivity(),
                            month_data.getDouble(month_data.getColumnIndex("AMOUNT"))));
                    getSetExpenses.setType(0);
                    expenses_list.add(getSetExpenses);
                }
                GetSet_Expenses getSet_expenses = new GetSet_Expenses();
                getSet_expenses.setValue(functionCalls.expenses_date_view(data.getString(data.getColumnIndex("exp_date"))));
                getSet_expenses.setExp_debit(functionCalls.getAmount(getActivity(),
                        data.getDouble(data.getColumnIndex("AMOUNT"))));
                getSet_expenses.setType(1);
                expenses_list.add(getSet_expenses);
                expensesAdapter.notifyDataSetChanged();
            }
        }
        month_data.close();
        data.close();
    }

}
