package com.chetangani.myapp.fragments.expenses;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.adapters.DayExpenseAdapter;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;
import java.util.Objects;

import static com.chetangani.myapp.values.Constants.SEARCH_ID;

public class ViewDayExpenses extends Fragment {

    View view;

    RecyclerView day_expense_view;
    ArrayList<GetSet_Expenses> day_expense_list;
    DayExpenseAdapter day_expense_adapter;

    String expense_date;

    Database database;
    FunctionCalls functionCalls;

    public ViewDayExpenses() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_day_expenses, container, false);

        initialize();

        getValues();

        return view;
    }

    private void initialize() {
        functionCalls = new FunctionCalls();
        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        day_expense_view = view.findViewById(R.id.view_day_expenses);
        day_expense_list = new ArrayList<>();
        day_expense_adapter = new DayExpenseAdapter(day_expense_list, getActivity());
        day_expense_view.setHasFixedSize(true);
        day_expense_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        day_expense_view.setAdapter(day_expense_adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            expense_date = bundle.getString(SEARCH_ID);
        }
    }

    private void getValues() {
        Cursor data = database.getDaydetails(expense_date);
        while (data.moveToNext()) {
            GetSet_Expenses getSetExpenses = new GetSet_Expenses();
            getSetExpenses.setDay_exp_mode(functionCalls.getCursorValue(data, "exp_mode"));
            if (!(functionCalls.getCursorValue(data, "exp_card")).equals("0"))
                getSetExpenses.setDay_exp_card(database.getCardname(functionCalls.getCursorValue(data, "exp_card")));
            else getSetExpenses.setDay_exp_card(functionCalls.getCursorValue(data, "exp_card"));
            getSetExpenses.setDay_exp_description(functionCalls.getCursorValue(data, "exp_description"));
            getSetExpenses.setDay_exp_category(database.getCategory(functionCalls.getCursorValue(data, "exp_category")));
            getSetExpenses.setDay_exp_amount(functionCalls.getCursorValue(data, "exp_debit"));
            day_expense_list.add(getSetExpenses);
            day_expense_adapter.notifyDataSetChanged();
        }
        data.close();
    }

}
