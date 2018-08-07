package com.chetangani.myapp.fragments.expenses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.values.FunctionCalls;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.chetangani.myapp.values.Constants.CARDS;
import static com.chetangani.myapp.values.Constants.CATEGORY;

public class AddExpenses extends Fragment {
    private static final int DATE_DLG = 1;

    View view;

    Button btn_addexpense;
    TextView tv_current_date, tv_current_month, tv_credit, tv_debit, tv_balance;
    TextInputLayout til_category;
    TextInputEditText et_description, et_amount, et_category;
    RadioGroup type_group;
    RadioButton rb_credit, rb_debit;
    LinearLayout card_mode_layout;
    Spinner sp_mode, sp_cards, sp_category;
    ArrayList<String> mode_list, card_list, category_list;
    ArrayAdapter<String> mode_adapter, card_adapter, category_adapter;

    double credit=0, debit=0;
    String selected_date="", selected_card="", category_code="", transaction_mode="", exp_description="", exp_amount="", transaction_type="";

    FunctionCalls functionCalls;
    Database database;

    public AddExpenses() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_expenses, container, false);

        initialize();

        setValues(functionCalls.getcurrentdate());

        spinner_values();

        radio_values();

        editText_values();

        btn_addexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_expense_details(v);
            }
        });

        tv_current_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog(DATE_DLG);
            }
        });

        et_amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    add_expense_details(v);
                return false;
            }
        });

        return view;
    }

    private void initialize() {
        functionCalls = new FunctionCalls();
        ViewExpenses.exp_month="";

        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        tv_current_date = view.findViewById(R.id.expense_date);
        tv_current_month = view.findViewById(R.id.month_expenses);
        tv_credit = view.findViewById(R.id.credit_amount);
        tv_debit = view.findViewById(R.id.debit_amount);
        tv_balance = view.findViewById(R.id.balance_amount);

        btn_addexpense = view.findViewById(R.id.addexpense_btn);

        et_description = view.findViewById(R.id.et_expense_description);
        et_amount = view.findViewById(R.id.et_expense_amount);

        til_category = view.findViewById(R.id.til_expense_new_category);
        et_category = view.findViewById(R.id.et_expense_new_category);

        card_mode_layout = view.findViewById(R.id.card_mode_layout);

        type_group = view.findViewById(R.id.rg_transaction_type);
        rb_credit = view.findViewById(R.id.rb_credit);
        rb_debit = view.findViewById(R.id.rb_debit);

        sp_mode = view.findViewById(R.id.spn_transaction_mode);
        mode_list = new ArrayList<>();
        mode_list.addAll(Arrays.asList(getResources().getStringArray(R.array.mode_list)));
        mode_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, mode_list);
        sp_mode.setAdapter(mode_adapter);

        sp_cards = view.findViewById(R.id.spn_card_transaction);
        card_list = new ArrayList<>();
        array_list_values(CARDS, card_list);
        card_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, card_list);
        sp_cards.setAdapter(card_adapter);

        sp_category = view.findViewById(R.id.spn_transaction_category);
        category_list = new ArrayList<>();
        array_list_values(CATEGORY, category_list);
        category_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, category_list);
        sp_category.setAdapter(category_adapter);
    }

    private void setValues(String date) {
        credit = 0;
        debit = 0;
        selected_date = date;
        tv_current_date.setText(functionCalls.convertdate(date));
        tv_current_month.setText(String.format("%s %s", functionCalls.monthview(date), getResources().getString(R.string.month_expenses)));
        Cursor data = database.getBalances(date.substring(0, 7));
        if (data.getCount() > 0) {
            data.moveToNext();
            credit = data.getDouble(data.getColumnIndex("CREDIT"));
            debit = data.getDouble(data.getColumnIndex("DEBIT"));
        }
        setbalances("0", 1);
    }

    private void setbalances(String amount, int value) {
        DecimalFormat num = new DecimalFormat("##0.00");
        double diff_credit, diff_debit;
        if (value == 1) {
            diff_credit = credit + Double.parseDouble(amount);
            diff_debit = debit;
        }
        else {
            diff_credit = credit;
            diff_debit = debit + Double.parseDouble(amount);
        }
        tv_credit.setText(String.format("%s %s %s", getResources().getString(R.string.rs), num.format(diff_credit), "/-"));
        tv_debit.setText(String.format("%s %s %s", getResources().getString(R.string.rs), num.format(diff_debit), "/-"));
        tv_balance.setText(String.format("%s %s %s", getResources().getString(R.string.rs), num.format(diff_credit - diff_debit), "/-"));
    }

    private void spinner_values() {
        sp_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2)
                    card_mode_layout.setVisibility(View.VISIBLE);
                else card_mode_layout.setVisibility(View.GONE);
                if (position > 0)
                    transaction_mode = String.valueOf(position);
                else transaction_mode = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String category = category_list.get(position);
                    if (category.equals("OTHERS")) {
                        til_category.setVisibility(View.VISIBLE);
                        til_category.requestFocus();
                    } else {
                        til_category.setVisibility(View.GONE);
                        category_code = functionCalls.getSelected_category(database, category);
                    }
                } else {
                    category_code = "";
                    til_category.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_cards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0)
                    selected_card = functionCalls.getSelected_card(database, position);
                else selected_card = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void array_list_values(String value, ArrayList<String> arrayList) {
        arrayList.clear();
        switch (value) {
            case CATEGORY:
                Cursor category = database.getCategorylist();
                while (category.moveToNext()) {
                    arrayList.add(category.getString(category.getColumnIndex("categories")));
                }
                category.close();
                break;

            case CARDS:
                Cursor cards = database.getcardsdetails();
                arrayList.add("SELECT");
                if (cards.getCount() > 0) {
                    while (cards.moveToNext())
                        arrayList.add(functionCalls.getCardName(cards));
                }
                cards.close();
                break;
        }
    }

    private void radio_values() {
        type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_credit.isChecked()) {
                    if (!TextUtils.isEmpty(et_amount.getText())) {
                        exp_amount = et_amount.getText().toString();
                        setbalances(exp_amount, 1);
                    }
                    transaction_type = "1";
                }
                if (rb_debit.isChecked()) {
                    if (!TextUtils.isEmpty(et_amount.getText())) {
                        exp_amount = et_amount.getText().toString();
                        setbalances(exp_amount, 0);
                    }
                    transaction_type = "0";
                }
            }
        });
    }

    private void editText_values() {
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (rb_credit.isChecked())
                        setbalances(s.toString(), 1);
                    if (rb_debit.isChecked())
                        setbalances(s.toString(), 0);
                } else setbalances("0", 1);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void add_expense_details(View view) {
        if (TextUtils.isEmpty(transaction_type)) {
            functionCalls.showsnackbar(view, getResources().getString(R.string.valid_exp_transaction_type));
            return;
        }
        if (TextUtils.isEmpty(transaction_mode)) {
            functionCalls.showsnackbar(view, getResources().getString(R.string.valid_exp_transaction_mode));
            return;
        }
        if (til_category.getVisibility() == View.VISIBLE) {
            if (!TextUtils.isEmpty(et_category.getText()))
                category_code = database.insert_new_category(et_category.getText().toString());
            else {
                functionCalls.showsnackbar(view, getResources().getString(R.string.valid_exp_new_category));
                return;
            }
        }
        if (TextUtils.isEmpty(category_code)) {
            functionCalls.showsnackbar(view, getResources().getString(R.string.valid_exp_category));
            return;
        }
        if (card_mode_layout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(selected_card)) {
                functionCalls.showsnackbar(view, getResources().getString(R.string.valid_exp_card_selection));
                return;
            }
        } else selected_card = "0";
        if (TextUtils.isEmpty(et_description.getText())) {
            functionCalls.showsnackbar(view, getResources().getString(R.string.valid_exp_description));
            return;
        } else exp_description = et_description.getText().toString();
        if (TextUtils.isEmpty(et_amount.getText())) {
            functionCalls.showsnackbar(view, getResources().getString(R.string.valid_exp_amount));
            return;
        } else exp_amount = et_amount.getText().toString();
        /*-----------------------------------Inserting Expenses-----------------------------------*/
        if (rb_credit.isChecked())
            if (database.insert_Expense_details(selected_date, selected_date.substring(0, 7), selected_date.substring(0, 4),
                    functionCalls.getcurrentTime(), transaction_mode, exp_amount, "0", exp_description, selected_card, category_code)) {
                clear_screen();
            } else functionCalls.showtoast(getActivity(), getResources().getString(R.string.expense_insert_error));
        else if (database.insert_Expense_details(selected_date, selected_date.substring(0, 7), selected_date.substring(0, 4),
                functionCalls.getcurrentTime(), transaction_mode, "0", exp_amount, exp_description, selected_card, category_code)) {
            clear_screen();
        } else functionCalls.showtoast(getActivity(), getResources().getString(R.string.expense_insert_error));
    }

    private void clear_screen() {
        functionCalls.showtoast(getActivity(), getResources().getString(R.string.expense_inserted));
        if (rb_credit.isChecked())
            credit = credit + Double.parseDouble(exp_amount);
        else debit = debit + Double.parseDouble(exp_amount);
        category_adapter.notifyDataSetChanged();
        type_group.clearCheck();
        sp_mode.setSelection(0);
        sp_category.setSelection(0);
        sp_cards.setSelection(0);
        et_category.setText("");
        et_description.setText("");
        et_amount.setText("");
    }

    private void showdialog(int id) {
        AlertDialog dialog;
        switch (id) {
            case DATE_DLG:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dp = new DatePickerDialog(Objects.requireNonNull(getActivity()), dpd, year, month, date);
                dp.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dialog = dp;
                dialog.show();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int yr, int monthofYear, int dayOfMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = null;
            try {
                date1 = sdf.parse(""+ yr + "-" + ""+ (monthofYear + 1) + "-" + ""+dayOfMonth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((MainActivity) Objects.requireNonNull(getActivity())).hidekeyboard();
            setValues(sdf.format(date1));
        }
    };
}
