package com.chetangani.myapp.fragments.fueltracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
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
import android.widget.EditText;
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
import java.util.Objects;

public class VehicleFuelTracker extends Fragment {
    private static final int DATE_DLG = 1;

    View view;
    private EditText et_curreading;
    private EditText et_fuelprice;
    private EditText et_fuelamount;
    private EditText et_fuelfilled;
    private EditText et_fueldate;
    private Button fueldetails_btn;
    FunctionCalls fcall;
    String curreading="", fuelprice="", fuelamount="", fuelfilled="", fueldate="", fuelbrand="0";
    Database database;
    Spinner sp_fuel_brand;
    ArrayList<String> fuel_brand_list;
    ArrayAdapter<String> fuel_brand_adapter;
    int year=0, month=0, date=0;
    FragmentManager fragmentManager;

    public VehicleFuelTracker() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vehicle_fuel_tracker, container, false);

        initialize();

        et_fueldate.setText(fcall.convertdate(fcall.getcurrentdate()));

        fuelfilledresult();

        fueldetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).hidekeyboard();
                fueldetails(v);
            }
        });

        et_fuelamount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_fueldate.requestFocus();
                    ((MainActivity) Objects.requireNonNull(getActivity())).hidekeyboard();
                }
                return false;
            }
        });

        sp_fuel_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fuelbrand = String.valueOf(parent.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void initialize() {
        et_curreading = view.findViewById(R.id.et_currentreading);
        et_fuelprice = view.findViewById(R.id.et_fuelprice);
        et_fuelamount = view.findViewById(R.id.et_fuelamount);
        et_fuelfilled = view.findViewById(R.id.et_fuelfilled);
        et_fueldate = view.findViewById(R.id.et_fuelfilleddate);
        fueldetails_btn = view.findViewById(R.id.addfueldetails_btn);

        sp_fuel_brand = view.findViewById(R.id.sp_fuel_brand);
        fuel_brand_list = new ArrayList<>();

        fcall = new FunctionCalls();
        fragmentManager = getFragmentManager();

        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        fuel_brand_list.addAll(Arrays.asList(getResources().getStringArray(R.array.fuel_brands)));
        fuel_brand_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, fuel_brand_list);
        sp_fuel_brand.setAdapter(fuel_brand_adapter);
    }

    private void fuelfilledresult() {
        et_fuelprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fuelamount = et_fuelamount.getText().toString();
                if (!fuelamount.equals("")) {
                    if (s.length() > 0) {
                        double price = Double.parseDouble(s.toString());
                        double amount = Double.parseDouble(fuelamount);
                        et_fuelfilled.setText(new DecimalFormat("##.##").format(amount / price));
                    } else {
                        et_fuelfilled.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_fuelamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fuelprice = et_fuelprice.getText().toString();
                if (!fuelprice.equals("")) {
                    if (s.length() > 0) {
                        double price = Double.parseDouble(fuelprice);
                        double amount = Double.parseDouble(s.toString());
                        et_fuelfilled.setText(new DecimalFormat("##.##").format(amount / price));
                    } else {
                        et_fuelfilled.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_fueldate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showdialog(DATE_DLG);
            }
        });

        et_fueldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).hidekeyboard();
                showdialog(DATE_DLG);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void fueldetails(View view) {
        if (!fuelbrand.equals("0")) {
            curreading = et_curreading.getText().toString();
            if (!curreading.equals("")) {
                fuelprice = et_fuelprice.getText().toString();
                if (!fuelprice.equals("")) {
                    fuelamount = et_fuelamount.getText().toString();
                    if (!fuelamount.equals("")) {
                        fueldate = fcall.getreturndate(et_fueldate.getText().toString());
                        if (!fueldate.equals("")) {
                            fuelfilled = et_fuelfilled.getText().toString();
                            String id = "", lastreading;
                            Cursor details = database.fueldetails();
                            if (details.getCount() > 0) {
                                Cursor getid = database.getlastfueldetails();
                                getid.moveToNext();
                                id = getid.getString(getid.getColumnIndex("_id"));
                                lastreading = getid.getString(getid.getColumnIndex("start_reading"));
                                if (Integer.parseInt(curreading) < Integer.parseInt(lastreading)) {
                                    et_curreading.setError("Enter current reading more than previous reading "+lastreading);
                                    ((MainActivity) Objects.requireNonNull(getActivity())).showsnackbar(view, "Enter current reading more than previous reading");
                                    return;
                                }
                            }
                            database.insertfueldetails(curreading, fuelprice, fuelamount, fuelfilled, fueldate, fuelbrand);
                            if (!id.equals("")) {
                                Cursor getdetails = database.getlastfueldetails();
                                if (getdetails.getCount() > 0) {
                                    getdetails.moveToNext();
                                    String reading = getdetails.getString(getdetails.getColumnIndex("start_reading"));
                                    String date = getdetails.getString(getdetails.getColumnIndex("fuel_date"));
                                    Cursor updatecursor = database.updatefueldetails(reading, date, id);
                                    updatecursor.moveToNext();
                                }
                            }
                            fragmentManager.popBackStack();
                            FragmentTransaction ft;
                            if (getFragmentManager() != null) {
                                ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.container_main, new AllFuelDetails());
                                ft.commit();
                            }
                        } else et_fueldate.setText("Take Date");
                    } else et_fuelamount.setError("Enter Amount");
                } else et_fuelprice.setError("Enter Fuel Price");
            } else et_curreading.setError("Enter Reading");
        } else Snackbar.make(view, "Please select Fuel brand & proceed...", Snackbar.LENGTH_SHORT).show();
    }

    protected void showdialog(int id) {
        Dialog dialog;
        switch (id) {
            case DATE_DLG:
                if (date == 0) {
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    date = cal.get(Calendar.DAY_OF_MONTH);
                }
                dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), dpd, year, month, date);
                dialog.show();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int yr, int monthofYear, int dayOfMonth) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            try {
                date = dayOfMonth;
                month = monthofYear;
                year = yr;
                date1 = sdf.parse(""+ yr + "-" + ""+ (monthofYear + 1) + "-" + ""+dayOfMonth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((MainActivity) Objects.requireNonNull(getActivity())).hidekeyboard();
            et_fueldate.setText(fcall.convertdate(sdf.format(date1)));
            et_fueldate.setSelection(et_fueldate.getText().length());
        }
    };
}
