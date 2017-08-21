package com.chetangani.myapp.fragments.fueltracker;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.values.FunctionCalls;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleFuelTracker extends Fragment {
    public static final int DATE_DLG = 1;

    View view;
    private EditText et_curreading;
    private EditText et_fuelprice;
    private EditText et_fuelamount;
    private EditText et_fuelfilled;
    private EditText et_fueldate;
    private Button fueldetails_btn;
    FunctionCalls fcall;
    String curreading="", fuelprice="", fuelamount="", fuelfilled="", fueldate="";
    Database database;
    int year=0, month=0, date=0;
    FragmentManager fragmentManager;

    public VehicleFuelTracker() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vehicle_fuel_tracker, container, false);

        initialize();

        et_fueldate.setText(fcall.convertdate(fcall.getcurrentdate()));

        fuelfilledresult();

        fueldetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).hidekeyboard();
                fueldetails(v);
            }
        });

        et_fuelamount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_fueldate.requestFocus();
                    ((MainActivity) getActivity()).hidekeyboard();
                }
                return false;
            }
        });

        return view;
    }

    private void initialize() {
        et_curreading = (EditText) view.findViewById(R.id.et_currentreading);
        et_fuelprice = (EditText) view.findViewById(R.id.et_fuelprice);
        et_fuelamount = (EditText) view.findViewById(R.id.et_fuelamount);
        et_fuelfilled = (EditText) view.findViewById(R.id.et_fuelfilled);
        et_fueldate = (EditText) view.findViewById(R.id.et_fuelfilleddate);
        fueldetails_btn = (Button) view.findViewById(R.id.addfueldetails_btn);

        fcall = new FunctionCalls();
        fragmentManager = getFragmentManager();

        database = new Database(getActivity());
        database.open();
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
                ((MainActivity) getActivity()).hidekeyboard();
                showdialog(DATE_DLG);
            }
        });
    }

    private void fueldetails(View view) {
        curreading = et_curreading.getText().toString();
        if (!curreading.equals("")) {
            fuelprice = et_fuelprice.getText().toString();
            if (!fuelprice.equals("")) {
                fuelamount = et_fuelamount.getText().toString();
                if (!fuelamount.equals("")) {
                    fueldate = fcall.getreturndate(et_fueldate.getText().toString());
                    if (!fueldate.equals("")) {
                        fuelfilled = et_fuelfilled.getText().toString();
                        String id = "", lastreading="";
                        Cursor details = database.fueldetails();
                        if (details.getCount() > 0) {
                            Cursor getid = database.getlastfueldetails();
                            getid.moveToNext();
                            id = getid.getString(getid.getColumnIndex("_id"));
                            lastreading = getid.getString(getid.getColumnIndex("start_reading"));
                            if (Integer.parseInt(curreading) < Integer.parseInt(lastreading)) {
                                et_curreading.setError("Enter current reading more than previous reading "+lastreading);
                                ((MainActivity) getActivity()).showsnackbar(view, "Enter current reading more than previous reading");
                                return;
                            }
                        }
                        database.insertfueldetails(curreading, fuelprice, fuelamount, fuelfilled, fueldate);
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
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container_main, new AllFuelDetails());
                        ft.commit();
                    } else et_fueldate.setText("Take Date");
                } else et_fuelamount.setError("Enter Amount");
            } else et_fuelprice.setError("Enter Fuel Price");
        } else et_curreading.setError("Enter Reading");
    }

    protected void showdialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DLG:
                if (date == 0) {
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    date = cal.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dp = new DatePickerDialog(getActivity(), dpd, year, month, date);
                dialog = dp;
                dialog.show();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int yr, int monthofYear, int dayOfMonth) {
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
            ((MainActivity) getActivity()).hidekeyboard();
            et_fueldate.setText(fcall.convertdate(sdf.format(date1)));
            et_fueldate.setSelection(et_fueldate.getText().length());
        }
    };
}
