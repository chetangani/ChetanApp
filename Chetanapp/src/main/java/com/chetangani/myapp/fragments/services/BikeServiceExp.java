package com.chetangani.myapp.fragments.services;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.values.FunctionCalls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BikeServiceExp extends Fragment {
    public static final int DATE_DLG = 1;

    View view;
    EditText et_servicedate, et_reading, et_description, et_amount;
    Button update_btn;
    Spinner sp_particular;
    ArrayList<String> particular_list;
    ArrayAdapter<String> particular_adapter;
    Database database;
    String servicedate="", reading="", servicedescription="", serviceamount="", servicelastreading="", service_particular="";
    FunctionCalls fcalls;

    public BikeServiceExp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.bike_service_exp, container, false);

        initialize();

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).hidekeyboard();
                checkdetails(v);
            }
        });

        et_servicedate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showdialog(DATE_DLG);
            }
        });

        et_amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkdetails(v);
                }
                return false;
            }
        });

        return view;
    }

    private void initialize() {
        fcalls = new FunctionCalls();

        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        et_servicedate = view.findViewById(R.id.et_servicedate);
        et_reading = view.findViewById(R.id.et_kilometers);
        et_description = view.findViewById(R.id.et_description);
        et_amount = view.findViewById(R.id.et_expamount);
        update_btn = view.findViewById(R.id.addexp_btn);

        sp_particular = view.findViewById(R.id.sp_service_particular);
        particular_list = new ArrayList<>();
        particular_list.addAll(Arrays.asList(getResources().getStringArray(R.array.service_particular)));
        particular_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, particular_list);
        sp_particular.setAdapter(particular_adapter);

        et_servicedate.setText(fcalls.convertdate(fcalls.getcurrentdate()));

        sp_particular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    service_particular = "";
                } else {
                    service_particular = particular_list.get(position);
                    et_description.requestFocus();
                    et_description.setSelection(et_description.getText().length());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void checkdetails(View view) {
        servicedate = fcalls.getreturndate(et_servicedate.getText().toString());
        if (!servicedate.equals("")) {
            if (!TextUtils.isEmpty(service_particular)) {
                servicedescription = et_description.getText().toString();
                if (!servicedescription.equals("")) {
                    serviceamount = et_amount.getText().toString();
                    if (!serviceamount.equals("")) {
                        reading = et_reading.getText().toString();
                        if (!reading.equals("")) {
                            Cursor services = database.getservicedetails();
                            if (services.getCount() > 0) {
                                Cursor getlastreading = database.getservicelastreading();
                                getlastreading.moveToNext();
                                try {
                                    servicelastreading = getlastreading.getString(getlastreading.getColumnIndex("cur_reading"));
                                } catch (NullPointerException e) {
                                    servicelastreading = "";
                                }
                            }
                        }
                        loadnextscreen(view);
                    } else et_amount.setError("Enter Service Amount");
                } else et_description.setError("Enter Service Description");
            }
        } else et_servicedate.getText().toString();
    }

    @SuppressWarnings("ConstantConditions")
    private void loadnextscreen(View view) {
        if (!reading.equals("") && !servicelastreading.equals(""))
            if (Integer.parseInt(reading) < Integer.parseInt(servicelastreading)) {
                et_reading.setError("Enter the reading more than previous reading..");
                ((MainActivity) getActivity()).showsnackbar(view, "Enter current reading more than previous reading");
                return;
            }
        database.insertservicedetails(servicedate, reading, service_particular, servicedescription, serviceamount, servicelastreading);
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        fragmentManager.popBackStack();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, new AllServices());
        ft.commit();
    }

    protected void showdialog(int id) {
        Dialog dialog;
        switch (id) {
            case DATE_DLG:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int date = cal.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), dpd, year, month, date);
                dialog.show();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = null;
            try {
                date1 = sdf.parse(""+ year + "-" + ""+ (month + 1) + "-" + ""+dayOfMonth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_servicedate.setText(fcalls.convertdate(sdf.format(date1)));
            et_reading.requestFocus();
        }
    };

}
